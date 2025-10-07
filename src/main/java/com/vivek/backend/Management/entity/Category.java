package com.vivek.backend.Management.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data


@NoArgsConstructor
public class Category {

    @Id
    private Long categoryId;
    private String categoryName;


    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;




}
