package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.QuestionRequestDto;
import com.vivek.backend.Management.dto.QuestionResponseDto;
import com.vivek.backend.Management.entity.Question;
import com.vivek.backend.Management.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Create a question
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('QUESTION_CREATE')")
    public ResponseEntity<QuestionResponseDto> createQuestion(@RequestBody QuestionRequestDto dto) {
        QuestionResponseDto createdQuestion = questionService.createQuestion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion); // 201 Created
    }

    // Get all questions of a quiz
    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasAuthority('QUESTION_READ')")
    public ResponseEntity<List<Question>> getAllQuestionOfQuiz(@PathVariable Long quizId) {
        List<Question> questions = questionService.getAllQuestionOfQuiz(quizId);
        return ResponseEntity.ok(questions); // 200 OK
    }

    // Delete question by ID
    @DeleteMapping("/delete/{questionId}")
    @PreAuthorize("hasAuthority('QUESTION_DELETE')")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
        String result = questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(result); // 200 OK
    }

    // Get all questions with options of a quiz
    @GetMapping("/options/{quizId}")
    @PreAuthorize("hasAuthority('QUESTION_READ')")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestionsWithOptionsOfQuiz(@PathVariable Long quizId) {
        List<QuestionResponseDto> questionsWithOptions = questionService.getAllQuestionsWithOptionsOfQuiz(quizId);
        return ResponseEntity.ok(questionsWithOptions); // 200 OK
    }
}