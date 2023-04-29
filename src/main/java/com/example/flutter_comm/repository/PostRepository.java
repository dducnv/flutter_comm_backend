package com.example.flutter_comm.repository;


import com.example.flutter_comm.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingOrContentContaining(String title,String content);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Post findFirstBySlug(String slug);
}
