package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.dto.PlanRequestDto;
import com.vivek.backend.Management.dto.PlanResponseDto;
import com.vivek.backend.Management.service.PlanService;
import com.vivek.backend.Management.vo.PlanVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/plan")
public class PlanController {

    private final PlanService plansService;

    public PlanController(PlanService plansService) {
        this.plansService = plansService;
    }

    @PreAuthorize("hasAuthority('UDEMY_WRITE')")
    @PostMapping("/create")
    public ResponseEntity<PlanResponseDto> createPlan(@RequestBody PlanRequestDto dto) {
        return ResponseEntity.ok(plansService.createPlan(dto));
    }

    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/all")
    public ResponseEntity<List<PlanResponseDto>> getAllPlans() {
        return ResponseEntity.ok(plansService.getAllPlans());
    }

    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDto> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(plansService.getPlanById(id));
    }

    @PreAuthorize("hasAuthority('UDEMY_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlanById(@PathVariable Long id) {
        return ResponseEntity.ok(plansService.deletePlanById(id));
    }

    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/dashboard")
    public ResponseEntity<List<PlanVO>> getPlanDashboardView() {
        return ResponseEntity.ok(plansService.getPlanDashboardView());
    }


    //  Get plans above a certain price
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/above-price")
    public ResponseEntity<List<PlanVO>> getPlansAbovePrice(@RequestParam Double minPrice) {
        List<PlanVO> plans = plansService.getPlansAbovePrice(minPrice);
        return ResponseEntity.ok(plans);
    }

    // Get plans within a duration range
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/duration-range")
    public ResponseEntity<List<PlanVO>> getPlansByDurationRange(@RequestParam int minDays,
                                                                @RequestParam int maxDays) {
        List<PlanVO> plans = plansService.getPlansByDurationRange(minDays, maxDays);
        return ResponseEntity.ok(plans);
    }

    //  Get total revenue from all plans
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/total-revenue")
    public ResponseEntity<Double> getTotalRevenueFromPlans() {
        Double totalRevenue = plansService.getTotalRevenueFromPlans();
        return ResponseEntity.ok(totalRevenue);
    }

    // Get total duration across all plans
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/total-duration")
    public ResponseEntity<Integer> getTotalDurationAcrossPlans() {
        Integer totalDuration = plansService.getTotalDurationAcrossPlans();
        return ResponseEntity.ok(totalDuration);
    }




}

