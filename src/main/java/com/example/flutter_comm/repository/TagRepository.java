package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsBySlug(String slug);
    @Query("SELECT t, COUNT(p) as postCount FROM Tag t  LEFT JOIN t.posts p GROUP BY t ORDER BY postCount DESC")
    List<Tag> findAllOrderByPostCounts();

    @Query("SELECT t, COUNT(p) FROM Tag t LEFT JOIN t.posts p WHERE t.name LIKE %:name% GROUP BY t.id ORDER BY COUNT(p) DESC")
    List<Tag> findTagByNameCountPosts(@Param("name") String name);
}
