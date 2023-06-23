package com.example.flutter_comm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CommentResDto {
    private List<CommentGetDto> commentsLatest;
    private List<CommentGetDto> commentsOutstanding;
    private boolean isLastComments;
    private int totalComments;
}
