package com.loveforfood.recipes.dto;

import jakarta.validation.constraints.NotBlank;

public record IngredientRequest(
        Long id,
        @NotBlank(message = "Ingredient name must not be blank")
        String name) {
}
