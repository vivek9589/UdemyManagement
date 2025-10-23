package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.PlanRequestDto;
import com.vivek.backend.Management.dto.PlanResponseDto;
import com.vivek.backend.Management.service.PlanService;
import com.vivek.backend.Management.vo.PlanVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/plan")
public class PlanController {

    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    private final PlanService plansService;

    public PlanController(PlanService plansService) {
        this.plansService = plansService;
    }

    @PreAuthorize("hasAuthority('PLAN_CREATE')")
    @PostMapping("/create")
    public ResponseEntity<PlanResponseDto> createPlan(@RequestBody PlanRequestDto dto) {
        logger.info("Received request to create a new plan");
        PlanResponseDto response = plansService.createPlan(dto);
        logger.info("Plan created successfully with ID: {}", response.getPlanId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/all")
    public ResponseEntity<List<PlanResponseDto>> getAllPlans() {
        logger.info("Fetching all plans");
        List<PlanResponseDto> plans = plansService.getAllPlans();
        logger.debug("Total plans fetched: {}", plans.size());
        return ResponseEntity.ok(plans);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDto> getPlanById(@PathVariable Long id) {
        logger.info("Fetching plan with ID: {}", id);
        PlanResponseDto plan = plansService.getPlanById(id);
        return ResponseEntity.ok(plan);
    }

    @PreAuthorize("hasAuthority('PLAN_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlanById(@PathVariable Long id) {
        logger.info("Deleting plan with ID: {}", id);
        String result = plansService.deletePlanById(id);
        logger.info("Plan deleted successfully with ID: {}", id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/dashboard")
    public ResponseEntity<List<PlanVO>> getPlanDashboardView() {
        logger.info("Fetching plan dashboard view");
        List<PlanVO> dashboard = plansService.getPlanDashboardView();
        return ResponseEntity.ok(dashboard);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/above-price")
    public ResponseEntity<List<PlanVO>> getPlansAbovePrice(@RequestParam Double minPrice) {
        logger.info("Fetching plans above price: {}", minPrice);
        List<PlanVO> plans = plansService.getPlansAbovePrice(minPrice);
        return ResponseEntity.ok(plans);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/duration-range")
    public ResponseEntity<List<PlanVO>> getPlansByDurationRange(@RequestParam int minDays,
                                                                @RequestParam int maxDays) {
        logger.info("Fetching plans with duration between {} and {} days", minDays, maxDays);
        List<PlanVO> plans = plansService.getPlansByDurationRange(minDays, maxDays);
        return ResponseEntity.ok(plans);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/total-revenue")
    public ResponseEntity<Double> getTotalRevenueFromPlans() {
        logger.info("Fetching total revenue from all plans");
        Double totalRevenue = plansService.getTotalRevenueFromPlans();
        return ResponseEntity.ok(totalRevenue);
    }

    @PreAuthorize("hasAuthority('PLAN_READ')")
    @GetMapping("/total-duration")
    public ResponseEntity<Integer> getTotalDurationAcrossPlans() {
        logger.info("Fetching total duration across all plans");
        Integer totalDuration = plansService.getTotalDurationAcrossPlans();
        return ResponseEntity.ok(totalDuration);
    }
}