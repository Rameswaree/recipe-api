package com.loveforfood.recipes.service;

import com.loveforfood.recipes.dto.RecipeRequest;
import com.loveforfood.recipes.dto.RecipeResponse;
import com.loveforfood.recipes.entity.Ingredient;
import com.loveforfood.recipes.entity.Recipe;
import com.loveforfood.recipes.exception.RecipeNotFoundException;
import com.loveforfood.recipes.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeResponse getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(this::createRecipeResponse)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
    }

    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::createRecipeResponse)
                .toList();
    }

    public void deleteRecipe(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException("Recipe not found with id: " + recipeId);
        }
        recipeRepository.deleteById(recipeId);
    }

    public RecipeResponse addRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeRequest.name());
        recipe.setVegetarian(recipeRequest.vegetarian());
        recipe.setServings(recipeRequest.servings());

        List<Ingredient> ingredientList = recipeRequest.ingredients()
                .stream()
                .map(ingredient -> {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingredient.getName());
                    return newIngredient;
                }).toList();
        recipe.setIngredients(ingredientList);
        recipe.setInstructions(recipeRequest.instructions());

        Recipe newRecipe = recipeRepository.save(recipe);

        return createRecipeResponse(newRecipe);
    }

    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        // Update the existing recipe with new values from recipeRequest
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        recipe.setName(recipeRequest.name());
        recipe.setVegetarian(recipeRequest.vegetarian());
        recipe.setServings(recipeRequest.servings());
        recipe.setIngredients(recipeRequest.ingredients());
        recipe.setInstructions(recipeRequest.instructions());

        Recipe updatedRecipe = recipeRepository.save(recipe);
        return createRecipeResponse(updatedRecipe);
    }

    private RecipeResponse createRecipeResponse(Recipe recipe) {
        return new RecipeResponse(recipe.getId(), recipe.getName(), recipe.isVegetarian(), recipe.getServings(), recipe.getIngredients(), recipe.getInstructions());
    }

    public List<RecipeResponse> search(Boolean vegetarian, Integer servings, String include, String exclude, String instructions) {
        return recipeRepository.search(vegetarian, servings, include, exclude, instructions)
                .stream()
                .map(this::createRecipeResponse)
                .toList();
    }
}
