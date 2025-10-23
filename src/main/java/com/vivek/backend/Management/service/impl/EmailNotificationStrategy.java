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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;

import java.util.*;


@Service
public class EmailNotificationStrategy implements NotificationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationStrategy.class);

    @Autowired private JavaMailSender mailSender;
    @Autowired private SpringTemplateEngine templateEngine;
    @Autowired private UserService userService;
    @Autowired private EnrollmentService enrollmentService;

    @Override
    public void send(String subject, String templateName, Map<String, Object> baseModel) throws MessagingException {
        logger.info("Starting email notification using template: {}", templateName);

        List<Object> users;

        try {
            if ("PromotionalOfferTemplate".equals(templateName)) {
                users = new ArrayList<>(userService.getAllUser());
                logger.debug("Fetched {} users for promotional offer", users.size());
            } else if ("ReminderTemplate".equals(templateName) || "CourseUpdateTemplate".equals(templateName)) {
                users = new ArrayList<>(enrollmentService.getAllEnrollment());
                logger.debug("Fetched {} enrollments for reminder/update", users.size());
            } else {
                logger.warn("Unknown template name: {}", templateName);
                users = Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch recipients for template: {}", templateName, e);
            throw new MessagingException("Failed to fetch recipients: " + e.getMessage());
        }

        for (Object obj : users) {
            String email = null;
            String username = null;

            if (obj instanceof UserResponseDto user) {
                email = user.getEmail();
                username = user.getFullName();
            } else if (obj instanceof EnrollmentResponseDto enrollment) {
                email = enrollment.getEmail();
                username = enrollment.getUserName();
            }

            if (email == null || username == null) {
                logger.warn("Skipping recipient due to missing email or username");
                continue;
            }

            try {
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
                logger.info("Email sent to: {}", email);
            } catch (Exception e) {
                logger.error("Failed to send email to: {}", email, e);
            }
        }

        logger.info("Email notification process completed for template: {}", templateName);
    }
}