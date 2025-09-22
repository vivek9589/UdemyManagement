package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.UserRequestDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;


import java.util.List;





public interface UserService {

    // crud operation for User




    public User createUser(User user);

    //register user
    public UserResponseDto registerUser(UserRequestDto user);



    // public List<User> getAllUser();

    public List<UserResponseDto> getAllUser();

    public UserResponseDto getUserById(Long id);

    public Long deleteUserById(Long id);


    String verify(User user);






}



