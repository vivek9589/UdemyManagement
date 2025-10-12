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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    /*
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    */
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JWTService jwtService;  //from your custom class

    public UserResponseDto signUp(SignupDto signupDto) {

        // Check if a userr already exists with the provided email

        Optional<User> user = Optional.ofNullable(userRepository
                .findByEmail(signupDto.getEmail()));

        if(user.isPresent()) throw new BadCredentialsException("Cannot signup, User already exists with email "+signupDto.getEmail());

        // Map SignupDto to UserEntity and encode the password
/*
        User mappedUser = modelMapper.map(signupDto,User.class);
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        // Save the userr entity to the database
        User savedUser = userRepository.save(mappedUser);*/
        User userr = User.builder()
                .firstName(signupDto.getFirstName())
                .lastName(signupDto.getLastName())
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .build();


        User savedUser = userRepository.save(userr);

        return UserResponseDto.builder()
                .id(savedUser.getUserId())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }


    public LoginResponseDto logIn(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

       //  User user = (User) authentication.getPrincipal();
        User user = userRepository.findByEmail(loginDto.getEmail());


        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken(user.getEmail());

        sessionService.generateNewSession(user,refreshToken);

        // return new LoginResponseDto(user.getId(),accessToken,refreshToken);
        return LoginResponseDto.builder()
                .id(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();


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

        String email = jwtService.extractUserName(refreshToken);

        String accessToken = jwtService.createAccessToken(email);

        return new LoginResponseDto((long)1,accessToken,refreshToken);

    }


}
