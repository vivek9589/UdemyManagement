package com.vivek.backend.Management.repository;


import com.vivek.backend.Management.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

   //get all question by quiz id
   List<Question> findByQuizQuizId(Long quizId);
}
