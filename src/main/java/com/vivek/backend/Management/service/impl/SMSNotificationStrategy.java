package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.service.NotificationStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("SMS")
public class SMSNotificationStrategy implements NotificationStrategy {


    @Override
    public void send(String subject, String templateName, Map<String, Object> model) {

    }
}