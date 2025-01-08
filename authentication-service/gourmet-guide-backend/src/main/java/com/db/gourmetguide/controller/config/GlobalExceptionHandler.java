package com.db.gourmetguide.controller.config;

import com.db.gourmetguide.exception.InvalidCredentialsException;
import com.db.gourmetguide.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException() {
        return new ResponseEntity<>("User does not exist!", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException() {
        return new ResponseEntity<>("Wrong password! Please try again!", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}