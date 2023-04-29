package com.example.flutter_comm.dto.reaction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ReactionDto {
    private Long id;
    private String name;
    @Column(name = "icon_url")
    private String iconUrl;
    @Column(name = "reaction_emoji")
    private String emoji;
}
