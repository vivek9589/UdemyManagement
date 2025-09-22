package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.entity.Payment;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.EnrollmentService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Enrollment mapToEntity(EnrollmentRequestDto dto, UserRepository userRepo, CourseRepository courseRepo) {

        Enrollment enrollment = new Enrollment();

        // enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(dto.getStatus());


        String status = dto.getStatus().name();

        switch (status)
        {
            case "MONTHLY":
                enrollment.setExpiresAt(LocalDate.now().plusDays(30));
                break;

            case "HALFYEARLY":
                enrollment.setExpiresAt(LocalDate.now().plusDays(180));
                break;

            case "YEARLY":
                enrollment.setExpiresAt(LocalDate.now().plusDays(360));
                break;

            default:
                enrollment.setExpiresAt(LocalDate.now().plusDays(7));

        }






        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not Found with this ID"));
        Course course = courseRepo.findById(dto.getCourseId()).orElseThrow(()-> new RuntimeException("Course not Found with this ID"));

        Payment payment = new Payment();
        payment.setAmount(dto.getPaymentAmount());
        // payment.setPaymentDate(dto.getPaymentDate());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(dto.getPaymentMethod());

        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setPayment(payment);

        enrollmentRepository.save(enrollment);


        return enrollment;
    }

}
