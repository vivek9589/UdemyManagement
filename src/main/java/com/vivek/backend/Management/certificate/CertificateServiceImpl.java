package com.vivek.backend.Management.certificate;


import com.vivek.backend.Management.entity.Course;
import com.vivek.backend.Management.service.CourseService;
import com.vivek.backend.Management.service.JWTService;
import com.vivek.backend.Management.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;


@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JWTService jwtService;




    public String generateHtml(String token, Long id) {

        CourseVO courseDetails = courseService.getCourseById(id);


        Context context = new Context();
        context.setVariable("name", jwtService.extractFirstName(token) + " " + jwtService.extractLastName(token));
        context.setVariable("course", courseDetails.getCourseTitle());



        Certificate certificate = new Certificate();
        certificate.setUserName(jwtService.extractUserName(token));
        certificate.setCourseName(courseDetails.getCourseTitle());
        certificate.setIssueDate(LocalDate.now());

        certificateRepository.save(certificate);

        return templateEngine.process("Certificate", context);


    }


}
