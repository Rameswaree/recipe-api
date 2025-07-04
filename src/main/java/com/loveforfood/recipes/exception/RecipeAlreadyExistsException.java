package com.loveforfood.recipes.exception;

public class RecipeAlreadyExistsException extends RuntimeException {
    public RecipeAlreadyExistsException(String message) {
        super(message);
    }
}
