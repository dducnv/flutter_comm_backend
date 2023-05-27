package com.example.flutter_comm.repository;


import com.example.flutter_comm.entity.Category;
import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.Tag;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
    @Where(clause = "post_public=true")
    List<Post> findByTitleContainingOrContentContaining(String title,String content);
    @Where(clause = "post_public=true")
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Where(clause = "post_public=true")
    Page<Post> findByTagsInOrderByCreatedAtDesc(List<Tag> tags, Pageable pageable);
    @Where(clause = "post_public=true")
    Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category,Pageable pageable);
    @Where(clause = "post_public=true")
    Post findFirstBySlug(String slug);
    @Where(clause = "post_public=true")
    Post findFirstByUuid(UUID uuid);

    Post findFirstBySlugAndCategory(String slug,Category category);
    @Query(value = "SELECT p FROM Post p WHERE  p.postPublic = false ")
    Page<Post> findPostUnPublic(Pageable pageable);
    @Where(clause = "post_public=true")
    Page<Post> findByTagsInAndCategoryOrderByTagsDescViewCountDesc(List<Tag> tags,Category category, Pageable pageable);
    @Where(clause = "post_public=true")
    Page<Post> findByTagsInAndCategoryOrderByTagsDesc(List<Tag> tags, Category category, Pageable pageable);


}
