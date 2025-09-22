package com.vivek.backend.Management.dto;

import com.vivek.backend.Management.enums.Role;
import lombok.*;

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
public class UserRequestDto {


    private String firstName;
    private String lastName;
    private String email;
    private int age;


    // private Role role;   // optional


}


