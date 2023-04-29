package com.example.flutter_comm.dto.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReactionStatusResDto {
    private String name;
    private String status;
}
