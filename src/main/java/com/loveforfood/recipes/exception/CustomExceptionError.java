package com.loveforfood.recipes.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomExceptionError {

    private HttpStatus status;
    private String message;
}
