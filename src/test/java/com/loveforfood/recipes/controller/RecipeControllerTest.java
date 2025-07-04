package com.loveforfood.recipes.controller;

import com.loveforfood.recipes.dto.*;
import com.loveforfood.recipes.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the RecipeController class.
 * This class tests the endpoints for adding, updating, deleting, retrieving, and searching recipes.
 */
@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeService recipeService;

    private RecipeResponse recipeResponse;

    @BeforeEach
    public void setup() {
        IngredientResponse ingredient = new IngredientResponse(1L, "Tomato");
        recipeResponse = new RecipeResponse(1L, "Tomato Soup", true, 2, List.of(ingredient), "Boil it");
    }

    @Test
    public void shouldAddRecipe() {
        RecipeRequest request = new RecipeRequest(
                "Tomato Soup", true, 2,
                List.of(new IngredientRequest(null, "Tomato")), "Boil it");

        when(recipeService.addRecipe(request)).thenReturn(recipeResponse);

        RecipeResponse response = recipeController.addRecipe(request);

        assertNotNull(response);
        assertEquals("Tomato Soup", response.name());
        verify(recipeService).addRecipe(request);
    }

    @Test
    public void shouldUpdateRecipe() {
        Long recipeId = 1L;
        RecipeUpdateRequest updateRequest = new RecipeUpdateRequest(
                Optional.of("Updated Soup"),
                Optional.of(false),
                Optional.of(3),
                Optional.of(List.of(new IngredientRequest(null, "Carrot"))),
                Optional.of("Cook well")
        );

        when(recipeService.updateRecipe(recipeId, updateRequest)).thenReturn(recipeResponse);

        RecipeResponse result = recipeController.updateRecipe(recipeId, updateRequest);

        assertNotNull(result);
        assertEquals("Tomato Soup", result.name());
        verify(recipeService).updateRecipe(recipeId, updateRequest);
    }

    @Test
    public void shouldDeleteRecipe() {
        Long recipeId = 1L;

        assertDoesNotThrow(() -> recipeController.deleteRecipe(recipeId));
        verify(recipeService).deleteRecipe(recipeId);
    }

    @Test
    public void shouldGetAllRecipes() {
        when(recipeService.getAllRecipes()).thenReturn(singletonList(recipeResponse));

        List<RecipeResponse> recipes = recipeController.getAllRecipes();

        assertEquals(1, recipes.size());
        assertEquals("Tomato Soup", recipes.getFirst().name());
    }

    @Test
    public void shouldGetRecipeById() {
        when(recipeService.getRecipeById(1L)).thenReturn(recipeResponse);

        RecipeResponse result = recipeController.getRecipe(1L);

        assertEquals("Tomato Soup", result.name());
        verify(recipeService).getRecipeById(1L);
    }

    @Test
    public void shouldSearchRecipes() {
        when(recipeService.search(true, 2, "Tomato", null, "boil"))
                .thenReturn(singletonList(recipeResponse));

        List<RecipeResponse> result = recipeController.searchRecipes(
                true, 2, "Tomato", null, "boil");

        assertEquals(1, result.size());
        assertEquals("Tomato Soup", result.getFirst().name());
    }
}
