package com.vivek.backend.Management.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmissionDto {
    private Long quizId;
    private Long userId;
    private List<AnswerDto> answers;

}

