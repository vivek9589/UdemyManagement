package com.vivek.backend.Management.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResultResponseDto {

    // private Long id;
    private Long quizId;
    private Long userId;
    private int totalQuestions;
    private int attemptedQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private double percentage;
    private String remark;



}
