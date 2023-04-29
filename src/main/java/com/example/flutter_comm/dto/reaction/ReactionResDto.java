package com.example.flutter_comm.dto.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ReactionResDto {
    private int totalReaction;
    private List<ReactionGetDto> myReactions;
    private List<ReactionGetDto> reactions;
}
