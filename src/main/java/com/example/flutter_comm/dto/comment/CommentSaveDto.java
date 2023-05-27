package com.example.flutter_comm.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveDto {
    @NotNull(message = "không được để trống")
    @NotBlank(message = "không được để trống")
    private String content;
}
