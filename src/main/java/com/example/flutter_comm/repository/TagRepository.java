package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsBySlug(String slug);
}
