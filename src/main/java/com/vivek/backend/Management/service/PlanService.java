package com.vivek.backend.Management.service;

import com.vivek.backend.Management.dto.PlanRequestDto;
import com.vivek.backend.Management.dto.PlanResponseDto;
import com.vivek.backend.Management.vo.PlanVO;

import java.util.List;

public interface PlanService {


    // jpaRepository related methods
    PlanResponseDto createPlan(PlanRequestDto dto);
    List<PlanResponseDto> getAllPlans();
    PlanResponseDto getPlanById(Long id);
    String deletePlanById(Long id);




    // PlanDao related methods
    List<PlanVO> getPlanDashboardView();

    List<PlanVO> getPlansAbovePrice(Double minPrice);

    List<PlanVO> getPlansByDurationRange(int minDays, int maxDays);

    Double getTotalRevenueFromPlans();

    Integer getTotalDurationAcrossPlans();



}
