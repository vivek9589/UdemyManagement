package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.*;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.service.QuizService;
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
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('QUIZ_UPDATE')")
    public ResponseEntity<QuizResponseDto> updateQuiz(@PathVariable Long id,
                                                      @RequestBody QuizRequestDto dto) {
        return ResponseEntity.ok(quizService.updateQuiz(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('QUIZ_DELETE')")
    public ResponseEntity<String> deleteQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.deleteQuizById(id));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('QUIZ_READ')")
    public ResponseEntity<QuizResponseDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('QUIZ_READ')")
    public ResponseEntity<List<QuizResponseDto>> getAllQuiz() {
        return ResponseEntity.ok(quizService.getAllQuiz());
    }

    @GetMapping("/option/{id}")
    @PreAuthorize("hasAuthority('QUIZ_ATTEMPT')")
    public ResponseEntity<List<QuestionResponseDto>> getQuizWithOption(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizWithOption(id));
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('QUIZ_ATTEMPT')")
    public ResponseEntity<QuizResultResponseDto> submitQuiz(@RequestBody QuizSubmissionDto dto) {
        return ResponseEntity.ok(quizService.evaluate(dto));
    }
}



