package com.vivek.backend.Management.certificate;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "certificate")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;
    private String userName;
    private String courseName;
    private LocalDate issueDate;


    // private String certificateUrl;




}
