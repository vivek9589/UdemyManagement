package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.*;
import com.vivek.backend.Management.entity.Option;
import com.vivek.backend.Management.entity.Question;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.entity.QuizResult;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.QuestionRepository;
import com.vivek.backend.Management.repository.QuizRepository;
import com.vivek.backend.Management.repository.QuizResultRepository;
import com.vivek.backend.Management.service.QuestionService;
import com.vivek.backend.Management.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public QuizResponseDto createQuiz(Quiz quiz) {
        if (quiz.getQuestionsList() != null) {
            for (Question question : quiz.getQuestionsList()) {
                question.setQuiz(quiz); // Link question to quiz

                if (question.getOptionsList() != null) {
                    for (Option option : question.getOptionsList()) {
                        option.setQuestion(question); // Link option to question
                    }
                }
            }
        }

         quizRepository.save(quiz); // Cascade saves everything

        return mapToResponseDto(quiz);
    }


    @Override
    public QuizResponseDto updateQuiz(Long id, QuizRequestDto dto) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));

        quiz.setTitle(dto.getTitle());
        quiz.setNoOfQuestions(dto.getNoOfQuestions());
        quiz.setTotalTime(dto.getTotalTime());
        quiz.setCreatedBy(dto.getCreatedBy());
        quiz.setNoOfAttempts(dto.getNoOfAttempts());

        Quiz updated = quizRepository.save(quiz);
        return mapToResponseDto(updated);
    }

    @Override
    public QuizResponseDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        return mapToResponseDto(quiz);
    }

    @Override
    public List<QuizResponseDto> getAllQuiz() {
        return quizRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteQuizById(Long id) {
        quizRepository.deleteById(id);
        return "Quiz deleted successfully";
    }

    @Override
    public List<QuestionResponseDto> getQuizWithOption(Long quizId) {
        List<Question> questions = questionRepository.findByQuizQuizId(quizId);
        return questions.stream().map(question -> {
            List<OptionResponseDto> optionResponses = question.getOptionsList().stream().map(opt ->
                    OptionResponseDto.builder()
                            .options(opt.getOptions())
                            .build()
            ).collect(Collectors.toList());

            return QuestionResponseDto.builder()
                    .question(question.getQuestion())
                    .optionsList(optionResponses)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public QuizResultResponseDto evaluate(QuizSubmissionDto quizSubmissionDto) {

        // Check enrollment
        if (!enrollmentRepository.existsByUser_UserId(quizSubmissionDto.getUserId())) {
            throw new RuntimeException("User is not enrolled");
        }

        List<Question> questions = questionService.getAllQuestionOfQuiz(quizSubmissionDto.getQuizId());
        int correct = 0;

        // Match answers by question text
        for (AnswerDto answer : quizSubmissionDto.getAnswers()) {
            for (Question question : questions) {
                if (question.getAnswer().trim().equalsIgnoreCase(answer.getSelectedOption().trim())) {
                    correct++;
                    break; // once matched, skip to next answer
                }
            }
        }


        QuizResultResponseDto quizResult = new QuizResultResponseDto();

        quizResult.setQuizId(null);
        quizResult.setQuizId(quizSubmissionDto.getQuizId());
        quizResult.setUserId(quizSubmissionDto.getUserId());
        quizResult.setTotalQuestions(questions.size());
        quizResult.setCorrectAnswers(correct);
        quizResult.setAttemptedQuestions(quizSubmissionDto.getAnswers().size());
        quizResult.setWrongAnswers(quizResult.getAttemptedQuestions() - correct);
        quizResult.setPercentage((correct * 100.0) / questions.size());
        quizResult.setRemark(quizResult.getPercentage() >= 40 ? "Passed" : "Failed");

        quizResultRepository.save(QuizResult.builder()
                .quizId(quizResult.getQuizId())
                .userId(quizResult.getUserId())
                .totalQuestions(quizResult.getTotalQuestions())
                .attemptedQuestions(quizResult.getAttemptedQuestions())
                .correctAnswers(quizResult.getCorrectAnswers())
                .wrongAnswers(quizResult.getWrongAnswers())
                .percentage(quizResult.getPercentage())
                .remark(quizResult.getRemark())
                .build());



        return quizResult;
    }

    private QuizResponseDto mapToResponseDto(Quiz quiz) {
        return QuizResponseDto.builder()
                .quizId(quiz.getQuizId())
                .title(quiz.getTitle())
                .noOfQuestions(quiz.getNoOfQuestions())
                .totalTimeFormatted(quiz.getTotalTime().toString())
                .createdBy(quiz.getCreatedBy())
                .noOfAttempts(quiz.getNoOfAttempts())
                .questionCount(quiz.getQuestionsList() != null ? quiz.getQuestionsList().size() : 0)
                .build();
    }
}

