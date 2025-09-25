package com.vivek.backend.Management.service;

import com.vivek.backend.Management.entity.Plans;

import java.util.List;

public interface PlansService {

    // create plan
    public Plans createPlan(Plans plans);


    // get all plans

    public List<Plans> getAllPlans();

    // get single plan by ID
    public Plans getPlanById(Long id);

    // delete Plan by ID
    public String deletePlanById(Long id);


    //


}
