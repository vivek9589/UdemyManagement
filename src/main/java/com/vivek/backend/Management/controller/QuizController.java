package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.*;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.service.QuizService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<QuizResponseDto> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<QuizResponseDto> updateQuiz(@PathVariable Long id,
                                                      @RequestBody QuizRequestDto dto) {
        return ResponseEntity.ok(quizService.updateQuiz(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.deleteQuizById(id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<QuizResponseDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<QuizResponseDto>> getAllQuiz() {
        return ResponseEntity.ok(quizService.getAllQuiz());
    }

    @GetMapping("/option/{id}")
    public ResponseEntity<List<QuestionResponseDto>> getQuizWithOption(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizWithOption(id));
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizResultResponseDto> submitQuiz(@RequestBody QuizSubmissionDto dto) {
        return ResponseEntity.ok(quizService.evaluate(dto));
    }
}



