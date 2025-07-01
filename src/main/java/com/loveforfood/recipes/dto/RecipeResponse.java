package com.loveforfood.recipes.dto;

import com.loveforfood.recipes.entity.Ingredient;

import java.util.List;

public record RecipeResponse(Long id, String name, boolean vegetarian, int servings, List<Ingredient> ingredients, String instructions) {}
