package com.vivek.backend.Management.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class UserVO {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
    private final int age;
    private final String address;
    private final String role;


}
