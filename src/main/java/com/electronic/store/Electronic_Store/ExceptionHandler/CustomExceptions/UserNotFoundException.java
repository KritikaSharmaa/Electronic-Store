package com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
