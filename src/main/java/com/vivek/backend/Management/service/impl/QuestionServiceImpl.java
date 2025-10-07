package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dto.OptionResponseDto;
import com.vivek.backend.Management.dto.QuestionRequestDto;
import com.vivek.backend.Management.dto.QuestionResponseDto;
import com.vivek.backend.Management.entity.Option;
import com.vivek.backend.Management.entity.Question;
import com.vivek.backend.Management.entity.Quiz;
import com.vivek.backend.Management.repository.QuestionRepository;
import com.vivek.backend.Management.repository.QuizRepository;
import com.vivek.backend.Management.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Autowired
    private QuizRepository quizRepository;


    @Override
    public QuestionResponseDto createQuestion(QuestionRequestDto dto) {

        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + dto.getQuizId()));

        // update no. of questions in quiz
        quiz.setNoOfQuestions(quiz.getNoOfQuestions() + 1);
        quizRepository.save(quiz);

        Question question = Question.builder()
                .questionType(dto.getQuestionType())
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .mark(dto.getMark())
                .quiz(quiz)
                .build();

        // Map OptionRequestDto to Option entities
        List<Option> options = dto.getOptionsList().stream().map(optDto -> {
            Option option = new Option();
            option.setOptions(optDto.getOptions());
            option.setCorrect(optDto.isCorrect());
            option.setQuestion(question); // link to parent question
            return option;
        }).collect(Collectors.toList());

        question.setOptionsList(options);

        Question savedQuestion = questionRepository.save(question);

        // Map Option entities to OptionResponseDto
        List<OptionResponseDto> optionResponses = savedQuestion.getOptionsList().stream().map(opt ->
                OptionResponseDto.builder()
                        //.optionId(opt.getOptionId())
                        .options(opt.getOptions())
                        // .correct(opt.isCorrect())
                        .build()
        ).collect(Collectors.toList());

        return QuestionResponseDto.builder()
                // .questionId(savedQuestion.getQuestionId())
               // .questionType(savedQuestion.getQuestionType())
                .question(savedQuestion.getQuestion())
                //.answer(savedQuestion.getAnswer())
                //.mark(savedQuestion.getMark())
                // .quizId(savedQuestion.getQuiz().getQuizId())
                .optionsList(optionResponses)
                .build();
    }

    @Override
    public List<Question> getAllQuestionOfQuiz(Long quizId) {

        List<Question> quiz = questionRepository.findByQuizQuizId(quizId);
        return quiz;
    }

    @Override
    public List<QuestionResponseDto> getAllQuestionsWithOptionsOfQuiz(Long quizId) {
        List<Question> questions = questionRepository.findByQuizQuizId(quizId);

        return questions.stream().map(question -> {
            List<OptionResponseDto> optionResponses = question.getOptionsList().stream().map(opt ->
                    OptionResponseDto.builder()
                            //.optionId(opt.getOptionId())
                            .options(opt.getOptions())
                            //.correct(opt.isCorrect())
                            .build()
            ).collect(Collectors.toList());

            return QuestionResponseDto.builder()
                    //.questionId(question.getQuestionId())
                    //.questionType(question.getQuestionType())
                    .question(question.getQuestion())
                    //.answer(question.getAnswer())
                    //.mark(question.getMark())
                    //.quizId(question.getQuiz().getQuizId())
                    .optionsList(optionResponses)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public String deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
        return "Question deleted successfully";
    }


    public List<QuestionResponseDto> getAllQuestion() {

        List<Question> questions = questionRepository.findAll();

        return questions.stream().map(question -> {
            List<OptionResponseDto> optionResponses = question.getOptionsList().stream().map(opt ->
                    OptionResponseDto.builder()
                            //.optionId(opt.getOptionId())
                            .options(opt.getOptions())
                            //.correct(opt.isCorrect())
                            .build()
            ).collect(Collectors.toList());

            return QuestionResponseDto.builder()
                    //.questionId(question.getQuestionId())
                    //.questionType(question.getQuestionType())
                    .question(question.getQuestion())
                    //.answer(question.getAnswer())
                    //.mark(question.getMark())
                    //.quizId(question.getQuiz().getQuizId())
                    .optionsList(optionResponses)
                    .build();
        }).collect(Collectors.toList());
    }





}
