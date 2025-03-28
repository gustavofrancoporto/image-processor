package com.bix.imageprocessor.web.exception.model;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User email already exists.");
    }
}
