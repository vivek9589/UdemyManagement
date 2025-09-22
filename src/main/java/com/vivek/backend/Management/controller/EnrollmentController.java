package com.vivek.backend.Management.controller;

import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {


    private final EnrollmentService enrollmentService;

    EnrollmentController(EnrollmentService enrollmentService)
    {
        this.enrollmentService = enrollmentService;
    }



    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;


    @PostMapping
    public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentRequestDto dto) {
         enrollmentService.mapToEntity(dto, userRepo, courseRepo);

        return ResponseEntity.ok("Enrollment created successfully");
    }
}
