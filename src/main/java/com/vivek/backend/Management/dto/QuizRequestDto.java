package com.vivek.backend.Management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRequestDto {
    private String title;
    private int noOfQuestions;
    private LocalTime totalTime;
    private String createdBy;
    private int noOfAttempts;
}