package com.example.flutter_comm.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UsernameCheckDto {
    @NotNull(message = "Username không được để trống.")
    @NotBlank(message = "Username không được để trống.")
    private String username;
}
