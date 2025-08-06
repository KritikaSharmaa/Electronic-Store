package com.electronic.store.Electronic_Store.DTOs;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;

    public ErrorResponseDTO(LocalDateTime timestamp, int status, String message, String path){
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    //Getters & Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
