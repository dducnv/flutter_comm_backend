package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.ReactionPostCount;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.UserReactionPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReactionPostRepository extends JpaRepository<UserReactionPost, Long> {
    boolean existsByUserAndReactionPostCount(User user, ReactionPostCount reactionPostCount);

    Optional<UserReactionPost> findFirstByUserAndReactionPostCount(User user, ReactionPostCount reactionPostCount);
    int countAllByReactionPostCount(ReactionPostCount reactionPostCount);
}
