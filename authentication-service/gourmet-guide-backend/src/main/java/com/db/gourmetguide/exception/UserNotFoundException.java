package com.db.gourmetguide.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User does not exist")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {}
    public UserNotFoundException(String message) {
        super(message);
    }
}