package com.vivek.backend.Management.repository;

import com.vivek.backend.Management.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
