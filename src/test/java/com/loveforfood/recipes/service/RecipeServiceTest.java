package com.loveforfood.recipes.service;

import com.loveforfood.recipes.dto.*;
import com.loveforfood.recipes.entity.Ingredient;
import com.loveforfood.recipes.entity.Recipe;
import com.loveforfood.recipes.exception.DuplicateIngredientException;
import com.loveforfood.recipes.exception.RecipeAlreadyExistsException;
import com.loveforfood.recipes.exception.RecipeNotFoundException;
import com.loveforfood.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void addRecipe_shouldSaveRecipeSuccessfully() {
        RecipeRequest request = new RecipeRequest("Pasta", true, 2,
                List.of(new IngredientRequest(null, "Salt")), "Boil water");

        when(recipeRepository.existsByNameIgnoreCase("Pasta")).thenReturn(false);
        when(recipeRepository.save(any())).thenAnswer(inv -> {
            Recipe r = inv.getArgument(0);
            r.setId(1L);
            r.getIngredients().getFirst().setId(1L);
            return r;
        });

        RecipeResponse response = recipeService.addRecipe(request);

        assertThat(response.name()).isEqualTo("Pasta");
        assertThat(response.ingredients()).hasSize(1);
        assertThat(response.ingredients().getFirst().name()).isEqualTo("Salt");
    }

    @Test
    public void addRecipe_shouldThrowExceptionForDuplicateIngredient() {
        RecipeRequest request = new RecipeRequest("Soup", true, 2,
                List.of(new IngredientRequest(1L, "Salt"), new IngredientRequest(2L, "salt")), "Cook");

        when(recipeRepository.existsByNameIgnoreCase("Soup")).thenReturn(false);

        DuplicateIngredientException duplicateIngredientException = assertThrows(DuplicateIngredientException.class, () -> recipeService.addRecipe(request));
        assertEquals("Duplicate ingredient name: salt", duplicateIngredientException.getMessage());
    }

    @Test
    public void addRecipe_shouldThrowExceptionForDuplicateRecipeName() {
        RecipeRequest request = new RecipeRequest("Curry", false, 3,
                List.of(new IngredientRequest(null, "Onion")), "Mix");

        when(recipeRepository.existsByNameIgnoreCase("Curry")).thenReturn(true);

        RecipeAlreadyExistsException recipeAlreadyExistsException = assertThrows(RecipeAlreadyExistsException.class, () -> recipeService.addRecipe(request));
        assertEquals("A recipe with this name already exists: Curry", recipeAlreadyExistsException.getMessage());
    }

    @Test
    public void updateRecipe_shouldUpdateSuccessfully() {
        Recipe existing = new Recipe(1L, "Old", false, 2, "Old steps", new ArrayList<>());

        Ingredient i1 = new Ingredient(1L, "Pepper", existing);
        existing.getIngredients().add(i1);

        RecipeUpdateRequest update = new RecipeUpdateRequest(
                Optional.of("New"), Optional.of(true), Optional.of(4),
                Optional.of(List.of(new IngredientRequest(1L, "Black Pepper"), new IngredientRequest(null, "Salt"))),
                Optional.of("New instructions")
        );

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(recipeRepository.existsByNameIgnoreCase("New")).thenReturn(false);
        when(recipeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RecipeResponse response = recipeService.updateRecipe(1L, update);

        assertThat(response.name()).isEqualTo("New");
        assertThat(response.ingredients()).extracting(IngredientResponse::name)
                .containsExactlyInAnyOrder("Black Pepper", "Salt");
    }

    @Test
    public void updateRecipe_shouldThrowOnDuplicateIngredient() {
        Recipe existing = new Recipe(1L, "Stew", true, 3, "Cook", new ArrayList<>());

        Ingredient i1 = new Ingredient(1L, "Salt", existing);
        existing.getIngredients().add(i1);

        RecipeUpdateRequest update = new RecipeUpdateRequest(
                Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(List.of(new IngredientRequest(null, "salt"))),
                Optional.empty()
        );

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(existing));

        DuplicateIngredientException duplicateIngredientException = assertThrows(DuplicateIngredientException.class, () -> recipeService.updateRecipe(1L, update));
        assertEquals("Ingredient name must be unique: salt", duplicateIngredientException.getMessage());
    }

    @Test
    public void updateRecipe_shouldThrowIfRecipeDoesNotExist() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        RecipeNotFoundException recipeNotFoundException = assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(1L, new RecipeUpdateRequest(
                Optional.of("Anything"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
        )));
        assertEquals("Recipe not found with id: 1", recipeNotFoundException.getMessage());
    }

    @Test
    public void deleteRecipe_shouldDeleteIfExists() {
        when(recipeRepository.existsById(1L)).thenReturn(true);

        recipeService.deleteRecipe(1L);

        verify(recipeRepository).deleteById(1L);
    }

    @Test
    public void deleteRecipe_shouldThrowIfNotExists() {
        when(recipeRepository.existsById(1L)).thenReturn(false);

        RecipeNotFoundException recipeNotFoundException = assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe(1L));
        assertEquals("Recipe not found with id: 1", recipeNotFoundException.getMessage());
    }

    @Test
    public void getRecipeById_shouldReturnRecipeResponse_whenFound() {
        Ingredient ingredient = new Ingredient(1L, "Tomato", null);
        Recipe recipe = new Recipe(1L, "Tomato Soup", true, 2, "Boil it", List.of(ingredient));
        ingredient.setRecipe(recipe);

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        RecipeResponse response = recipeService.getRecipeById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Tomato Soup");
        assertThat(response.ingredients()).hasSize(1);
        assertThat(response.ingredients().getFirst().name()).isEqualTo("Tomato");
    }

    @Test
    public void getRecipeById_shouldThrowException_whenNotFound() {
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        RecipeNotFoundException recipeNotFoundException = assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(99L));
        assertEquals("Recipe not found with id: 99", recipeNotFoundException.getMessage());
    }

    @Test
    public void getAllRecipes_shouldReturnListOfRecipeResponses() {
        Recipe r1 = new Recipe(1L, "Soup", true, 2, "Boil", new ArrayList<>());
        Recipe r2 = new Recipe(2L, "Salad", false, 1, "Chop", new ArrayList<>());
        when(recipeRepository.findAll()).thenReturn(List.of(r1, r2));

        List<RecipeResponse> recipes = recipeService.getAllRecipes();

        assertThat(recipes).hasSize(2);
        assertThat(recipes).extracting(RecipeResponse::name)
                .containsExactlyInAnyOrder("Soup", "Salad");
    }

    @Test
    public void search_shouldReturnFilteredRecipeResponses() {
        Recipe recipe = new Recipe(10L, "Pasta", true, 3, "Boil pasta", new ArrayList<>());
        recipe.getIngredients().add(new Ingredient(1L, "Penne", recipe));

        when(recipeRepository.search(true, 3, "Penne", null, "Boil"))
                .thenReturn(List.of(recipe));

        List<RecipeResponse> result = recipeService.search(true, 3, "Penne", null, "Boil");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Pasta");
        assertThat(result.getFirst().ingredients().getFirst().name()).isEqualTo("Penne");
    }

}
