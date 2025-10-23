package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.dao.PlanDao;
import com.vivek.backend.Management.dto.PlanRequestDto;
import com.vivek.backend.Management.dto.PlanResponseDto;
import com.vivek.backend.Management.entity.Plan;
import com.vivek.backend.Management.exception.PlanNotFoundException;
import com.vivek.backend.Management.repository.PlanRepository;
import com.vivek.backend.Management.service.PlanService;
import com.vivek.backend.Management.vo.PlanVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    @Override
    public PlanResponseDto createPlan(PlanRequestDto dto) {
        logger.info("Plan creating");
        Plan plan = Plan.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .duration(dto.getDuration())
                .build();
        Plan saved = plansRepository.save(plan);

        logger.info("Plan Created with ID: {}",plan.getPlanId());
        return mapToResponseDto(saved);
    }

    @Override
    public List<PlanResponseDto> getAllPlans() {
        logger.info("Getting all the plans");
        // logger.debug("Total plans fetched: {}", plans.size());

        return plansRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public PlanResponseDto getPlanById(Long id) {
        logger.info("Getting Plan by ID : {}",id);
        Plan plan = plansRepository.findById(id)
                .orElseThrow(() ->
                {   logger.warn("Plan not found with ID: {}",id);
                    throw new PlanNotFoundException("Plan not found with id: " + id);
                });
        logger.info("Get Plan with ID: {}",id);
        return mapToResponseDto(plan);
    }

    @Override
    public String deletePlanById(Long id) {
        logger.info("Deleting Plan with ID :{}",id);
        if (!plansRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent plan with ID: {}", id);
            throw new PlanNotFoundException("Plan not found with ID: " + id);
        }

        plansRepository.deleteById(id);

        logger.info("Deleted Plan with ID:",id);
        return "Plan Deleted Successfully";
    }

    @Override
    public List<PlanVO> getPlanDashboardView() {
        logger.info("Get Plan Dashboard view");
        return plansDao.getPlanDashboardView();
    }

    @Override
    public List<PlanVO> getPlansAbovePrice(Double minPrice) {
        logger.info("Get Plans Above Minimum price : {}",minPrice);
        return plansDao.getPlansAbovePrice(minPrice);
    }

    @Override
    public List<PlanVO> getPlansByDurationRange(int minDays, int maxDays) {

        logger.info("Getting plans by Duration Range: minDays={} to maxDays={}", minDays, maxDays);        return plansDao.getPlansByDurationRange(minDays, maxDays);
    }

    @Override
    public Double getTotalRevenueFromPlans() {
        logger.info("Getting Total Revenue From all the plans ");
        return plansDao.getTotalRevenueFromPlans();
    }

    @Override
    public Integer getTotalDurationAcrossPlans() {
        logger.info("Getting total duration across all plans");
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


