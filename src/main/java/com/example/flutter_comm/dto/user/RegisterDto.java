package com.example.flutter_comm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegisterDto {
    private String fullName;
    private String userName;
    private String email;

}
