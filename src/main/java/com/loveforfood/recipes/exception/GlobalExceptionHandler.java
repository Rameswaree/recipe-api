package com.loveforfood.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the Recipe API.
 * This class handles various exceptions and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionError handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return CustomExceptionError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = RecipeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomExceptionError handleRecipeAlreadyExistsException(RecipeAlreadyExistsException ex) {
        return CustomExceptionError.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = DuplicateIngredientException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CustomExceptionError handleDuplicateIngredientException(DuplicateIngredientException ex) {
        return CustomExceptionError.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionError handleIllegalArgumentException(IllegalArgumentException ex) {
        return CustomExceptionError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }
}
