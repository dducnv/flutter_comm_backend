package com.example.flutter_comm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentGetMoreDto {
    private List<CommentGetDto> comments;
    private boolean isLastComments;
    private int currentPage;
}
