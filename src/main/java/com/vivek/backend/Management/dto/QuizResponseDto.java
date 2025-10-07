package com.vivek.backend.Management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponseDto {
    private Long quizId;
    private String title;
    private int noOfQuestions;
    private String totalTimeFormatted;
    private String createdBy;
    private int noOfAttempts;
    private int questionCount;
}

