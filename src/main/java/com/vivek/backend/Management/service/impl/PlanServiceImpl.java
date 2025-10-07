package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dao.PlanDao;
import com.vivek.backend.Management.dto.PlanRequestDto;
import com.vivek.backend.Management.dto.PlanResponseDto;
import com.vivek.backend.Management.entity.Plan;
import com.vivek.backend.Management.repository.PlanRepository;
import com.vivek.backend.Management.service.PlanService;
import com.vivek.backend.Management.vo.PlanVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository plansRepository;
    private final PlanDao plansDao;

    public PlanServiceImpl(PlanRepository plansRepository, PlanDao plansDao) {
        this.plansRepository = plansRepository;
        this.plansDao = plansDao;
    }

    @Override
    public PlanResponseDto createPlan(PlanRequestDto dto) {
        Plan plan = Plan.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .duration(dto.getDuration())
                .build();
        Plan saved = plansRepository.save(plan);
        return mapToResponseDto(saved);
    }

    @Override
    public List<PlanResponseDto> getAllPlans() {
        return plansRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public PlanResponseDto getPlanById(Long id) {
        Plan plan = plansRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plan not found with id: " + id));
        return mapToResponseDto(plan);
    }

    @Override
    public String deletePlanById(Long id) {
        plansRepository.deleteById(id);
        return "Plan Deleted Successfully";
    }

    @Override
    public List<PlanVO> getPlanDashboardView() {
        return plansDao.getPlanDashboardView();
    }

    @Override
    public List<PlanVO> getPlansAbovePrice(Double minPrice) {
        return plansDao.getPlansAbovePrice(minPrice);
    }

    @Override
    public List<PlanVO> getPlansByDurationRange(int minDays, int maxDays) {
        return plansDao.getPlansByDurationRange(minDays, maxDays);
    }

    @Override
    public Double getTotalRevenueFromPlans() {
        return plansDao.getTotalRevenueFromPlans();
    }

    @Override
    public Integer getTotalDurationAcrossPlans() {
        return plansDao.getTotalDurationAcrossPlans();
    }

    private PlanResponseDto mapToResponseDto(Plan plan) {
        return PlanResponseDto.builder()
                .planId(plan.getPlanId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .duration(plan.getDuration())
                .build();
    }
}


