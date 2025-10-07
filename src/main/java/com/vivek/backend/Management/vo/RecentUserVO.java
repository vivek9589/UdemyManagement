package com.vivek.backend.Management.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecentUserVO {
    private String name;
    private String email;
    private LocalDateTime registeredAt;
}