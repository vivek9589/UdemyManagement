package com.vivek.backend.Management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponseDto {
    private Long planId;
    private String name;
    private String description;
    private Double price;
    private int duration;
}