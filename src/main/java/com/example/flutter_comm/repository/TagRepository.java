package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsBySlug(String slug);

    List<Tag> findAllBySlugIn(List<String> slugs);
    @Query("SELECT t, COUNT(p) as postCount FROM Tag t  LEFT JOIN t.posts p GROUP BY t ORDER BY postCount DESC")
    Page<Tag> findAllOrderByPostCounts(Pageable pageable);

    @Query("SELECT t, COUNT(p) FROM Tag t LEFT JOIN t.posts p WHERE t.name LIKE %:name% GROUP BY t.id ORDER BY COUNT(p) DESC")
    Page<Tag> findTagByNameCountPosts(@Param("name") String name, Pageable pageable);
}
