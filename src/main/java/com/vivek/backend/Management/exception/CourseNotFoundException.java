package com.vivek.backend.Management.exception;

public class CourseNotFoundException extends RuntimeException{

    public CourseNotFoundException(String message)
    {
        super(message);
    }
}
