package com.vivek.backend.Management.dto;


import com.vivek.backend.Management.enums.Role;
import lombok.*;



// - UserResponseDto: Only fields needed for response (e.g., id, name, email, role).
// Prevents overexposing sensitive fields like passwords
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private Role role;

}
