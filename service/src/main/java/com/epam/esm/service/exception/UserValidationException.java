package com.epam.esm.service.exception;

public class UserValidationException extends RuntimeException{

    public UserValidationException(String message) {
        super(message);
    }
}
