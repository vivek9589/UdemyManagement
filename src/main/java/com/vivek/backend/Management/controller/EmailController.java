package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.service.impl.EmailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email/send")
public class EmailController {

    @Autowired
    private EmailNotification emailNotification;

    // Endpoint to send a test email
    @PostMapping("/test")
    public String sendTestEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        emailNotification.sendEmail(to, subject, body);
        return "Email sent successfully";
    }
}
