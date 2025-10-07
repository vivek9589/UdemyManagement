package com.vivek.backend.Management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;


@Entity
@Table(name = "course")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;
    private double fees;
    private int duration; // in days
    private String description;
    private String publicId;
    private String url;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnore
    private Faculty faculty;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Enrollment> enrollments;




/*
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "facultyId")
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;



 */



}
