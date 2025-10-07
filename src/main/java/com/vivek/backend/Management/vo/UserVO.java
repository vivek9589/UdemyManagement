package com.vivek.backend.Management.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


//  UserVO  ---> Use for computed or presentation-specific views.
// Why this matters: You can customize roleLabel (e.g., "Admin", "Student") for frontend display
@Data
@AllArgsConstructor
public class UserVO {

    private String firstName;
    private String email;
    private String roleLabel;





}



