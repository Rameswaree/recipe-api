package com.loveforfood.recipes.service;

import com.loveforfood.recipes.dto.RecipeRequest;
import com.loveforfood.recipes.dto.RecipeResponse;
import com.loveforfood.recipes.dto.RecipeUpdateRequest;
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

    public RecipeResponse updateRecipe(Long id, RecipeUpdateRequest recipeUpdateRequest) {
        // Update the existing recipe with new values from recipeRequest
        Recipe recipe = recipeRepository.findById(id)
                                        .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        if(recipeUpdateRequest == null) {
            throw new IllegalArgumentException("Recipe update request cannot be null");
        } else {
            updateRecipeFields(recipe, recipeUpdateRequest);
        }

        Recipe updatedRecipe = recipeRepository.save(recipe);
        return createRecipeResponse(updatedRecipe);
    }

    private void updateRecipeFields(Recipe recipe, RecipeUpdateRequest recipeUpdateRequest) {
        if(recipeUpdateRequest.name().isPresent()) {
            recipe.setName(recipeUpdateRequest.name().get());
        }
        if(recipeUpdateRequest.vegetarian().isPresent()) {
            recipe.setVegetarian(recipeUpdateRequest.vegetarian().get());
        }
        if(recipeUpdateRequest.servings().isPresent()) {
            recipe.setServings(recipeUpdateRequest.servings().get());
        }
        if(recipeUpdateRequest.ingredients().isPresent()) {
            List<Ingredient> updatedIngredients = recipeUpdateRequest.ingredients().get()
                    .stream()
                    .map(ingredient -> {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredient.getName());
                        return newIngredient;
                    }).toList();
            recipe.setIngredients(updatedIngredients);
        }
        if(recipeUpdateRequest.instructions().isPresent()) {
            recipe.setInstructions(recipeUpdateRequest.instructions().get());
        }
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
