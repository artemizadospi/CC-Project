package com.db.gourmetguide.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid credentials!")
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(){}
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
