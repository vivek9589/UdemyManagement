package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {


    public String generateToken(String email);

    String extractUserName(String token);

    boolean validateToken(String token, UserDetails userDetails);

    public String extractFirstName(String token);
    public String extractLastName(String token);

    public Long generateUserIdFromToken(String token);
    public String createAccessToken(String email);
    public String createRefreshToken(String email);

}
