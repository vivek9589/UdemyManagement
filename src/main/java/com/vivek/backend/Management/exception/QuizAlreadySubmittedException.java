package com.vivek.backend.Management.exception;

public class QuizAlreadySubmittedException extends RuntimeException {
    public QuizAlreadySubmittedException(String message) {
        super(message);
    }
}
