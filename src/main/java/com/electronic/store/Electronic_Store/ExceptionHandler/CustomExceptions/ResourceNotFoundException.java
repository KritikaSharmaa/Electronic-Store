package com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
