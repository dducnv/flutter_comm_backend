package com.example.flutter_comm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class LoginEmailPasswordDto {
        @NotNull(message = "email shouldn't be null")
        @NotBlank(message = "email is empty")
        private String email;
        @NotNull(message = "password shouldn't be null")
        @NotBlank(message = "password is empty")
        private String password;
    }

