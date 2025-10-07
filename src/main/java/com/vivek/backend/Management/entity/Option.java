package com.vivek.backend.Management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    private String options; // The actual option content

    private boolean correct; // Flag to mark correct answer (optional if using separate answer field)

    // Relationship mapping
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "questionId")
    @JsonBackReference
    private Question question;
}