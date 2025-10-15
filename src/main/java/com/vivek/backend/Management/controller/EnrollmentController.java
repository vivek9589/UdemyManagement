package com.vivek.backend.Management.controller;

import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.dto.EnrollmentResponseDto;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
EnrollmentController
    POST /enrollments → enroll user in course  -> create enrollment / subscribe plan

    GET /enrollments/{userId} → list user's enrollments

    GET /enrollments/access/{courseId} → check if current user has access
 */

 // (start subscription , cancel subscription ,get current plan/status)

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {


    private final EnrollmentService enrollmentService;


    EnrollmentController(EnrollmentService enrollmentService)
    {
        this.enrollmentService = enrollmentService;
    }



    // POST /enrollments → enroll user in course



    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ENROLL_USER')")
    public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentRequestDto dto) {
         enrollmentService.createEnrollment(dto);

        return ResponseEntity.ok("Enrollment created successfully");
    }




    // admin access for all enrollments    // user can see only their enrollment
    // Admin access for all enrollments
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('VIEW_ENROLLMENTS')")
    public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollments()
    {
       List<EnrollmentResponseDto> enrollments=  enrollmentService.getAllEnrollment();

       return new ResponseEntity<>(enrollments,HttpStatus.OK);

    }

    // get current plan/status

    // Admin access for all enrollments
    @GetMapping("/all/{id}")
    @PreAuthorize("hasAuthority('VIEW_ENROLLMENTS')")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id)
    {
        EnrollmentResponseDto enrollment = enrollmentService.getEnrollmentById(id);

        return new ResponseEntity<>(enrollment,HttpStatus.OK);
    }

    // only enrolled user get access to course

    //    GET /enrollments/access/{courseId} → check if current user has access

    @GetMapping("/access/{user_id}")
    @PreAuthorize("hasAuthority('VIEW_ENROLLMENTS')")
    public ResponseEntity<List<CourseResponseDto>> getAccess(@PathVariable Long user_id)
    {
        List<CourseResponseDto> courses =  enrollmentService.getAccess(user_id);

        return new ResponseEntity<>(courses,HttpStatus.OK);
    }





    // cancel subscription
    @DeleteMapping("/cancel/{enrollmentId}")
    @PreAuthorize("hasAuthority('CANCEL_ENROLLMENT')")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long enrollmentId)
    {
        enrollmentService.cancelSubscription(enrollmentId);

        return ResponseEntity.ok("Subscription cancelled successfully");
    }




}
