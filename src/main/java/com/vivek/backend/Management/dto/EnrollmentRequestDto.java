package com.vivek.backend.Management.dto;

import com.vivek.backend.Management.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequestDto {

    private EnrollmentStatus status;  // TRIAL,   MONTHLY ,HALFYEARLY,  YEARLY

    private Long userId;
    private Long courseId;

    private float paymentAmount;
    private String paymentMethod;



}