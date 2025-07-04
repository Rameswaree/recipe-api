package com.loveforfood.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loveforfood.recipes.dto.IngredientRequest;
import com.loveforfood.recipes.dto.RecipeRequest;
import com.loveforfood.recipes.dto.RecipeUpdateRequest;
import com.loveforfood.recipes.entity.Ingredient;
import com.loveforfood.recipes.entity.Recipe;
import com.loveforfood.recipes.repository.RecipeRepository;
import com.loveforfood.recipes.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDatabase() {
        recipeRepository.deleteAll();
    }

    @Test
    public void shouldCreateAndFetchRecipe() throws Exception {
        RecipeRequest request = new RecipeRequest(
                "Tomato Soup", true, 2,
                List.of(new IngredientRequest(null, "Tomato")),
                "Boil the tomato");

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tomato Soup"))
                .andExpect(jsonPath("$.ingredients[0].name").value("Tomato"));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldUpdateRecipe() throws Exception {
        // Setup initial recipe with ingredient with id
        Recipe recipe = new Recipe();
        recipe.setName("Pasta");
        recipe.setVegetarian(false);
        recipe.setServings(1);
        recipe.setInstructions("Boil pasta");

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Pasta");
        ingredient.setRecipe(recipe);
        recipe.setIngredients(List.of(ingredient));

        recipe = recipeRepository.save(recipe);

         // Create update request with ingredient IDs set
        IngredientRequest ingredientRequestWithId = new IngredientRequest(ingredient.getId(), "Pasta");
        IngredientRequest newIngredientRequest = new IngredientRequest(null, "Sauce");

        RecipeUpdateRequest updateRequest = new RecipeUpdateRequest(
                Optional.of("Veg Pasta"),
                Optional.of(true),
                Optional.of(2),
                Optional.of(List.of(ingredientRequestWithId, newIngredientRequest)),
                Optional.of("Boil pasta and add sauce")
        );

        mockMvc.perform(put("/api/recipes?id=" + recipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Veg Pasta"))
                .andExpect(jsonPath("$.ingredients", hasSize(2)));
    }


    @Test
    public void shouldDeleteRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setName("To Delete");
        recipe.setVegetarian(true);
        recipe.setServings(1);
        recipe.setInstructions("To be deleted");
        recipe.setIngredients(List.of());
        recipe = recipeRepository.save(recipe);

        mockMvc.perform(delete("/api/recipes?id=" + recipe.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/recipes/" + recipe.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSearchRecipes() throws Exception {
        // Create RecipeRequest with ingredient, same as controller input
        RecipeRequest request = new RecipeRequest(
                "Salad",
                true,
                1,
                List.of(new IngredientRequest(null, "Lettuce")),
                "Toss veggies"
        );

        // Save using service (which calls repository internally)
        recipeService.addRecipe(request);

        // Then call search endpoint
        mockMvc.perform(get("/api/recipes/search")
                        .param("vegetarian", "true")
                        .param("include", "Lettuce")
                        .param("exclude", "")
                        .param("servings", "")
                        .param("instructions", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Salad"))
                .andExpect(jsonPath("$[0].ingredients[0].name").value("Lettuce"));
    }
}
