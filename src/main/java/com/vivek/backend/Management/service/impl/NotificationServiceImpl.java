package com.vivek.backend.Management.service.impl;


import com.vivek.backend.Management.service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired private EmailNotificationStrategy emailChannel;
    @Autowired private SMSNotificationStrategy smsChannel;
    @Autowired private PushNotificationStrategy pushChannel;

    @Override
    public void dispatch(String channel, String subject, String template, Map<String, Object> model) throws MessagingException {


        switch (channel.toUpperCase()) {
            case "EMAIL" -> emailChannel.send(subject, template, model);
            case "SMS" -> smsChannel.send(subject, template, model);
            case "PUSH" -> pushChannel.send(subject, template, model);
            default -> throw new IllegalArgumentException("Unknown channel: " + channel);
        }
    }



}
