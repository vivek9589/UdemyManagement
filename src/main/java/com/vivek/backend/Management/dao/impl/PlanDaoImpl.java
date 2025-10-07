package com.vivek.backend.Management.dao.impl;

import com.vivek.backend.Management.dao.PlanDao;
import com.vivek.backend.Management.vo.PlanVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlanDaoImpl implements PlanDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PlanVO> getPlanDashboardView() {
        String jpql = "SELECT new com.vivek.backend.Management.vo.PlanVO(" +
                "p.name, p.description, CONCAT('₹', p.price), CONCAT(p.duration, ' days')) " +
                "FROM Plan p";
        return entityManager.createQuery(jpql, PlanVO.class).getResultList();
    }

    @Override
    public List<PlanVO> getPlansAbovePrice(Double minPrice) {
        String jpql = "SELECT new com.vivek.backend.Management.vo.PlanVO(" +
                "p.name, p.description, CONCAT('₹', p.price), CONCAT(p.duration, ' days')) " +
                "FROM Plan p WHERE p.price > :minPrice";
        return entityManager.createQuery(jpql, PlanVO.class)
                .setParameter("minPrice", minPrice)
                .getResultList();
    }

    @Override
    public List<PlanVO> getPlansByDurationRange(int minDays, int maxDays) {
        String jpql = "SELECT new com.vivek.backend.Management.vo.PlanVO(" +
                "p.name, p.description, CONCAT('₹', p.price), CONCAT(p.duration, ' days')) " +
                "FROM Plan p WHERE p.duration BETWEEN :minDays AND :maxDays";
        return entityManager.createQuery(jpql, PlanVO.class)
                .setParameter("minDays", minDays)
                .setParameter("maxDays", maxDays)
                .getResultList();
    }

    @Override
    public Double getTotalRevenueFromPlans() {
        String jpql = "SELECT SUM(p.price) FROM Plan p";
        return entityManager.createQuery(jpql, Double.class).getSingleResult();
    }


    @Override
    public Integer getTotalDurationAcrossPlans() {
        String jpql = "SELECT SUM(p.duration) FROM Plan p";
        Long result = entityManager.createQuery(jpql, Long.class).getSingleResult();
        return result != null ? result.intValue() : 0;
    }

}