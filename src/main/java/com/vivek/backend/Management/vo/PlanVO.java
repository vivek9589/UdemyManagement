package com.vivek.backend.Management.vo;

import lombok.AllArgsConstructor;
import lombok.Data;


//  VO — for dashboard or analytics


@Data
@AllArgsConstructor
public class PlanVO {
    private String name;
    private String description;
    private String priceLabel;     // e.g., "₹499.00"
    private String durationLabel;  // e.g., "30 days"
}