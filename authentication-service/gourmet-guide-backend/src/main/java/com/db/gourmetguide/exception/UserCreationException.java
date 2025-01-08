package com.db.gourmetguide.exception;

public class UserCreationException extends RuntimeException {
    public UserCreationException() {}
    public UserCreationException(String message) {
        super(message);
    }
}