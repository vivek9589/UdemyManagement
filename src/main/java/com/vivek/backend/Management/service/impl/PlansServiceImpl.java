package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.entity.Plans;
import com.vivek.backend.Management.repository.PlansRepository;
import com.vivek.backend.Management.service.PlansService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlansServiceImpl implements PlansService {

    private final PlansRepository plansRepository;

   public PlansServiceImpl(PlansRepository plansRepository)
    {
        this.plansRepository = plansRepository;
    }



    @Override
    public Plans createPlan(Plans plans) {

        return plansRepository.save(plans);


    }

    @Override
    public List<Plans> getAllPlans() {

        List<Plans> plans = plansRepository.findAll();
        return plans;
    }

    @Override
    public Plans getPlanById(Long id) {

       Plans plan = plansRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Plan not found with id: " + id));
       return plan;
    }

    @Override
    public String deletePlanById(Long id) {

        plansRepository.deleteById(id);
        return "Plan Deleted Successfully";
    }
}
