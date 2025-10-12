package com.vivek.backend.Management.dto;

import lombok.*;


// - UserRequestDto: Only fields needed for creation/update (e.g., name, email, password).


/*
Used to send/receive data between layers (especially API ↔ Service)

Contains only required fields (e.g., hide password, show name/email)

Often used with @RequestBody, @ResponseBody
 */



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDto {


    // combine krke --> only name kr do
    private String firstName;
    private String lastName;
    //you can add validation here
    private String email;
    private String password;



}


