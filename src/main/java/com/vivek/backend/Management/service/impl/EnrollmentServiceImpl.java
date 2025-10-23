package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dto.CourseResponseDto;
import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.dto.EnrollmentResponseDto;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.entity.Payment;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.exception.CourseNotFoundException;
import com.vivek.backend.Management.exception.EnrollmentAccessException;
import com.vivek.backend.Management.exception.EnrollmentNotFoundException;
import com.vivek.backend.Management.exception.UserNotFoundException;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.PaymentRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.service.EnrollmentService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseService courseService;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, PaymentRepository paymentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void createEnrollment(EnrollmentRequestDto dto) {
        logger.info("Creating enrollment for user ID: {} and course ID: {}", dto.getUserId(), dto.getCourseId());

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
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", dto.getUserId());
                    throw new UserNotFoundException("User not found with ID: " + dto.getUserId());
                });

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> {
                    logger.error("Course not found with ID: {}", dto.getCourseId());
                    throw new CourseNotFoundException("Course not found with ID: " + dto.getCourseId());
                });

        Payment payment = new Payment();
        payment.setAmount(dto.getPaymentAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setEnrollment(enrollment);

        enrollment.setPayment(payment);
        enrollment.setUser(user);
        enrollment.setCourse(course);

        enrollmentRepository.save(enrollment);
        logger.info("Enrollment created successfully for user ID: {}", dto.getUserId());
    }

    @Override
    public List<EnrollmentResponseDto> getAllEnrollment() {
        logger.info("Fetching all enrollments");
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        logger.debug("Total enrollments fetched: {}", enrollments.size());

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
        logger.info("Fetching enrollment with ID: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Enrollment not found with ID: {}", id);
                    throw new EnrollmentNotFoundException("Enrollment not found with ID: " + id);
                });

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
    public List<CourseResponseDto> getAccess(Long userId) {
        logger.info("Checking access for user ID: {}", userId);

        if (!enrollmentRepository.existsByUser_UserId(userId)) {
            logger.warn("User not enrolled: {}", userId);
            throw new EnrollmentAccessException("User is not enrolled");
        }

        Enrollment enrollment = enrollmentRepository.findByUser_UserId(userId);
        String plan = enrollment.getStatus().name();

        if ("TRIAL".equals(plan) && LocalDate.now().isAfter(enrollment.getExpiresAt())) {
            logger.warn("Trial expired for user ID: {}", userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Trial period has expired. Please purchase a plan.");
        }

        List<CourseResponseDto> courses = courseService.getAllCourse();
        logger.debug("Access granted to {} courses for user ID: {}", courses.size(), userId);
        return courses;
    }

    @Override
    public void cancelSubscription(Long enrollmentId) {
        logger.info("Cancelling subscription for enrollment ID: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> {
                    logger.error("Enrollment not found with ID: {}", enrollmentId);
                    throw new EnrollmentNotFoundException("Enrollment not found with ID: " + enrollmentId);
                });

        enrollmentRepository.delete(enrollment);
        logger.info("Subscription cancelled for enrollment ID: {}", enrollmentId);
    }
}