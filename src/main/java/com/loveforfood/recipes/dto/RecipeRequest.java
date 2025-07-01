package com.loveforfood.recipes.dto;

import com.loveforfood.recipes.entity.Ingredient;

import java.util.List;

public record RecipeRequest(String name, boolean vegetarian, int servings, List<Ingredient> ingredients, String instructions){}
