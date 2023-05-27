package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.CommentPost;
import com.example.flutter_comm.entity.Reaction;
import com.example.flutter_comm.entity.ReactionCommentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionCommentRepository extends JpaRepository<ReactionCommentCount, Long> {
    ReactionCommentCount findFirstByCommentPostAndReaction(CommentPost post, Reaction reaction);
}
