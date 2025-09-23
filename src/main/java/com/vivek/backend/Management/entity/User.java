package com.vivek.backend.Management.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vivek.backend.Management.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*This is the standard Plain Old Java Object (POJO) that represents the users table in the database. */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;
    private String lastName;

    private String phone;
    private String email;
    private String password;

    private int age;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    // private String role;
    private Role role;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Enrollment enrollment;


    // Relationship mapping

/*
    // one user has many course (1-M)
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();





 */





}
