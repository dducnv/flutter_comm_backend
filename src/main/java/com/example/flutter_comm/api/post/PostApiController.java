package com.example.flutter_comm.api.post;

import com.example.flutter_comm.dto.ApiResDto;
import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.post.PostSaveDto;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionResDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.PREFIX_POST_API;
import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.PREFIX_POST_REACTION;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class PostApiController {
    @Autowired
    PostServiceImpl postService;

    @RequestMapping(value = PREFIX_POST_API, method = RequestMethod.GET)
    public ResponseEntity<Page<PostGetDto>> getPosts(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getPaginate(page -1, size));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_API, method = RequestMethod.POST)
    public ResponseEntity<?> savePost(@RequestBody PostSaveDto postSaveDto) {
        boolean postSaveStatus = postService.save(postSaveDto);
        if (!postSaveStatus) {
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Tạo bài đăng thất bài!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(false)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Tạo bài đăng thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(true)
                        .build()
        );
    }
    @RequestMapping(value = PREFIX_POST_REACTION, method = RequestMethod.GET)
    public ResponseEntity<?> getReactionPost(@PathVariable String slug) {
        ReactionResDto getReactionPost = postService.getReaction(slug);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Lấy danh sách reaction bài viết thành công!")
                        .httpStatus(HttpStatus.OK)
                        .data(getReactionPost)
                        .build());
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_REACTION, method = RequestMethod.POST)
    public ResponseEntity<?> reactionPost(@PathVariable String slug, @RequestBody ReactionDto reactionDto) {
      ReactionStatusResDto reactionStatusResDto = postService.postReaction(slug, reactionDto);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Reaction Post Success!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(reactionStatusResDto)
                        .build());
    }
}
