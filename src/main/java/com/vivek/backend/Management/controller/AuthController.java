package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.LoginDto;
import com.vivek.backend.Management.dto.LoginResponseDto;
import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignupDto signupDto) {
        logger.info("Received signup request for email: {}", signupDto.getEmail());

        try {
            UserResponseDto response = authService.signUp(signupDto);
            logger.info("Signup successful for email: {}", signupDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Signup failed for email: {}", signupDto.getEmail(), e);
            throw e; // Let global exception handler format the response
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity<LoginResponseDto> logIn(
            @RequestBody LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response) {

        logger.info("Login attempt for email: {}", loginDto.getEmail());

        try {
            LoginResponseDto loginResponseDto = authService.logIn(loginDto);

            Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // Optional: restrict path
            response.addCookie(cookie);

            logger.info("Login successful for user ID: {}", loginResponseDto.getId());
            return ResponseEntity.ok(loginResponseDto);
        } catch (Exception e) {
            logger.error("Login failed for email: {}", loginDto.getEmail(), e);
            throw e;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request) {
        logger.info("Attempting to refresh access token");

        try {
            String refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> {
                        logger.warn("Refresh token not found in cookies");
                        return new AuthenticationServiceException("RefreshToken not found");
                    });

            LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
            logger.info("Access token refreshed for user");
            return ResponseEntity.ok(loginResponseDto);
        } catch (Exception e) {
            logger.error("Failed to refresh token", e);
            throw e;
        }
    }
}