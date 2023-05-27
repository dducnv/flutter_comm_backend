package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.ReactionCommentCount;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.UserReactionComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserReactionCommentRepository  extends JpaRepository<UserReactionComment, Long> {
    Optional<UserReactionComment> findFirstByUserAndReactionCommentCount(User user, ReactionCommentCount reactionCommentCount);
    int countAllByReactionCommentCount(ReactionCommentCount reactionCommentCount);
}
