package com.vivek.backend.Management.controller;

import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.dto.EnrollmentResponseDto;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

     private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

     private final EnrollmentService enrollmentService;

     EnrollmentController(EnrollmentService enrollmentService) {
         this.enrollmentService = enrollmentService;
     }

     // POST /enrollments → enroll user in course
     @PostMapping("/create")
     @PreAuthorize("hasAuthority('ENROLL_USER')")
     public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentRequestDto dto) {
         logger.info("Received enrollment request for user ID: {} and course ID: {}", dto.getUserId(), dto.getCourseId());
         enrollmentService.createEnrollment(dto);
         logger.info("Enrollment created successfully for user ID: {}", dto.getUserId());
         return ResponseEntity.ok("Enrollment created successfully");
     }

     // admin access for all enrollments    // user can see only their enrollment
     // Admin access for all enrollments
     @GetMapping("/all")
     @PreAuthorize("hasAuthority('VIEW_ENROLLMENTS')")
     public ResponseEntity<List<EnrollmentResponseDto>> getAllEnrollments() {
         logger.info("Fetching all enrollments");
         List<EnrollmentResponseDto> enrollments = enrollmentService.getAllEnrollment();
         logger.debug("Total enrollments fetched: {}", enrollments.size());
         return new ResponseEntity<>(enrollments, HttpStatus.OK);
     }

     // get current plan/status
     // Admin access for all enrollments
     @GetMapping("/all/{id}")
     @PreAuthorize("hasAuthority('VIEW_ENROLLMENTS')")
     public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id) {
         logger.info("Fetching enrollment details for ID: {}", id);
         EnrollmentResponseDto enrollment = enrollmentService.getEnrollmentById(id);
         return new ResponseEntity<>(enrollment, HttpStatus.OK);
     }

     // only enrolled user get access to course
     // GET /enrollments/access/{courseId} → check if current user has access
     @GetMapping("/access/{user_id}")
     @PreAuthorize("hasAuthority('VIEW_ENROLLMENTS')")
     public ResponseEntity<List<CourseResponseDto>> getAccess(@PathVariable Long user_id) {
         logger.info("Checking course access for user ID: {}", user_id);
         List<CourseResponseDto> courses = enrollmentService.getAccess(user_id);
         logger.debug("User ID: {} has access to {} courses", user_id, courses.size());
         return new ResponseEntity<>(courses, HttpStatus.OK);
     }

     // cancel subscription
     @DeleteMapping("/cancel/{enrollmentId}")
     @PreAuthorize("hasAuthority('CANCEL_ENROLLMENT')")
     public ResponseEntity<String> cancelSubscription(@PathVariable Long enrollmentId) {
         logger.info("Cancelling subscription for enrollment ID: {}", enrollmentId);
         enrollmentService.cancelSubscription(enrollmentId);
         logger.info("Subscription cancelled for enrollment ID: {}", enrollmentId);
         return ResponseEntity.ok("Subscription cancelled successfully");
     }
 }