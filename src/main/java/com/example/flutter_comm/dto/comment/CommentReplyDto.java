package com.example.flutter_comm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CommentReplyDto {
    @NotNull(message = "không được để trống")
    @NotBlank(message = "không được để trống")
    @Min(value = 15, message = "Vui lòng nhập văn bản trên 15 ký tự")
    private String content;
    @NotNull(message = "Vui lòng chọn comment muốn trả lời")
    @NotBlank(message = "Vui lòng chọn comment muốn trả lời")
    private UUID parent_uuid;
}
