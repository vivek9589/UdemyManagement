package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.*;
import com.vivek.backend.Management.entity.Quiz;

import java.util.List;



public interface QuizService {
    QuizResponseDto createQuiz(Quiz quiz);
    QuizResponseDto updateQuiz(Long id, QuizRequestDto dto);
    QuizResponseDto getQuizById(Long id);
    List<QuizResponseDto> getAllQuiz();
    String deleteQuizById(Long id);
    List<QuestionResponseDto> getQuizWithOption(Long quizId);
    QuizResultResponseDto evaluate(QuizSubmissionDto submissionDto);
}


