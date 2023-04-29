package com.example.flutter_comm.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorForPostDto {
    private String name;
    private String avatar;
    private String username;

}
