package com.loveforfood.recipes.controller;

import com.loveforfood.recipes.dto.RecipeRequest;
import com.loveforfood.recipes.dto.RecipeResponse;
import com.loveforfood.recipes.dto.RecipeUpdateRequest;
import com.loveforfood.recipes.service.RecipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse addRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        return recipeService.addRecipe(recipeRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RecipeResponse updateRecipe(@RequestParam("id") Long recipeId, @Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        return recipeService.updateRecipe(recipeId, recipeUpdateRequest);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@RequestParam("id") Long recipeId) {
        recipeService.deleteRecipe(recipeId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeResponse> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeResponse getRecipe(@PathVariable("id") Long recipeId) {
        return recipeService.getRecipeById(recipeId);
    }


    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeResponse> searchRecipes(@RequestParam(required = false) Boolean vegetarian,
                                              @RequestParam(required = false) Integer servings,
                                              @RequestParam(required = false) String include,
                                              @RequestParam(required = false) String exclude,
                                              @RequestParam(required = false) String instructions) {
        return recipeService.search(vegetarian, servings, include, exclude, instructions);
    }
}
