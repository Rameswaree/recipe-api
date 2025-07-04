package com.loveforfood.recipes.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Represents an error response for custom exceptions.
 * Contains HTTP status and error message.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomExceptionError {

    private HttpStatus status;
    private String message;
}
