package com.loveforfood.recipes.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validRecipeRequest_shouldHaveNoViolations() {
        RecipeRequest request = new RecipeRequest(
                "Pasta",
                true,
                2,
                List.of(new IngredientRequest(null, "Tomato")),
                "Boil and mix"
        );

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    public void blankName_shouldTriggerViolation() {
        RecipeRequest request = new RecipeRequest(
                " ",
                true,
                2,
                List.of(new IngredientRequest(null, "Tomato")),
                "Instructions"
        );

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    public void zeroServings_shouldTriggerViolation() {
        RecipeRequest request = new RecipeRequest(
                "Salad",
                false,
                0,
                List.of(new IngredientRequest(1L, "Lettuce")),
                "Mix and serve"
        );

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("servings"));
    }

    @Test
    public void emptyIngredients_shouldTriggerViolation() {
        RecipeRequest request = new RecipeRequest(
                "Soup",
                false,
                1,
                List.of(), // empty ingredients
                "Cook everything"
        );

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("ingredients"));
    }

    @Test
    public void nullIngredientName_shouldTriggerNestedViolation() {
        RecipeRequest request = new RecipeRequest(
                "Soup",
                false,
                2,
                List.of(new IngredientRequest(null, null)), // invalid ingredient
                "Cook"
        );

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(request);

        // There should be a violation on ingredient.name
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("ingredients[0].name"));
    }

    @Test
    public void blankInstructions_shouldTriggerViolation() {
        RecipeRequest request = new RecipeRequest(
                "Cake",
                true,
                3,
                List.of(new IngredientRequest(null, "Flour")),
                " "
        );

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("instructions"));
    }
}