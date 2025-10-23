package com.vivek.backend.Management.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message)
    {
        super(message);
    }
}
