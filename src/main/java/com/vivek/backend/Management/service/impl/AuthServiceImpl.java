package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.LoginDto;
import com.vivek.backend.Management.dto.LoginResponseDto;
import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.AuthService;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.service.SessionService;
import com.vivek.backend.Management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired private  UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private SessionService sessionService;
    @Autowired private  AuthenticationManager authenticationManager;
    @Autowired private  JWTService jwtService;  //from your custom class

    public UserResponseDto signUp(SignupDto signupDto) {
        logger.info("Attempting signup for email: {}", signupDto.getEmail());

        try {
            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(signupDto.getEmail()));
            if (user.isPresent()) {
                logger.warn("Signup failed: user already exists with email {}", signupDto.getEmail());
                throw new BadCredentialsException("Cannot signup, User already exists with email " + signupDto.getEmail());
            }

            /*
            User mappedUser = modelMapper.map(signupDto, User.class);
            mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
            User savedUser = userRepository.save(mappedUser);
            */

            User userr = User.builder()
                    .firstName(signupDto.getFirstName())
                    .lastName(signupDto.getLastName())
                    .email(signupDto.getEmail())
                    .password(signupDto.getPassword())
                    .build();

            User savedUser = userRepository.save(userr);
            logger.info("User signed up successfully with ID: {}", savedUser.getUserId());

            return UserResponseDto.builder()
                    .id(savedUser.getUserId())
                    .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole())
                    .build();

        } catch (Exception e) {
            logger.error("Signup failed for email: {}", signupDto.getEmail(), e);
            throw new RuntimeException("Signup failed: " + e.getMessage());
        }
    }




    public LoginResponseDto logIn(LoginDto loginDto) {
        logger.info("Attempting login for email: {}", loginDto.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            User user = userRepository.findByEmail(loginDto.getEmail());
            if (user == null) {
                logger.warn("Login failed: user not found with email {}", loginDto.getEmail());
                throw new BadCredentialsException("Invalid credentials");
            }

            String accessToken = jwtService.createAccessToken(user.getEmail());
            String refreshToken = jwtService.createRefreshToken(user.getEmail());

            sessionService.generateNewSession(user, refreshToken);
            logger.info("Login successful for user ID: {}", user.getUserId());

            return LoginResponseDto.builder()
                    .id(user.getUserId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for email: {}", loginDto.getEmail());
            throw e;
        } catch (Exception e) {
            logger.error("Login error for email: {}", loginDto.getEmail(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

/*

    public String logIn(LoginDto loginDto) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );


       // User user = (User) authentication.getPrincipal(); //userentity used from you custom entity class

        if(authentication.isAuthenticated())
            return jwtService.generateToken(loginDto.getEmail());

        // String token = jwtService.generateToken(user.getEmail());

        return "Cannot generate token" ;

    }
*/


    public LoginResponseDto refreshToken(String refreshToken) {
       // Long userId = jwtService.generateUserIdFromToken(refreshToken);  //refresh token is valid
        //UserResponseDto user = userService.getUserById(userId);


        // use hardcoded value for user id
        // because we are not storing refresh token in db or in memory
        // so we cannot verify whether the refresh token is valid or not
        // in real world application we have to store the refresh token in db or in memory
        // and then we can verify whether the refresh token is valid or not
        // if valid then we can generate new access token
        // otherwise we have to ask user to login again

        logger.info("Refreshing access token using refresh token");

        try {
            String email = jwtService.extractUserName(refreshToken);
            String accessToken = jwtService.createAccessToken(email);

            logger.info("Access token refreshed for email: {}", email);
            return new LoginResponseDto((long) 1, accessToken, refreshToken);

        } catch (Exception e) {
            logger.error("Failed to refresh token", e);
            throw new RuntimeException("Refresh token failed: " + e.getMessage());
        }
    }



}



