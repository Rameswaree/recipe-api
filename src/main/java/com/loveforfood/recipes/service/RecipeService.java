package com.loveforfood.recipes.service;

import com.loveforfood.recipes.dto.*;
import com.loveforfood.recipes.entity.Ingredient;
import com.loveforfood.recipes.entity.Recipe;
import com.loveforfood.recipes.exception.DuplicateIngredientException;
import com.loveforfood.recipes.exception.RecipeAlreadyExistsException;
import com.loveforfood.recipes.exception.RecipeNotFoundException;
import com.loveforfood.recipes.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing recipes.
 * Provides methods to add, update, delete, and retrieve recipes.
 */
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
        if (recipeRepository.existsByNameIgnoreCase(recipeRequest.name())) {
            throw new RecipeAlreadyExistsException("A recipe with this name already exists: " + recipeRequest.name());
        }

        Recipe recipe = new Recipe();
        recipe.setName(recipeRequest.name());
        recipe.setVegetarian(recipeRequest.vegetarian());
        recipe.setServings(recipeRequest.servings());

        Set<String> savedRecipes = new HashSet<>();
        List<Ingredient> ingredientList = recipeRequest.ingredients()
                .stream()
                .map(ingredientRequest -> {

                    String name = ingredientRequest.name().toLowerCase();
                    if (!savedRecipes.add(name)) {
                        throw new DuplicateIngredientException("Duplicate ingredient name: " + ingredientRequest.name());
                    }

                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingredientRequest.name());
                    newIngredient.setRecipe(recipe);
                    return newIngredient;
                }).toList();
        recipe.setIngredients(ingredientList);
        recipe.setInstructions(recipeRequest.instructions());

        Recipe newRecipe = recipeRepository.save(recipe);

        return createRecipeResponse(newRecipe);
    }

    public RecipeResponse updateRecipe(Long id, RecipeUpdateRequest recipeUpdateRequest) {
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
            String newName = recipeUpdateRequest.name().get();
            if (!newName.equalsIgnoreCase(recipe.getName()) &&
                    recipeRepository.existsByNameIgnoreCase(newName)) {
                throw new RecipeAlreadyExistsException("Another recipe already exists with name: " + newName);
            }

            recipe.setName(recipeUpdateRequest.name().get());
        }
        if(recipeUpdateRequest.vegetarian().isPresent()) {
            recipe.setVegetarian(recipeUpdateRequest.vegetarian().get());
        }
        if(recipeUpdateRequest.servings().isPresent()) {
            recipe.setServings(recipeUpdateRequest.servings().get());
        }
        if(recipeUpdateRequest.ingredients().isPresent()) {
            List<IngredientRequest> recipeIngredientRequest = recipeUpdateRequest.ingredients().get();

            updateIngredients(recipe, recipeIngredientRequest);
        }
        if(recipeUpdateRequest.instructions().isPresent()) {
            recipe.setInstructions(recipeUpdateRequest.instructions().get());
        }
    }

    private void updateIngredients(Recipe recipe, List<IngredientRequest> recipeIngredientRequest) {
        Map<Long, Ingredient> existingById = recipe.getIngredients().stream()
                .filter(i -> i.getId() != null)
                .collect(Collectors.toMap(Ingredient::getId, i -> i));

        Set<String> existingNames = recipe.getIngredients().stream()
                .map(Ingredient::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        for(IngredientRequest ingredientRequest : recipeIngredientRequest) {

            String ingredientName = ingredientRequest.name().toLowerCase();

            if (ingredientRequest.id() != null && existingById.containsKey(ingredientRequest.id())) {
                Ingredient ingredient = existingById.get(ingredientRequest.id());

                if (existingNames.contains(ingredientName) && !ingredient.getName().equalsIgnoreCase(ingredientName)) {
                    throw new DuplicateIngredientException("Ingredient name must be unique: " + ingredientName);
                }

                existingNames.remove(ingredient.getName().toLowerCase());
                ingredient.setName(ingredientRequest.name());
            } else {

                if (existingNames.contains(ingredientName)) {
                    throw new DuplicateIngredientException("Ingredient name must be unique: " + ingredientName);
                }

                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(ingredientRequest.name());
                newIngredient.setRecipe(recipe);
                recipe.getIngredients().add(newIngredient);
            }
            existingNames.add(ingredientName);
        }
    }

    public List<RecipeResponse> search(Boolean vegetarian, Integer servings, String include, String exclude, String instructions) {
        return recipeRepository.search(vegetarian, servings, include, exclude, instructions)
                .stream()
                .map(this::createRecipeResponse)
                .toList();
    }

    private RecipeResponse createRecipeResponse(Recipe recipe) {
        List<IngredientResponse> ingredients = recipe.getIngredients().stream()
                .map(ingredient -> new IngredientResponse(ingredient.getId(), ingredient.getName()))
                .toList();
        return new RecipeResponse(recipe.getId(), recipe.getName(), recipe.isVegetarian(), recipe.getServings(), ingredients, recipe.getInstructions());
    }
}
