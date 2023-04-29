package com.example.flutter_comm.dto.category;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryGetDto {
    private String name;
    private String slug;
}
