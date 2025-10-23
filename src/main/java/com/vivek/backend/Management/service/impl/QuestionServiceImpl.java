package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.OptionResponseDto;
import com.vivek.backend.Management.dto.QuestionRequestDto;
import com.vivek.backend.Management.dto.QuestionResponseDto;
import com.vivek.backend.Management.entity.Option;
import com.vivek.backend.Management.entity.Question;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.exception.QuestionNotFoundException;
import com.vivek.backend.Management.exception.QuizNotFoundException;
import com.vivek.backend.Management.repository.QuestionRepository;
import com.vivek.backend.Management.repository.QuizRepository;
import com.vivek.backend.Management.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public QuestionResponseDto createQuestion(QuestionRequestDto dto) {
        logger.info("Creating question for quiz ID: {}", dto.getQuizId());

        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> {
                    logger.error("Quiz not found with ID: {}", dto.getQuizId());
                    throw new QuizNotFoundException("Quiz not found with ID: " + dto.getQuizId());
                });

        quiz.setNoOfQuestions(quiz.getNoOfQuestions() + 1);
        quizRepository.save(quiz);
        logger.debug("Updated quiz question count to {}", quiz.getNoOfQuestions());

        Question question = Question.builder()
                .questionType(dto.getQuestionType())
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .mark(dto.getMark())
                .quiz(quiz)
                .build();

        List<Option> options = dto.getOptionsList().stream().map(optDto -> {
            Option option = new Option();
            option.setOptions(optDto.getOptions());
            option.setCorrect(optDto.isCorrect());
            option.setQuestion(question);
            return option;
        }).collect(Collectors.toList());

        question.setOptionsList(options);

        Question savedQuestion = questionRepository.save(question);
        logger.info("Question created with ID: {}", savedQuestion.getQuestionId());

        List<OptionResponseDto> optionResponses = savedQuestion.getOptionsList().stream().map(opt ->
                OptionResponseDto.builder()
                        .options(opt.getOptions())
                        .build()
        ).collect(Collectors.toList());

        return QuestionResponseDto.builder()
                .question(savedQuestion.getQuestion())
                .optionsList(optionResponses)
                .build();
    }

    @Override
    public List<Question> getAllQuestionOfQuiz(Long quizId) {
        logger.info("Fetching all questions for quiz ID: {}", quizId);
        List<Question> questions = questionRepository.findByQuizQuizId(quizId);

        if (questions.isEmpty()) {
            logger.warn("No questions found for quiz ID: {}", quizId);
        } else {
            logger.debug("Total questions found: {}", questions.size());
        }

        return questions;
    }

    @Override
    public List<QuestionResponseDto> getAllQuestionsWithOptionsOfQuiz(Long quizId) {
        logger.info("Fetching questions with options for quiz ID: {}", quizId);
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
    public String deleteQuestion(Long questionId) {
        logger.info("Deleting question with ID: {}", questionId);

        if (!questionRepository.existsById(questionId)) {
            logger.warn("Question not found with ID: {}", questionId);
            throw new QuestionNotFoundException("Question not found with ID: " + questionId);
        }

        questionRepository.deleteById(questionId);
        logger.info("Question deleted successfully with ID: {}", questionId);
        return "Question deleted successfully";
    }


    public List<QuestionResponseDto> getAllQuestion() {
        logger.info("Fetching all questions");

        List<Question> questions = questionRepository.findAll();
        logger.debug("Total questions fetched: {}", questions.size());

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
}