package com.loveforfood.recipes.dto;

import com.loveforfood.recipes.entity.Ingredient;

import java.util.List;
import java.util.Optional;

public record RecipeUpdateRequest(
        Optional<String> name,
        Optional<Boolean> vegetarian,
        Optional<Integer> servings,
        Optional<List<Ingredient>> ingredients,
        Optional<String> instructions) {}
