package com.loveforfood.recipes.dto;

import java.util.List;

/**
 * Represents a response containing recipe details.
 *
 * @param id          the unique identifier of the recipe
 * @param name        the name of the recipe
 * @param vegetarian  indicates if the recipe is vegetarian
 * @param servings    the number of servings the recipe makes
 * @param ingredients a list of ingredients required for the recipe
 * @param instructions the cooking instructions for the recipe
 */
public record RecipeResponse(
        Long id,
        String name,
        boolean vegetarian,
        int servings,
        List<IngredientResponse> ingredients,
        String instructions) {}
