package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.CommentPost;
import com.example.flutter_comm.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentPost, Long> {
    CommentPost findFistByUuid(UUID uuid);
    @Query("SELECT c " +
            "FROM CommentPost c " +
            "LEFT JOIN c.replies r " +
            "LEFT JOIN c.reactionCommentCounts rcc " +
            "LEFT JOIN rcc.userReactionComments urc " +
            "WHERE c.post.uuid = :uuid AND c.parent IS NULL " +
            "GROUP BY c " +
            "ORDER BY COUNT(r) DESC,  COUNT(urc)  DESC")
    Page<CommentPost> findCommentPostOrderByTotalRepliesAndReaction(UUID uuid, Pageable pageable);
    Page<CommentPost> findByPostAndParentIsNullOrderByCreatedAtDesc(Post post,Pageable pageable);

    Page<CommentPost> findByPost_UuidAndParentIsNullOrderByCreatedAtDesc(UUID uuid,Pageable pageable);
    Page<CommentPost> findByPostAndSupperComment(Post post, CommentPost commentPost,Pageable pageable);

}
