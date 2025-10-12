package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.LoginDto;
import com.vivek.backend.Management.dto.LoginResponseDto;
import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    private AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignupDto signupDto){
        return new ResponseEntity<>(
                authService.signUp(signupDto), HttpStatus.CREATED);
    }


  /*  @PostMapping("/logIn")
    public ResponseEntity<LoginResponseDto> logIn(
            @RequestBody LoginDto loginDto){

        LoginResponseDto token = authService.logIn(loginDto);
        return ResponseEntity.ok(token);
    }

*/


    @PostMapping("/logIn")
    public ResponseEntity<LoginResponseDto> logIn(
            @RequestBody LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response){


        //change these things
        LoginResponseDto loginResponseDto = authService.logIn(loginDto);

        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true); // Prevents client-side scripts from accessing the cookie
        response.addCookie(cookie); // Http only cookies can be passed from backend to frontend only


        return ResponseEntity.ok(loginResponseDto);
    }



    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request){

        String refreshToken = Arrays.stream(request.getCookies()) //getCookies() method returns a array of cookie
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow(()-> new AuthenticationServiceException("RefreshToken not found"));
        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }
}


