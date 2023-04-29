package com.example.flutter_comm.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    @NotNull(message = "role name shouldn't be null")
    @NotBlank(message = "role name không được đẻ trống")
    private String name;
    @NotNull(message = "role name shouldn't be null")
    @NotBlank(message = "role name không được đẻ trống")
    private String description;
}
