package com.vivek.backend.Management.service;


import jakarta.mail.MessagingException;

import java.util.Map;

public interface NotificationStrategy {

    void send( String subject, String templateName, Map<String, Object> model) throws MessagingException;
}

