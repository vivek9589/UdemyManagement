package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.*;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('QUIZ_CREATE')")
    public ResponseEntity<QuizResponseDto> createQuiz(@RequestBody Quiz quiz) {
        QuizResponseDto createdQuiz = quizService.createQuiz(quiz);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('QUIZ_UPDATE')")
    public ResponseEntity<QuizResponseDto> updateQuiz(@PathVariable Long id,
                                                      @RequestBody QuizRequestDto dto) {
        QuizResponseDto updatedQuiz = quizService.updateQuiz(id, dto);
        return ResponseEntity.ok(updatedQuiz);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('QUIZ_DELETE')")
    public ResponseEntity<String> deleteQuizById(@PathVariable Long id) {
        String result = quizService.deleteQuizById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('QUIZ_READ')")
    public ResponseEntity<QuizResponseDto> getQuizById(@PathVariable Long id) {
        QuizResponseDto quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('QUIZ_READ')")
    public ResponseEntity<List<QuizResponseDto>> getAllQuiz() {
        List<QuizResponseDto> quizzes = quizService.getAllQuiz();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/option/{id}")
    @PreAuthorize("hasAuthority('QUIZ_ATTEMPT')")
    public ResponseEntity<List<QuestionResponseDto>> getQuizWithOption(@PathVariable Long id) {
        List<QuestionResponseDto> questions = quizService.getQuizWithOption(id);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('QUIZ_ATTEMPT')")
    public ResponseEntity<QuizResultResponseDto> submitQuiz(@RequestBody QuizSubmissionDto dto) {
        QuizResultResponseDto result = quizService.evaluate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}