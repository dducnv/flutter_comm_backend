package com.example.flutter_comm.dto.tag;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGetDto {
    private String name;
    private String slug;
}
