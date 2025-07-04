package com.loveforfood.recipes.dto;

import java.util.List;
import java.util.Optional;

public record RecipeUpdateRequest(
        Optional<String> name,
        Optional<Boolean> vegetarian,
        Optional<Integer> servings,
        Optional<List<IngredientRequest>> ingredients,
        Optional<String> instructions) {}
