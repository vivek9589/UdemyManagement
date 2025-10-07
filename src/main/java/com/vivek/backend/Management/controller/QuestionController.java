package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.QuestionRequestDto;
import com.vivek.backend.Management.dto.QuestionResponseDto;
import com.vivek.backend.Management.entity.Question;
import com.vivek.backend.Management.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {


    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    // add /create question
    @PostMapping("/create")
    public QuestionResponseDto createQuestion(@RequestBody QuestionRequestDto dto){
        return questionService.createQuestion(dto);
    }


    // get all question of quiz
    // http://localhost:8080/question/quiz/1
    @GetMapping("/quiz/{quizId}")
    public List<Question> getAllQuestionOfQuiz(@PathVariable Long quizId) {

            return questionService.getAllQuestionOfQuiz(quizId);
    }


    // delete question by question id
    @DeleteMapping("/delete/{questionId}")
    public String deleteQuestion(@PathVariable Long questionId) {
        return questionService.deleteQuestion(questionId);
    }



        // get all questions with options of quiz
    @GetMapping("/options/{quizId}")
    public List<QuestionResponseDto> getAllQuestionsWithOptionsOfQuiz(@PathVariable Long quizId) {
        return questionService.getAllQuestionsWithOptionsOfQuiz(quizId);
    }



    

}
