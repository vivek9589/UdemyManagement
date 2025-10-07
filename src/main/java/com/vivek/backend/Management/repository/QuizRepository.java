package com.vivek.backend.Management.repository;

import com.vivek.backend.Management.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
