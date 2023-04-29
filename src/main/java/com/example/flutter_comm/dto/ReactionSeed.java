package com.example.flutter_comm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReactionSeed {
    private int id;
    private String name;

    private String emoji;
    private String url;
}
