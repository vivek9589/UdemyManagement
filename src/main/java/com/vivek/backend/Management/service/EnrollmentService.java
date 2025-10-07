package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.dto.EnrollmentResponseDto;

import java.util.List;

public interface EnrollmentService {

    public void createEnrollment(EnrollmentRequestDto dto);

    public List<EnrollmentResponseDto> getAllEnrollment();

    public EnrollmentResponseDto getEnrollmentById(Long id);

    public List<CourseResponseDto> getAccess(Long user_id);


    // cancel subscription
    public void cancelSubscription(Long enrollmentId);









}
