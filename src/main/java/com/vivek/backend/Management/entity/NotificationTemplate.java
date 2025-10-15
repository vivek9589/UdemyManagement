package com.vivek.backend.Management.entity;

import com.vivek.backend.Management.enums.NotificationChannel;
import com.vivek.backend.Management.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {


    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String subject;

    @Lob
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;



}