package com.loveforfood.recipes.dto;

/**
 * Represents a response containing ingredient details.
 *
 * @param id   the unique identifier of the ingredient
 * @param name the name of the ingredient
 */
public record IngredientResponse(Long id, String name) {
}
