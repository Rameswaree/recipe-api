package com.loveforfood.recipes.dto;

import java.util.List;

public record RecipeResponse(Long id, String name, boolean vegetarian, int servings, List<IngredientResponse> ingredients, String instructions) {}
