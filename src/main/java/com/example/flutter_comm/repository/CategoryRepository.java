package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsBySlug(String slug);
}
