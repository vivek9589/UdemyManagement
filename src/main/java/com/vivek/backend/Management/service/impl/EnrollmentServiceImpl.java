package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.dto.EnrollmentResponseDto;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.entity.Payment;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.PaymentRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.service.EnrollmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    CourseService courseService;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, PaymentRepository paymentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
    }

    // Java
    public void createEnrollment(
            EnrollmentRequestDto dto) {

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(dto.getStatus());

        switch (dto.getStatus()) {
            case MONTHLY -> enrollment.setExpiresAt(LocalDate.now().plusDays(30));
            case HALFYEARLY -> enrollment.setExpiresAt(LocalDate.now().plusDays(180));
            case YEARLY -> enrollment.setExpiresAt(LocalDate.now().plusDays(360));
            default -> enrollment.setExpiresAt(LocalDate.now().plusDays(7));
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not Found with this ID"));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not Found with this ID"));

        Payment payment = new Payment();
        payment.setAmount(dto.getPaymentAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(dto.getPaymentMethod());

        payment.setEnrollment(enrollment);
        enrollment.setPayment(payment);

        enrollment.setUser(user);
        enrollment.setCourse(course);

        // Save enrollment (will cascade to payment if configured)
        enrollmentRepository.save(enrollment);

    }



    // only admin can see all enrollments
    @Override
    public List<EnrollmentResponseDto> getAllEnrollment() {
         List<Enrollment> enrollments = enrollmentRepository.findAll();

            return enrollments.stream().map(enrollment -> EnrollmentResponseDto.builder()
                    .enrollmentId(enrollment.getEnrollmentId())
                    .userName(enrollment.getUser().getFirstName())
                    .email(enrollment.getUser().getEmail())
                    .CourseName(enrollment.getCourse().getCourseName())
                    .enrollmentDate(enrollment.getEnrollmentDate())
                    .expiresAt(enrollment.getExpiresAt())
                    .status(enrollment.getStatus().name())
                    .build()).toList();
    }

    @Override
    public EnrollmentResponseDto getEnrollmentById(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Enrollment not found with this ID"+id));
        return EnrollmentResponseDto.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .userName(enrollment.getUser().getFirstName())
                .email(enrollment.getUser().getEmail())
                .CourseName(enrollment.getCourse().getCourseName())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .expiresAt(enrollment.getExpiresAt())
                .status(enrollment.getStatus().name())
                .build();
    }


    @Override
    public List<CourseResponseDto> getAccess(Long user_id) {

        if(!enrollmentRepository.existsByUser_UserId(user_id))
        {
            throw new RuntimeException("User is not enrolled");

        }


        Enrollment enrollment = enrollmentRepository.findByUser_UserId(user_id);
        String plan = enrollment.getStatus().name();

        if(plan.equals("TRIAL")  && LocalDate.now().isAfter(enrollment.getExpiresAt()))
        {
            // Trial has expired
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Trial period has expired. Please purchase a plan.");
            // System.out.println("Trial period has expired. Please purchase a plan.");
        }
        List<CourseResponseDto> courses = courseService.getAllCourse();
        return courses;


    }


    @Override
    public void cancelSubscription(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with this ID: " + enrollmentId));

        enrollmentRepository.delete(enrollment);
    }



}
