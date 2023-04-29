package com.example.flutter_comm.dto.post;

import com.example.flutter_comm.dto.category.CategoryGetDto;
import com.example.flutter_comm.dto.reaction.ReactionGetDto;
import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.dto.user.AuthorForPostDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class PostGetDto {
    private String title;
    private String slug;
    private String description;
    private CategoryGetDto category;
    private List<TagGetDto> tags = new ArrayList<>();
    private List<ReactionGetDto> reactions = new ArrayList<>();
    private AuthorForPostDto author;
    private int viewCount;
    private int commentCount;
    private int reactionCount;
    private LocalDateTime createdAt;
    public PostGetDto(String title, String slug, String description, CategoryGetDto category, List<TagGetDto> tags, List<ReactionGetDto> reactions, AuthorForPostDto author, int viewCount, int commentCount, int reactionCount, LocalDateTime createdAt) {
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.reactions = reactions;
        this.author = author;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        for (ReactionGetDto it : reactions) {
            this.reactionCount += it.getCount();
        }
        this.createdAt = createdAt;
    }
}
