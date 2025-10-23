package com.vivek.backend.Management.exception;

public class UsernameNotFoundException extends RuntimeException{

    public UsernameNotFoundException(String message)
    {
        super(message);
    }
}
