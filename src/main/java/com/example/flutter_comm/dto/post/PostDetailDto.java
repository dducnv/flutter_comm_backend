package com.example.flutter_comm.dto.post;

import com.example.flutter_comm.dto.category.CategoryGetDto;
import com.example.flutter_comm.dto.reaction.ReactionGetDto;
import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.dto.user.AuthorForPostDto;
import com.example.flutter_comm.dto.user.UserCommentDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostDetailDto extends PostGetDto{
    private String contents;
    public PostDetailDto(UUID uuid, String title, String slug, String description, String contents, CategoryGetDto category, List<TagGetDto> tags, List<ReactionGetDto> reactions, AuthorForPostDto author, boolean isPublic, int viewCount, int commentCount  , List<UserCommentDto> usersComment, LocalDateTime createdAt) {
        super(uuid,title, slug, description, category, tags, reactions, author,isPublic, viewCount, commentCount, 0,usersComment, createdAt);
        this.contents = contents;
    }

}
