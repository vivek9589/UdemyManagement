package com.vivek.backend.Management.dto;


import com.vivek.backend.Management.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private int age;

    // private Role role;   // optional
}
