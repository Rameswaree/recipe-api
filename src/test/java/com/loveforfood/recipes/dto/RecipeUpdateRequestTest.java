package com.loveforfood.recipes.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validUpdateRequest_shouldHaveNoViolations() {
        RecipeUpdateRequest request = new RecipeUpdateRequest(
                Optional.of("New Name"),
                Optional.of(true),
                Optional.of(4),
                Optional.of(List.of(new IngredientRequest(null, "Salt"))),
                Optional.of("Updated instructions")
        );

        Set<ConstraintViolation<RecipeUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    public void invalidIngredientName_shouldTriggerViolation() {
        RecipeUpdateRequest request = new RecipeUpdateRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(List.of(new IngredientRequest(null, ""))),  // invalid name
                Optional.empty()
        );

        // Manually unwrap and validate nested IngredientRequest
        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request.ingredients().orElse(List.of()).getFirst());
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
    }

    @Test
    public void emptyOptionalFields_shouldNotCauseValidationErrors() {
        RecipeUpdateRequest request = new RecipeUpdateRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        Set<ConstraintViolation<RecipeUpdateRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
