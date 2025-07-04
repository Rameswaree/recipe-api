package com.loveforfood.recipes.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validIngredientRequest_shouldHaveNoViolations() {
        IngredientRequest request = new IngredientRequest(1L, "Tomato");

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    public void blankIngredientName_shouldTriggerViolation() {
        IngredientRequest request = new IngredientRequest(2L, " ");

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);

        ConstraintViolation<IngredientRequest> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo("Ingredient name must not be blank");
    }

    @Test
    public void nullIngredientName_shouldTriggerViolation() {
        IngredientRequest request = new IngredientRequest(3L, null);

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        ConstraintViolation<IngredientRequest> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo("Ingredient name must not be blank");
    }
}
