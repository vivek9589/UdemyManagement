package com.vivek.backend.Management.service.impl;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.vivek.backend.Management.dto.EnrollmentResponseDto;
import com.vivek.backend.Management.dto.UserResponseDto;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.service.EnrollmentService;
import com.vivek.backend.Management.service.NotificationStrategy;
import com.vivek.backend.Management.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class EmailNotificationStrategy implements NotificationStrategy {

    @Autowired private JavaMailSender mailSender;
    @Autowired private SpringTemplateEngine templateEngine;

    @Autowired private UserService userService;
    @Autowired private EnrollmentService enrollmentService;

    @Override
    public void send(String subject, String templateName, Map<String, Object> baseModel) throws MessagingException {

        List<Object> users;

        if ("PromotionalOfferTemplate".equals(templateName)) {
            users = new ArrayList<>(userService.getAllUser()); // List<UserResponseDto>
        } else if ("ReminderTemplate".equals(templateName) || "CourseUpdateTemplate".equals(templateName)) {
            users = new ArrayList<>(enrollmentService.getAllEnrollment()); // List<EnrollmentResponseDto>
        } else {
            users = Collections.emptyList();
        }

        for (Object obj : users) {
            String email = null;
            String username = null;

            if (obj instanceof UserResponseDto) {
                UserResponseDto user = (UserResponseDto) obj;
                email = user.getEmail();
                username = user.getFullName();
            } else if (obj instanceof EnrollmentResponseDto) {
                EnrollmentResponseDto enrollment = (EnrollmentResponseDto) obj;
                email = enrollment.getEmail();
                username = enrollment.getUserName();
            }

            if (email != null && username != null) {
                Map<String, Object> model = new HashMap<>(baseModel);
                model.put("userName", username);

                Context context = new Context();
                context.setVariables(model);
                String html = templateEngine.process(templateName, context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(email);
                helper.setSubject(subject);
                helper.setText(html, true);
                mailSender.send(message);
            }
        }
    }
}