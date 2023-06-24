package com.example.flutter_comm.dto.comment;

import com.example.flutter_comm.dto.reaction.ReactionResDto;
import com.example.flutter_comm.dto.user.AuthorForPostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CommentGetDto {
    private UUID uuid;
    private String content;
    private AuthorForPostDto createdBy;
    private CommentGetDto parentComment;
    private int replyCount;
    private int countReplyForParent;
    private List<CommentGetDto> replies;
    private ReactionResDto reactionResDto;
    private LocalDateTime createdAt;
    private boolean isLastReply;
    private LocalDateTime editedAt;
    private boolean isDeleted;
    private boolean isAuthor;
}
