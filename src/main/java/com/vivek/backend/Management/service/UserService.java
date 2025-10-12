package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.vo.RecentUserVO;
import com.vivek.backend.Management.vo.UserVO;


import java.util.List;





public interface UserService {

    // crud operation for User

    public User createUser(User user);

    //register signupDto
    public UserResponseDto registerUser(SignupDto signupDto);


    // public List<User> getAllUser();

    public List<UserResponseDto> getAllUser();

    public List<UserVO> getUserDashboardView();

    public UserResponseDto getUserById(Long id);

    public String deleteUserById(Long id);


    String verify(String email,String password);


    public List<RecentUserVO> getRecentUsers();



}



