package com.example.flutter_comm.api.comment;

import com.example.flutter_comm.dto.ApiResDto;
import com.example.flutter_comm.dto.BlackWorkDto;
import com.example.flutter_comm.dto.comment.CommentGetDto;
import com.example.flutter_comm.dto.comment.CommentReplyDto;
import com.example.flutter_comm.dto.comment.CommentSaveDto;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.entity.CommentPost;
import com.example.flutter_comm.service.impl.AppServiceImpl;
import com.example.flutter_comm.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.*;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class CommentApiController {
    CommentServiceImpl commentService;
    AppServiceImpl appService;

    @Autowired
    public CommentApiController(CommentServiceImpl commentService, AppServiceImpl appService) {
        this.commentService = commentService;
        this.appService = appService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = PREFIX_COMMENT_API, method = RequestMethod.GET)
    public ResponseEntity<?> getComments() {
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Thêm bình luận thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(commentService.getAll())
                        .build()
        );
    }
    @RequestMapping(value = PREFIX_COMMENT_PARENT_API, method = RequestMethod.GET)
    public ResponseEntity<?> getCommentWithoutParent(@PathVariable UUID post_uuid) {
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Lấy dữ liệu thành công!")
                        .httpStatus(HttpStatus.OK)
                        .data(commentService.getCommentWithoutParentByPostUUID(post_uuid))
                        .build()
        );
    }
    @RequestMapping(value = PREFIX_COMMENT_REPLY_GET_API, method = RequestMethod.GET)
    public ResponseEntity<?> getCommentByParentUUIDAndPostUUID(@PathVariable UUID post_uuid, @PathVariable UUID parent_uuid) {
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Lấy dữ liệu thành công!")
                        .httpStatus(HttpStatus.OK)
                        .data(commentService.getCommentByParentUUIDAndPostUUID(parent_uuid, post_uuid))
                        .build()
        );
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_COMMENT_PARENT_API, method = RequestMethod.POST)
    public ResponseEntity<?> saveComment(@RequestBody CommentSaveDto commentPost, @PathVariable UUID post_uuid) {
        String textFilterBlackWords = commentPost.getContent();
        BlackWorkDto blackWorkDto = appService.blackWorkFilter(textFilterBlackWords);
        if (blackWorkDto.isHadBlackWord()) {
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Bài đăng chứa các từ ngữ cấm!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(blackWorkDto)
                            .build()
            );
        }
        CommentGetDto commentPostRes = commentService.addComment(commentPost, post_uuid);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Thêm bình luận thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(commentPostRes)
                        .build()
        );
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_COMMENT_REPLY_API, method = RequestMethod.POST)
    public ResponseEntity<?> saveReplyComment(@RequestBody CommentReplyDto commentPost, @PathVariable UUID post_uuid) {
        String textFilterBlackWords = commentPost.getContent();
        BlackWorkDto blackWorkDto = appService.blackWorkFilter(textFilterBlackWords);
        if (blackWorkDto.isHadBlackWord()) {
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Bài đăng chứa các từ ngữ cấm!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(blackWorkDto)
                            .build()
            );
        }
        CommentGetDto commentPostRes = commentService.addReply(commentPost, post_uuid);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Thêm bình luận thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(commentPostRes)
                        .build()
        );
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_COMMENT_UPDATE_API, method = RequestMethod.PUT)
    public ResponseEntity<?> updateComment(@RequestBody CommentSaveDto commentPost, @PathVariable UUID comment_uuid) {
        String textFilterBlackWords = commentPost.getContent();
        BlackWorkDto blackWorkDto = appService.blackWorkFilter(textFilterBlackWords);
        if (blackWorkDto.isHadBlackWord()) {
            return ResponseEntity.badRequest().body(
                    ApiResDto.builder()
                            .message("Bài đăng chứa các từ ngữ cấm!")
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .data(blackWorkDto)
                            .build()
            );
        }
        CommentPost commentPostGet = commentService.findCommentByUUid(comment_uuid);
        CommentGetDto commentPostRes = commentService.update(commentPostGet, commentPost);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Cập nhật luận thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(commentPostRes)
                        .build()
        );
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value =PREFIX_COMMENT_UPDATE_API, method = RequestMethod.DELETE)
    public ResponseEntity<?> removeComment(@PathVariable UUID comment_uuid) {
        CommentPost commentPost = commentService.findCommentByUUid(comment_uuid);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Xoá bình luận thành công!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(commentService.remove(commentPost))
                        .build()
        );
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_COMMENT_REACTION, method = RequestMethod.POST)
    public ResponseEntity<?> reactionPost(@PathVariable UUID comment_uuid, @RequestBody ReactionDto reactionDto) {
        ReactionStatusResDto reactionStatusResDto = commentService.addReactionForComment(comment_uuid, reactionDto);
        return ResponseEntity.ok(
                ApiResDto.builder()
                        .message("Success!")
                        .httpStatus(HttpStatus.CREATED)
                        .data(reactionStatusResDto)
                        .build());
    }
}

