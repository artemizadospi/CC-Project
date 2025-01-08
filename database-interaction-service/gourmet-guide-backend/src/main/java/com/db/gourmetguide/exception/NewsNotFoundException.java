package com.db.gourmetguide.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "News does not exist")
public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() {}
    public NewsNotFoundException(String message) {
        super(message);
    }
}