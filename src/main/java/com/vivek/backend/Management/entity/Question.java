package com.vivek.backend.Management.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vivek.backend.Management.enums.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "questions")
@Entity
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    //I can use enum for question type
    // private String questionType;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    private String question;
    private String answer;
    private int mark;


    // Relationship mapping
    // many questions belong to one quiz (M-1)
    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "quizId")
    @JsonBackReference
    private Quiz quiz;

    // one question has many options (1-M)
    @OneToMany(mappedBy = "question" , cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Option> optionsList = new ArrayList<>();;





}
