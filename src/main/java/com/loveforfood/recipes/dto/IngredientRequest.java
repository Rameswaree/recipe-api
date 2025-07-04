package com.loveforfood.recipes.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a request to create or update an ingredient.
 * This record is used to encapsulate the data required for ingredient operations.
 *
 * @param id   The unique identifier of the ingredient (optional for creation).
 * @param name The name of the ingredient, must not be blank.
 */
public record IngredientRequest(
        Long id,
        @NotBlank(message = "Ingredient name must not be blank")
        String name) {
}
