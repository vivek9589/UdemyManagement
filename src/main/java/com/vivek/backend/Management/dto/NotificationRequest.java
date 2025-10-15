package com.vivek.backend.Management.dto;

import lombok.Data;

import java.util.Map;



@Data
public class NotificationRequest {
    private String channel;       // EMAIL, SMS, PUSH
    private String subject;
    private String template;      // template name (optional if hardcoded)
    private Map<String, Object> model;

    // Getters and setters
}