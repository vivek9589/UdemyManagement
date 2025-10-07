package com.vivek.backend.Management.dao;

import com.vivek.backend.Management.vo.PlanVO;

import java.util.List;

public interface PlanDao {
    List<PlanVO> getPlanDashboardView();

    List<PlanVO> getPlansAbovePrice(Double minPrice);
    List<PlanVO> getPlansByDurationRange(int minDays, int maxDays);

    Double getTotalRevenueFromPlans();
    Integer getTotalDurationAcrossPlans();


}