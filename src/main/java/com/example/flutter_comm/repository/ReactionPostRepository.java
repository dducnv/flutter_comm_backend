package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.Reaction;
import com.example.flutter_comm.entity.ReactionPostCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionPostRepository extends JpaRepository<ReactionPostCount, Long> {
    ReactionPostCount findFirstByPostAndReaction(Post post, Reaction reaction);

    int countAllByPost(Post post);
}
