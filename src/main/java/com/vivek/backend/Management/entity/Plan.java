package com.vivek.backend.Management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanId")
    private Long planId;
    private String name;
    private String description;
    private Double price;
    private int duration;

}
