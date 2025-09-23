package com.vivek.backend.Management.controller;

import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
EnrollmentController
    POST /enrollments → enroll user in course  -> create enrollment

    GET /enrollments/{userId} → list user's enrollments

    GET /enrollments/access/{courseId} → check if current user has access
 */



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

    // POST /enrollments → enroll user in course


    @PostMapping
    public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentRequestDto dto) {
         enrollmentService.mapToEntity(dto, userRepo, courseRepo);

        return ResponseEntity.ok("Enrollment created successfully");
    }


    //  GET /enrollments/{userId} → list user's enrollments

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments()
    {
       List<Enrollment> enrollments=  enrollmentService.getAllEnrollment();

       return new ResponseEntity<>(enrollments,HttpStatus.OK);

    }

    // only enrolled user get access to course

    //    GET /enrollments/access/{courseId} → check if current user has access

    @GetMapping("/access/{user_id}")
    public ResponseEntity<List<Course>> getAccess(@PathVariable Long user_id)
    {
        List<Course> courses =  enrollmentService.getAccess(user_id);

        return new ResponseEntity<>(courses,HttpStatus.OK);



    }



}
