package com.vivek.backend.Management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name= "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id @GeneratedValue
    private Long paymentId;

    private Float amount;
    private LocalDate paymentDate;
    private String paymentMethod;

    @OneToOne(mappedBy = "payment")
    private Enrollment enrollment;


}
