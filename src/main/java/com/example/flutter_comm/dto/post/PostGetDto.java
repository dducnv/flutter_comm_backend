package com.example.flutter_comm.dto.post;

import com.example.flutter_comm.dto.category.CategoryGetDto;
import com.example.flutter_comm.dto.reaction.ReactionGetDto;
import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.dto.user.AuthorForPostDto;
import com.example.flutter_comm.dto.user.UserCommentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PostGetDto {
    private UUID uuid;
    private String title;
    private String slug;
    private String description;
    private CategoryGetDto category;
    private List<TagGetDto> tags = new ArrayList<>();
    private List<ReactionGetDto> reactions = new ArrayList<>();
    private AuthorForPostDto author;
    private boolean isPublished;
    private int viewCount;
    private int commentCount;
    private int reactionCount;
    private List<UserCommentDto> usersComment;
    private LocalDateTime createdAt;
    public PostGetDto(UUID uuid,String title, String slug, String description, CategoryGetDto category, List<TagGetDto> tags, List<ReactionGetDto> reactions, AuthorForPostDto author, boolean isPublished, int viewCount, int commentCount, int reactionCount,List<UserCommentDto> usersComment, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.reactions = reactions;
        this.author = author;
        this.isPublished = isPublished;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        for (ReactionGetDto it : reactions) {
            this.reactionCount += it.getCount();
        }
        this.usersComment = usersComment;
        this.createdAt = createdAt;
    }
}
