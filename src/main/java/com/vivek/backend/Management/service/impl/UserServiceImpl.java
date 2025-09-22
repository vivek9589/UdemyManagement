package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dto.UserRequestDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }





    public User createUser(User user)
    {
        return userRepository.save(user);
    }




    public UserResponseDto registerUser(UserRequestDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .age(dto.getAge())
                //.role(Role.valueOf(dto.getRole().toUpperCase()))
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(savedUser.getUserId())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .email(savedUser.getEmail())
                .age(savedUser.getAge())
                //.role(savedUser.getRole().name())
                .build();
    }




    public UserResponseDto getUserById(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        return UserResponseDto.builder()
                .id(user.getUserId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .build();

        //return userRepository.findById(id).orElse(null);
    }


    public List<UserResponseDto> getAllUser() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getUserId())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .email(user.getEmail())
                        .age(user.getAge())
                        .build())
                .toList();
    }





    public Long deleteUserById(Long id)
    {
        userRepository.deleteById(id);
        return id;

    }

    /*
    public AuthResponseDto verify(LoginRequestDto loginDto) {
        return null;
    }

     */


    public String verify(User user)
    {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getEmail());

        return "fail";
    }




}
