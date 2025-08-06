package com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message) {
        super(message);
    }
}
