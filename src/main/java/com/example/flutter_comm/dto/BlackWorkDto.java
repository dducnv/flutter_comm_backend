package com.example.flutter_comm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class BlackWorkDto {
    private boolean isHadBlackWord;
    public String blackWords;
}
