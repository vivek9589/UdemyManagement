// File: ErrorResponse.java
package com.vivek.backend.Management.exception;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and setters
}