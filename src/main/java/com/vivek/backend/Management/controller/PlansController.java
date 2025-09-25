package com.vivek.backend.Management.controller;


import com.vivek.backend.Management.entity.Plans;
import com.vivek.backend.Management.service.PlansService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlansController {


    private final PlansService plansService;

   public PlansController(PlansService plansService)
    {
        this.plansService = plansService;
    }

    // create plan

    @PreAuthorize("hasAuthority('UDEMY_WRITE')")
    @PostMapping("/create")
    public ResponseEntity<Plans> createPlan(@RequestBody Plans plans)
    {
        plansService.createPlan(plans);
        return ResponseEntity.ok(plans);
    }

    // get all plans
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/all")
    public ResponseEntity<List<Plans>> getAllPlans()
    {
        List<Plans> plans = plansService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    // get single plan by ID
    @PreAuthorize("hasAuthority('UDEMY_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<Plans> getPlanById(@PathVariable Long id)
    {
        Plans plan = plansService.getPlanById(id);
        return ResponseEntity.ok(plan);
    }


    // delete Plan by ID

    @PreAuthorize("hasAuthority('UDEMY_DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlanById(@PathVariable Long id)
    {
        String response = plansService.deletePlanById(id);
        return ResponseEntity.ok(response);
    }




}
