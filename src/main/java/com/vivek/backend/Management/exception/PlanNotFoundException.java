package com.vivek.backend.Management.exception;

public class PlanNotFoundException extends RuntimeException{

    public PlanNotFoundException(String message)
    {
        super(message);
    }
}
