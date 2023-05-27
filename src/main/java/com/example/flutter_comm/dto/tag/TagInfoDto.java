package com.example.flutter_comm.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TagInfoDto {
    private Long id;
    private String name;
    private String slug;
    private int useCount;
}
