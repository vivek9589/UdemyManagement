package com.vivek.backend.Management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;


@Entity
@Table(name = "faculty")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Faculty {

    @Id @GeneratedValue
    private Long facultyId;
    private String facultyName;
    private String specialization; // specialization




    @OneToMany(mappedBy = "faculty" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;





}
