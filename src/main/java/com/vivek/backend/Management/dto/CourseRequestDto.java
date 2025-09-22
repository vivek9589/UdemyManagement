package com.vivek.backend.Management.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequestDto {

    private String courseName;
    private String description;
    private int duration;       // in days
    private double fees;
    private Long categoryId; // Add this
    private Long facultyId;


       // foreign key reference to Faculty
}
