package com.vivek.backend.Management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponseDto {


    private String userName;
    private String email;
    private Long enrollmentId;
    private LocalDate enrollmentDate;
    private String CourseName;
    private LocalDate expiresAt;
    private String status;  // status (TRIAL,   MONTHLY ,HALFYEARLY,  YEARLY)





}
