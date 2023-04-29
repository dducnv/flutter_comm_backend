package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.User;

public interface ReactionService {
    ReactionStatusResDto addReactionToPost(Post post, ReactionDto reaction, User user);
}
