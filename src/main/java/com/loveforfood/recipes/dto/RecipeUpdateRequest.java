package com.loveforfood.recipes.dto;

import java.util.List;
import java.util.Optional;

/**
 * Represents a request to update a recipe.
 *
 * @param name        the new name of the recipe, if provided
 * @param vegetarian  indicates if the recipe is vegetarian, if provided
 * @param servings    the new number of servings the recipe makes, if provided
 * @param ingredients a list of ingredients to be updated, if provided
 * @param instructions the new cooking instructions for the recipe, if provided
 */
public record RecipeUpdateRequest(
        Optional<String> name,
        Optional<Boolean> vegetarian,
        Optional<Integer> servings,
        Optional<List<IngredientRequest>> ingredients,
        Optional<String> instructions) {}
