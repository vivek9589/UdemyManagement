package com.vivek.backend.Management.service;


import com.vivek.backend.Management.dto.QuestionRequestDto;
import com.vivek.backend.Management.dto.QuestionResponseDto;
import com.vivek.backend.Management.entity.Question;

import java.util.List;

public interface QuestionService {


    // create question  using quiz id to create question for that quiz
    public QuestionResponseDto createQuestion(QuestionRequestDto dto);



    // get questions of quiz
    public  List<Question>  getAllQuestionOfQuiz(Long quizId);


    // Returns list of questions with options
    public List<QuestionResponseDto> getAllQuestionsWithOptionsOfQuiz(Long quizId);



    // delete question
    public String deleteQuestion(Long questionId);




    /*

    // update question
    public String updateQuestion();

    // get question
    public String getQuestion();

     */

}
