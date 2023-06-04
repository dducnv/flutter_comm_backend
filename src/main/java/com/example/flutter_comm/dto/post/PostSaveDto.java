package com.example.flutter_comm.dto.post;

import com.example.flutter_comm.entity.Category;
import com.example.flutter_comm.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Set<Tag> tags = new HashSet<>();
    @NotBlank
    @NotNull
    private Category category;
    @NotNull
    private boolean isPublished = Boolean.TRUE;
}
