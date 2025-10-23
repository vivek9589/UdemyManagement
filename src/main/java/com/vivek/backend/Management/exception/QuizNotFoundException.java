package com.vivek.backend.Management.exception;

public class QuizNotFoundException extends RuntimeException{

    public QuizNotFoundException(String message)
    {
        super(message);
    }
}
