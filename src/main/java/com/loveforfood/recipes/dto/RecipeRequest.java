package com.loveforfood.recipes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Represents a request to create or update a recipe.
 *
 * @param name        the name of the recipe
 * @param vegetarian  indicates if the recipe is vegetarian
 * @param servings    the number of servings the recipe yields
 * @param ingredients a list of ingredients required for the recipe
 * @param instructions the cooking instructions for the recipe
 */
public record RecipeRequest(
        @NotBlank(message = "Recipe name must not be an empty string")
        String name,
        boolean vegetarian,
        @Min(value = 1, message = "Servings must be at least 1")
        int servings,
        @Valid
        @NotEmpty(message = "Ingredients list must not be empty")
        List<IngredientRequest> ingredients,
        @NotBlank(message = "Instructions must not be blank")
        String instructions){}
