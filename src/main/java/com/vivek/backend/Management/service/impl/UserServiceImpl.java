package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dao.UserDao;
import com.vivek.backend.Management.dto.UserRequestDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.service.UserService;
import com.vivek.backend.Management.vo.RecentUserVO;
import com.vivek.backend.Management.vo.UserVO;
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
    UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    // used for custom queries
    @Autowired
    private UserDao userDao;


    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;



    public User createUser(User user)
    {

        return userRepository.save(user);
    }




    public UserResponseDto registerUser(UserRequestDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(savedUser.getUserId())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
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
                        .role(user.getRole())
                        .build())
                .toList();
    }

    public List<UserVO> getUserDashboardView() {
        return userDao.getAllUsers();
    }




    public String deleteUserById(Long id)
    {
        userRepository.deleteById(id);
        return "Successfully deleted user with id: " + id;

    }




    public String verify(String email, String password)
    {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(email);

        return "fail";
    }

    @Override
    public List<RecentUserVO> getRecentUsers() {
        return userDao.getUsersRegisteredInLast7Days();
    }


}
