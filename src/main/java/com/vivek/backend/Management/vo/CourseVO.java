

package com.vivek.backend.Management.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CourseVO {

    private Long courseId;
    private String courseTitle;
    private String instructorName;
    private int enrolledCount;
    private String category;
    private int duration; // e.g., "6 weeks", "3 hours"
}