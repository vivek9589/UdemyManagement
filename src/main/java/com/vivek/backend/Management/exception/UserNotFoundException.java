// File: UserNotFoundException.java
package com.vivek.backend.Management.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}