package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ADMIN")
public class AdminController {

    @Autowired
    private UserService userService;


    /*

    @GetMapping("/all-users")
   public List<User> getAllUsers()
   {
       List<User> all = userService.getAllUser();

       if(all != null && !all.isEmpty())
       {
            return all;
       }

       return null;  // for now we return null , if no user exists
   }

   //delete user api
   @DeleteMapping("/remove-user/{id}")
   public Long deleteUserById(@PathVariable Long id)
   {
       User user = userService.getUserById(id);
       userService.deleteUserById(id);
       return id;
   }


   // update user api



     */


}
