package com.example.flutter_comm.dto;

import com.example.flutter_comm.entity.my_enum.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private UUID uuid;
    private String avatar;
    private String name;
    private String bio;
    private boolean verify;
    private boolean emailVerify;
    private String username;
    private String email;
    private String emailPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus userStatus;

}
