package com.loveforfood.recipes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

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
