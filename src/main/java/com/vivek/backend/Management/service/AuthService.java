package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.LoginDto;
import com.vivek.backend.Management.dto.LoginResponseDto;
import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;

public interface AuthService {

    public UserResponseDto signUp(SignupDto signupDto);
    public LoginResponseDto logIn(LoginDto loginDto);
    public LoginResponseDto refreshToken(String refreshToken);


}
