package com.vivek.backend.Management.repository;

import com.vivek.backend.Management.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult,Long> {
}
