package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dao.UserDao;
import com.vivek.backend.Management.dto.SignupDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.exception.InvalidCredentialsException;
import com.vivek.backend.Management.exception.NoRecentUsersExceptions;
import com.vivek.backend.Management.exception.UserNotFoundException;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.service.UserService;
import com.vivek.backend.Management.vo.RecentUserVO;
import com.vivek.backend.Management.vo.UserVO;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDao userDao;
    private final JWTService jwtService;
    private final AuthenticationManager authManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDao userDao,
                           JWTService jwtService, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    // This gives you access to logging methods like info(), debug(), warn(), and error().


    @Override
    public User createUser(User user)
    {
        logger.info("Creating User with email: {}",user.getEmail());
         User savedUser = userRepository.save(user);
         logger.debug("User created with ID: {}", savedUser.getUserId());

         return savedUser;

    }



     @Override
    public UserResponseDto registerUser(SignupDto signupDto) {

        logger.info("Registering new user: {}",signupDto.getEmail());
        User user = User.builder()
                .firstName(signupDto.getFirstName())
                .lastName(signupDto.getLastName())
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .build();

        User savedUser = userRepository.save(user);


        logger.debug("User Registered successfully with ID: {}",savedUser.getUserId());
        return UserResponseDto.builder()
                .id(savedUser.getUserId())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }



    @Override
    public UserResponseDto getUserById(Long id)
    {
        logger.info("Fetching user with ID: {}",id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID {}",id);
                    return new UserNotFoundException("User not found with ID: {} " + id);
                });


        logger.debug("User Found: {}",user.getEmail());
        return UserResponseDto.builder()
                .id(user.getUserId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .build();

        //return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserResponseDto> getAllUser() {

        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();


        logger.debug("Total users fetched : {}",users.size());
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



    @Override
    public String deleteUserById(Long id) {
        logger.info("Deleting user with ID :{}", id);

        boolean exists = userRepository.existsById(id);

        if (!exists) {
            logger.warn("User with ID {} not found", id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
        logger.info("User Deleted successfully");

        return "Successfully deleted user with id: " + id;
    }


    @Override
    public String verify(String email, String password)
    {

        logger.info("Verifying credentials for email: {}",email);
        try{
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            if(authentication.isAuthenticated())
            {
                logger.info("LOGIN SUCCESS | email={} | timestamp={}", email, LocalDateTime.now());
                return jwtService.generateToken(email);
            }

        }catch (Exception e){
            logger.warn("LOGIN FAILURE | email={} | timestamp={} | reason={}", email, LocalDateTime.now(), e.getMessage());
            throw new InvalidCredentialsException("Invalid email or password");
        }


        return "fail";
    }

    @Override
    public List<RecentUserVO> getRecentUsers() {

        logger.info("Start Fetching users, which enrolled in last 7 days");
        List<RecentUserVO> recentUsers= userDao.getUsersRegisteredInLast7Days();

        if (recentUsers.isEmpty()) {
            logger.warn("No users registered in the last 7 days");
            throw new NoRecentUsersExceptions("No users registered in the last 7 days");
        }

        logger.info("Fetched Users , those enrolled in last 7 days");
        return recentUsers;
    }


}
