package com.loveforfood.recipes.service;

import com.loveforfood.recipes.dto.RecipeRequest;
import com.loveforfood.recipes.dto.RecipeResponse;
import com.loveforfood.recipes.entity.Recipe;
import com.loveforfood.recipes.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeResponse getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> new RecipeResponse(recipe.getId(), null, false, 0, null, null))
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));
    }

    public List<RecipeResponse> getAllRecipes() {
        recipeRepository.findAll();
        return new ArrayList<>();
    }

    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public RecipeResponse addRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = new Recipe();
        return new RecipeResponse(recipe.getId(), null, false, 0, null, null);
    }

    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    // Update the existing recipe with new values from recipeRequest
                    return new RecipeResponse(existingRecipe.getId(), null, false, 0, null, null);
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
    }
}
