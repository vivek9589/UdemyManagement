package com.vivek.backend.Management.service;

import jakarta.mail.MessagingException;

import java.util.Map;


public interface NotificationService {

    public void dispatch(String channel, String subject, String template, Map<String, Object> model) throws MessagingException;
}
