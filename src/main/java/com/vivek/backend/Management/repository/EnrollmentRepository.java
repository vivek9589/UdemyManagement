package com.vivek.backend.Management.repository;


import com.vivek.backend.Management.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    boolean existsByUser_UserId(Long userId); /*Spring Data JPA uses property navigation to generate queries:

                                                existsByUser_Id(...) → looks for user.id

                                                existsByUser_UserId(...) → looks for user.userId
                                                */

    Enrollment findByUser_UserId(Long userId);
    void deleteByUser_UserId(Long userId);

    void deleteByEnrollmentId(Long enrollmentId);

}
