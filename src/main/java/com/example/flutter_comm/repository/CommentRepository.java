package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.CommentPost;
import com.example.flutter_comm.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentPost, Long> {
    CommentPost findFistByUuid(UUID uuid);

    List<CommentPost> findByPostAndParentIsNull(Post post);
    List<CommentPost> findByPostAndSupperComment(Post post, CommentPost commentPost);

}
