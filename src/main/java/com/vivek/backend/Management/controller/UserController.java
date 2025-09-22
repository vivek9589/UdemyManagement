package com.vivek.backend.Management.controller;



import com.vivek.backend.Management.dto.UserRequestDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    public UserResponseDto getUserById(@PathVariable Long id)
    {
        return userService.getUserById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    public List<UserResponseDto> getAllUser()
    {
        return userService.getAllUser();
    }


        // only admin can create user
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('UDEMY_WRITE')")
    public User createUser(@RequestBody User user)
    {

        return userService.createUser(user);
    }

    // user registration by self (it does not require any permission)

    @PostMapping("/dto/register")
    public UserResponseDto registerUser(@RequestBody UserRequestDto user)
    {

        return userService.registerUser(user);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UDEMY_DELETE')")
    public Long deleteUserById(@PathVariable Long id)
    {
        //User user = userService.getUserById(id);
            userService.deleteUserById(id);

        return id;
    }






    //api for for update user



    //login

    @PostMapping("/login")
    public String login(@RequestBody User user)
    {

        return userService.verify(user);

    }







}
