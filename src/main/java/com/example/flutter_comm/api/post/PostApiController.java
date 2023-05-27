package com.example.flutter_comm.api.post;

import com.example.flutter_comm.dto.ApiResDto;
import com.example.flutter_comm.dto.BlackWorkDto;
import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.post.PostSaveDto;
import com.example.flutter_comm.dto.post.PostUpdateDto;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionResDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.entity.my_enum.PostSort;
import com.example.flutter_comm.entity.my_enum.PostType;
import com.example.flutter_comm.exception.ApiRequestException;
import com.example.flutter_comm.service.impl.AppServiceImpl;
import com.example.flutter_comm.service.impl.CategoryServiceImpl;
import com.example.flutter_comm.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.*;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class PostApiController {
    PostServiceImpl postService;
    CategoryServiceImpl categoryService;
    AppServiceImpl appService;

    @Autowired
    public PostApiController(PostServiceImpl postService, CategoryServiceImpl categoryService, AppServiceImpl appService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.appService = appService;
    }

    @RequestMapping(value = PREFIX_POST_API, method = RequestMethod.GET)
    public ResponseEntity<Page<PostGetDto>> getPosts(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "posts") String type,
                                                     @RequestParam(defaultValue = "all") String sort
    ) {
        try {
            PostType postType = PostType.valueOf(type);
            PostSort postSort = PostSort.valueOf(sort);
            return ResponseEntity.ok(postService.getPosts(page, 10, postType, postSort));
        } catch (IllegalArgumentException ex) {
            throw new ApiRequestException("Request Param không hợp lệ!");
        } catch (ApiRequestException apiRequestException) {
            throw new ApiRequestException(apiRequestException.getMessage());
        } catch (RuntimeException runtimeException) {
            throw new ApiRequestException("Có lỗi khi truy vấn dữ liệu, vui lòng thử lại!");
        }
    }
    @RequestMapping(value = PREFIX_GET_POST_UN_PUBLIC_API, method = RequestMethod.GET)
    public ResponseEntity<Page<PostGetDto>> getPostsUnPublic(@RequestParam(defaultValue = "1") int page ) {
            return ResponseEntity.ok(postService.getPostsUnPublic(page -1, 10));
    }

    @RequestMapping(value = PREFIX_POST_DETAILS_API, method = RequestMethod.GET)
    public ResponseEntity<?> details(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getDetails(slug));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_API, method = RequestMethod.POST)
    public ResponseEntity<?> savePost(@RequestBody PostSaveDto postSaveDto) {
        String textFilterBlackWords = postSaveDto.getTitle() + " "+ postSaveDto.getContent();
        BlackWorkDto blackWorkDto = appService.blackWorkFilter(textFilterBlackWords);
        if(blackWorkDto.isHadBlackWord()){
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Bài đăng chứa các từ ngữ cấm!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(blackWorkDto)
                            .build()
            );
        }
        boolean postSaveStatus = postService.save(postSaveDto);
        if (!postSaveStatus) {
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Tạo bài đăng thất bại!")
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_UPDATE_API, method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@RequestBody PostUpdateDto postUpdateDto, @PathVariable UUID post_uuid) {
        String textFilterBlackWords = postUpdateDto.getTitle() + " "+ postUpdateDto.getContent();
        BlackWorkDto blackWorkDto = appService.blackWorkFilter(textFilterBlackWords);
        if(blackWorkDto.isHadBlackWord()){
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Bài đăng chứa các từ ngữ cấm!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(blackWorkDto)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Tạo bài đăng thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(postService.update(postUpdateDto,post_uuid))
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_UPDATE_API, method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable UUID post_uuid) {
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Xoá bài viết thành công!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(postService.removePost(post_uuid))
                            .build()
            );

    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_PUBLIC_API, method = RequestMethod.PUT)
    public ResponseEntity<?> publicPost(@PathVariable UUID post_uuid) {
        return ResponseEntity.badRequest().body(
                ApiResDto.builder()
                        .message("Bài viết đã được công khai!")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .data(postService.changePublicPost(post_uuid,true))
                        .build()
        );

    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_UN_PUBLIC_API, method = RequestMethod.PUT)
    public ResponseEntity<?> unPublicPost(@PathVariable UUID post_uuid) {
        return ResponseEntity.badRequest().body(
                ApiResDto.builder()
                        .message("Bài viết đã được ẩn!")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .data(postService.changePublicPost(post_uuid,false))
                        .build()
        );

    }
    @RequestMapping(value = PREFIX_POST_REACTION, method = RequestMethod.GET)
    public ResponseEntity<?> getReactionPost(@PathVariable UUID post_uuid, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        ReactionResDto getReactionPost = postService.getReaction(post_uuid, ipAddress);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Lấy danh sách reaction bài viết thành công!")
                        .httpStatus(HttpStatus.OK)
                        .data(getReactionPost)
                        .build());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_POST_REACTION, method = RequestMethod.POST)
    public ResponseEntity<?> reactionPost(@PathVariable UUID post_uuid, @RequestBody ReactionDto reactionDto) {
        ReactionStatusResDto reactionStatusResDto = postService.postReaction(post_uuid, reactionDto);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Reaction Post Success!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(reactionStatusResDto)
                        .build());
    }
}
