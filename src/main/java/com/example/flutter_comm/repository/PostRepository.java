package com.example.flutter_comm.repository;


import com.example.flutter_comm.entity.Category;
import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.Tag;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Where(clause = "post_public=true")
    @Query(value="SELECT p FROM Post p WHERE (p.title LIKE CONCAT('%', :title, '%') OR p.content LIKE  CONCAT('%', :content, '%') ) AND p.category = :category ORDER BY CASE WHEN p.title LIKE :title THEN 0 ELSE 1 END, LENGTH(p.title)")
    Page<Post> searchPost(@Param("title") String title, @Param("content") String content, @Param("category") Category category, Pageable pageable);

    @Where(clause = "post_public=true")
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Where(clause = "post_public=true")
    Page<Post> findByTagsInOrderByCreatedAtDesc(List<Tag> tags, Pageable pageable);

    @Where(clause = "post_public=true")
    Page<Post> findByTagsInAndCategoryOrderByCreatedAtDesc(Set<Tag> tags, Category category, Pageable pageable);

    @Where(clause = "post_public=true")
    Page<Post> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    @Where(clause = "post_public=true")
    Post findFirstBySlug(String slug);

    @Where(clause = "post_public=true")
    Post findFirstByUuid(UUID uuid);

    Post findFirstBySlugAndCategory(String slug, Category category);

    @Query(value = "SELECT p FROM Post p WHERE  p.postPublic = false ")
    Page<Post> findPostUnPublic(Pageable pageable);

    @Where(clause = "post_public=true")
    Page<Post> findByTagsInAndCategoryOrderByTagsDescViewCountDesc(List<Tag> tags, Category category, Pageable pageable);

    @Where(clause = "post_public=true")
    Page<Post> findByTagsInAndCategoryOrderByTagsDesc(List<Tag> tags, Category category, Pageable pageable);


}
