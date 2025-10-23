package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.*;
import com.vivek.backend.Management.entity.Option;
import com.vivek.backend.Management.entity.Question;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.entity.QuizResult;
import com.vivek.backend.Management.exception.EnrollmentNotFoundException;
import com.vivek.backend.Management.exception.QuizNotFoundException;
import com.vivek.backend.Management.repository.EnrollmentRepository;
import com.vivek.backend.Management.repository.QuestionRepository;
import com.vivek.backend.Management.repository.QuizRepository;
import com.vivek.backend.Management.repository.QuizResultRepository;
import com.vivek.backend.Management.service.QuestionService;
import com.vivek.backend.Management.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Autowired private QuizRepository quizRepository;
    @Autowired private QuizResultRepository quizResultRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private QuestionService questionService;
    @Autowired private EnrollmentRepository enrollmentRepository;

    @Override
    public QuizResponseDto createQuiz(Quiz quiz) {
        logger.info("Creating new quiz");

        if (quiz.getQuestionsList() != null) {
            for (Question question : quiz.getQuestionsList()) {
                question.setQuiz(quiz);
                if (question.getOptionsList() != null) {
                    for (Option option : question.getOptionsList()) {
                        option.setQuestion(question);
                    }
                }
            }
        }

        Quiz savedQuiz = quizRepository.save(quiz);
        logger.info("Quiz created successfully with ID: {}", savedQuiz.getQuizId());

        return mapToResponseDto(savedQuiz);
    }

    @Override
    public QuizResponseDto updateQuiz(Long id, QuizRequestDto dto) {
        logger.info("Updating quiz with ID: {}", id);

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Quiz not found with ID: {}", id);
                    throw new QuizNotFoundException("Quiz not found with ID: " + id);
                });

        quiz.setTitle(dto.getTitle());
        quiz.setNoOfQuestions(dto.getNoOfQuestions());
        quiz.setTotalTime(dto.getTotalTime());
        quiz.setCreatedBy(dto.getCreatedBy());
        quiz.setNoOfAttempts(dto.getNoOfAttempts());

        Quiz updatedQuiz = quizRepository.save(quiz);
        logger.info("Quiz updated successfully with ID: {}", updatedQuiz.getQuizId());

        return mapToResponseDto(updatedQuiz);
    }

    @Override
    public QuizResponseDto getQuizById(Long id) {
        logger.info("Fetching quiz with ID: {}", id);

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz not found with ID: {}", id);
                    throw new QuizNotFoundException("Quiz not found with ID: " + id);
                });

        logger.debug("Quiz found: {}", quiz.getTitle());
        return mapToResponseDto(quiz);
    }

    @Override
    public List<QuizResponseDto> getAllQuiz() {
        logger.info("Fetching all quizzes");

        List<Quiz> quizzes = quizRepository.findAll();
        logger.debug("Total quizzes fetched: {}", quizzes.size());

        return quizzes.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteQuizById(Long id) {
        logger.info("Deleting quiz with ID: {}", id);

        if (!quizRepository.existsById(id)) {
            logger.warn("Quiz not found for deletion with ID: {}", id);
            throw new QuizNotFoundException("Quiz not found with ID: " + id);
        }

        quizRepository.deleteById(id);
        logger.info("Quiz deleted successfully with ID: {}", id);

        return "Quiz deleted successfully";
    }

    @Override
    public List<QuestionResponseDto> getQuizWithOption(Long quizId) {
        logger.info("Fetching quiz questions with options for quiz ID: {}", quizId);

        List<Question> questions = questionRepository.findByQuizQuizId(quizId);
        if (questions.isEmpty()) {
            logger.warn("No questions found for quiz ID: {}", quizId);
        }

        return questions.stream().map(question -> {
            List<OptionResponseDto> optionResponses = question.getOptionsList().stream()
                    .map(opt -> OptionResponseDto.builder()
                            .options(opt.getOptions())
                            .build())
                    .collect(Collectors.toList());

            return QuestionResponseDto.builder()
                    .question(question.getQuestion())
                    .optionsList(optionResponses)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public QuizResultResponseDto evaluate(QuizSubmissionDto quizSubmissionDto) {
        Long userId = quizSubmissionDto.getUserId();
        Long quizId = quizSubmissionDto.getQuizId();

        logger.info("Evaluating quiz submission for user ID: {} and quiz ID: {}", userId, quizId);

        if (!enrollmentRepository.existsByUser_UserId(userId)) {
            logger.warn("User not enrolled: {}", userId);
            throw new EnrollmentNotFoundException("User is not enrolled");
        }

        List<Question> questions = questionService.getAllQuestionOfQuiz(quizId);
        int correct = 0;

        for (AnswerDto answer : quizSubmissionDto.getAnswers()) {
            for (Question question : questions) {
                if (question.getAnswer().trim().equalsIgnoreCase(answer.getSelectedOption().trim())) {
                    correct++;
                    break;
                }
            }
        }

        QuizResultResponseDto quizResult = new QuizResultResponseDto();
        quizResult.setQuizId(quizId);
        quizResult.setUserId(userId);
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

        logger.info("Quiz evaluated for user ID: {} with result: {}", userId, quizResult.getRemark());
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