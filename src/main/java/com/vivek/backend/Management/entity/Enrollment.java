package com.vivek.backend.Management.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vivek.backend.Management.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    private LocalDate enrollmentDate;
    private LocalDate expiresAt;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;  // status (TRIAL,   MONTHLY ,HALFYEARLY,  YEARLY)


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    @JsonIgnore
    private Course course;



    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;


}
