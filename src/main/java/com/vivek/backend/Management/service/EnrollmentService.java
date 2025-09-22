package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.UserRepository;

public interface EnrollmentService {

    public Enrollment mapToEntity(EnrollmentRequestDto dto, UserRepository userRepo, CourseRepository courseRepo);



}
