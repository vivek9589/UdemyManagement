package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.service.NotificationService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired private EmailNotificationStrategy emailChannel;
    @Autowired private SMSNotificationStrategy smsChannel;
    @Autowired private PushNotificationStrategy pushChannel;

    @Override
    public void dispatch(String channel, String subject, String template, Map<String, Object> model) throws MessagingException {
        logger.info("Dispatching notification | Channel: {} | Subject: {} | Template: {}", channel, subject, template);

        try {
            switch (channel.toUpperCase()) {
                case "EMAIL" -> {
                    logger.debug("Using Email channel");
                    emailChannel.send(subject, template, model);
                }
                case "SMS" -> {
                    logger.debug("Using SMS channel");
                    smsChannel.send(subject, template, model);
                }
                case "PUSH" -> {
                    logger.debug("Using Push channel");
                    pushChannel.send(subject, template, model);
                }
                default -> {
                    logger.warn("Unknown notification channel: {}", channel);
                    throw new IllegalArgumentException("Unknown channel: " + channel);
                }
            }
            logger.info("Notification dispatched successfully via {}", channel);
        } catch (MessagingException e) {
            logger.error("MessagingException while sending {} notification", channel, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during notification dispatch", e);
            throw new RuntimeException("Failed to dispatch notification: " + e.getMessage());
        }
    }
}
