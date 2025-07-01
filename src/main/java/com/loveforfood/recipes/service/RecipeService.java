package com.loveforfood.recipes.service;

import com.loveforfood.recipes.dto.RecipeRequest;
import com.loveforfood.recipes.dto.RecipeResponse;
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
                .map(recipe -> new RecipeResponse())
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));
    }

    public List<RecipeResponse> getAllRecipes() {
        recipeRepository.findAll();
        return new ArrayList<RecipeResponse>();
    }

    public void deleteRecipe(long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public RecipeResponse addRecipe(RecipeRequest recipeRequest) {
        return new RecipeResponse();
    }

    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) {
        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    // Update the existing recipe with new values from recipeRequest
                    return new RecipeResponse();
                })
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
    }
}
