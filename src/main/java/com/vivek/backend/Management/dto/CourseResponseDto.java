package com.vivek.backend.Management.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDto {

    private Long courseId;
    private String courseName;
    private String description;
    private int duration;
    private double fees;
    private String publicId;
    private String url;

    private String createdBy;    // optional: derived from User entity
}