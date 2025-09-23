package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.dto.EnrollmentRequestDto;
import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.entity.Enrollment;
import com.vivek.backend.Management.entity.Payment;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.enums.EnrollmentStatus;
import com.vivek.backend.Management.repository.CourseRepository;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.UserRepository;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    CourseService courseService;

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

    @Override
    public List<Enrollment> getAllEnrollment() {
         List<Enrollment> enrollments = enrollmentRepository.findAll();
         return enrollments;
    }

    @Override
    public List<Course> getAccess(Long user_id) {



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
        List<Course> courses = courseService.getAllCourse();
        return courses;




    }

}
