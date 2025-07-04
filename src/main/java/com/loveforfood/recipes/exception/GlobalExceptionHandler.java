package com.loveforfood.recipes.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return CustomExceptionError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(Objects.requireNonNull(ex.getFieldError()).getField() +" : "+ ex.getFieldError().getDefaultMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Malformed JSON request";

        // Optionally check cause for InvalidFormatException for detailed info
        if (ex.getCause() instanceof InvalidFormatException ife) {
            String targetType = ife.getTargetType().getSimpleName();
            String value = ife.getValue().toString();
            message = String.format("Invalid value '%s' for field expecting type %s", value, targetType);
        }

        return Map.of("error", message);
    }
}
