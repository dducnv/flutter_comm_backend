package com.example.flutter_comm.dto.reaction;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactionGetDto {
    private String name;
    private String iconUrl;
    private String emoji;
    private int count;
}
