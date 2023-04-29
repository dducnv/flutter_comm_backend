package com.example.flutter_comm.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CredentialDto {
    private String accessToken;
    private Long expiresIn;
    private String tokenType;
    private String scope;
}
