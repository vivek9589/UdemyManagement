package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.UserRepository;

import java.util.List;

public interface EnrollmentService {

    public Enrollment mapToEntity(EnrollmentRequestDto dto, UserRepository userRepo, CourseRepository courseRepo);

    public List<Enrollment> getAllEnrollment();

    public List<Course> getAccess(Long user_id);









}
