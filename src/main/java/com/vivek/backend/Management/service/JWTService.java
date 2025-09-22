package com.vivek.backend.Management.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {


    public String generateToken(String email);

    String extractUserName(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
