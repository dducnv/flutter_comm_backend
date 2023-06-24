package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.comment.*;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.entity.CommentPost;
import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.my_enum.CommentFilter;
import com.example.flutter_comm.exception.ApiRequestException;
import com.example.flutter_comm.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl {
    CommentRepository commentRepository;
    UserServiceImpl userService;
    PostServiceImpl postService;
    ReactionServiceImpl reactionService;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserServiceImpl userService, PostServiceImpl postService, ReactionServiceImpl reactionService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
        this.reactionService = reactionService;
    }

    public List<CommentGetDto> getAll() {
        User user = userService.getUserFromToken();
        return commentRepository.findAll().stream().map(it -> toCommentDto(it, user)).collect(Collectors.toList());
    }

    @Cacheable(value = "getCommentOfPost", key = "#post_uuid")
    public CommentResDto getCommentWithoutParentByPostUUID(UUID post_uuid) {
        Pageable pageable = PageRequest.of(0, 10);
        User user = userService.getUserFromToken();
        Post post = postService.getByUUid(post_uuid);

        Page<CommentPost> commentsOutstandingPage = commentRepository.findCommentPostOrderByTotalRepliesAndReaction(post.getUuid(), pageable);
        List<CommentGetDto> commentsOutstandingList = commentsOutstandingPage.getContent().stream()
                .map(commentPost -> toCommentDto(commentPost, user))
                .collect(Collectors.toList());

        Page<CommentPost> commentsLatestPage = commentRepository.findByPost_UuidAndParentIsNullOrderByCreatedAtDesc(post.getUuid(), pageable);
        List<CommentGetDto> commentsLatestList = commentsLatestPage.getContent().stream()
                .map(commentPost -> toCommentDto(commentPost, user))
                .collect(Collectors.toList());

        return CommentResDto.builder()
                .commentsLatest(commentsLatestList)
                .commentsOutstanding(commentsOutstandingList)
                .totalComments(post.getCommentPosts().size())
                .isLastComments(commentsLatestPage.isLast())
                .build();
    }
    @Cacheable(value = "getCommentOfPost", key = "#post_uuid +'_'+ #page + '-' + #commentFilter ", unless = "#result.currentPage != #page")
    public CommentGetMoreDto getMoreComment(UUID post_uuid, int page, CommentFilter commentFilter){
        Pageable pageable = PageRequest.of(page-1, 10);
        User user = userService.getUserFromToken();
        Page<CommentPost> commentGetMore;
        if (Objects.requireNonNull(commentFilter) == CommentFilter.hot) {
            commentGetMore = commentRepository.findCommentPostOrderByTotalRepliesAndReaction(post_uuid, pageable);
        }else {
            commentGetMore = commentRepository.findByPost_UuidAndParentIsNullOrderByCreatedAtDesc(post_uuid, pageable);
        }
        List<CommentGetDto> commentGetDtoList = commentGetMore.getContent().stream()
                .map(commentPost -> toCommentDto(commentPost, user))
                .collect(Collectors.toList());
        return  CommentGetMoreDto.builder()
                .comments(commentGetDtoList)
                .isLastComments(commentGetMore.isLast())
                .currentPage(commentGetMore.getNumber())
                .build();

    }

   @Cacheable(value = "getCommentOfPost", key = "#parent_uuid + '-' +#post_uuid+ '-' + #page", unless = "#result.currentPage != #page")
    public CommentGetMoreDto getMoreCommentByParentUUIDAndPostUUID(UUID parent_uuid,UUID post_uuid, int page) {
        User user = userService.getUserFromToken();
        Pageable pageable = PageRequest.of(page-1, 5);
        CommentPost parent = findCommentByUUid(parent_uuid);
        Page<CommentPost> commentPostPage = commentRepository.findByPostAndSupperComment(parent.getPost(), parent, pageable);
       List<CommentGetDto> commentGetDtoList = commentPostPage.getContent().stream().map(it -> toCommentDto(it, user)).collect(Collectors.toList());
        return  CommentGetMoreDto.builder()
                .comments(commentGetDtoList)
                .isLastComments(commentPostPage.isLast())
                .currentPage(commentPostPage.getNumber())
                .build();
    }

    public ReactionStatusResDto addReactionForComment(UUID uuid, ReactionDto reactionDto) {
        User user = userService.getUserFromToken();
        CommentPost commentPost = findCommentByUUid(uuid);
        return reactionService.addReactionToComment(commentPost, reactionDto, user);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "getCommentOfPost", key = "#post_uuid"),
                    @CacheEvict(value = {"posts"}, allEntries = true)
            }
    )
    public CommentGetDto addReply(CommentReplyDto commentReplyDto, UUID post_uuid) {
        User user = userService.getUserFromToken();
        Post post = postService.getByUUid(post_uuid);
        CommentPost commentParentGet = findCommentByUUid(commentReplyDto.getParent_uuid());
        if (commentParentGet.isDelete()) {
            throw new ApiRequestException("Comment bạn muốn reply đã bị xoá!");
        }
        boolean isHasSupperComment = commentParentGet.getSupperComment() != null;
        if (isHasSupperComment && commentParentGet.getSupperComment().isDelete()) {
            throw new ApiRequestException("Comment bạn muốn reply đã bị xoá!");
        }
        UUID uuid = UUID.randomUUID();
        CommentPost supperComment = commentParentGet.getParent() == null ? commentParentGet : commentParentGet.getSupperComment();
        CommentPost commentReplyPost = new CommentPost();
        commentReplyPost.setContent(commentReplyDto.getContent());
        commentReplyPost.setSupperComment(supperComment);
        commentReplyPost.setParent(commentParentGet);
        commentReplyPost.setUuid(uuid);
        commentReplyPost.setUser(user);
        commentReplyPost.setPost(post);
        //save reply for comment parent
        commentParentGet.getReplies().add(commentReplyPost);
        commentRepository.save(commentReplyPost);
        return CommentGetDto.builder()
                .uuid(commentReplyPost.getUuid())
                .content(commentReplyPost.getContent())
                .createdBy(userService.toAuthorForPostDto(commentReplyPost.getUser()))
                .replyCount(commentReplyPost.getReplies() == null ? 0 : commentReplyPost.getReplies().size())
                .parentComment(commentParentDto(commentParentGet))
                .countReplyForParent(commentParentGet.getReplies().size())
                .build();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "getCommentOfPost", key = "#post_uuid"),
                    @CacheEvict(value = {"posts"}, allEntries = true)
            }
    )

    public CommentGetDto addComment(CommentSaveDto comment, UUID post_uuid) {
        User user = userService.getUserFromToken();
        Post post = postService.getByUUid(post_uuid);
        UUID uuid = UUID.randomUUID();
        CommentPost commentPost = new CommentPost();
        commentPost.setContent(comment.getContent());
        commentPost.setUser(user);
        commentPost.setUuid(uuid);
        commentPost.setPost(post);
        CommentPost commentSave = commentRepository.save(commentPost);
        return CommentGetDto.builder()
                .uuid(commentSave.getUuid())
                .content(comment.getContent())
                .countReplyForParent(0)
                .createdBy(userService.toAuthorForPostDto(commentSave.getUser()))
                .replyCount(commentSave.getReplies() == null ? 0 : commentSave.getReplies().size())
                .build();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "getCommentOfPost", key = "#commentPost.post.uuid")
            }
    )
    public CommentGetDto remove(CommentPost commentPost) {

        User user = userService.getUserFromToken();
        if (!commentPost.getUser().equals(user)) {
            throw new ApiRequestException("Bạn không có quyền xoá comment này!");
        }
        User userIsDelete = User.builder()
                .name("Ẩn danh")
                .avatar("https://res.cloudinary.com/teepublic/image/private/s--IkWqQmTn--/t_Preview/b_rgb:0195c3,c_limit,f_auto,h_630,q_90,w_630/v1570281377/production/designs/6215195_0.jpg")
                .build();
        if (commentPost.getSupperComment() == null) {
            //xoa cho supper comment
            boolean childOfSupperCommentNotSoftDelete = commentPost.getListOfSupperComment()
                    .stream().anyMatch(it -> !it.isDelete());
            if (childOfSupperCommentNotSoftDelete) {
                commentPost.setDelete(true);
                commentRepository.save(commentPost);
                return CommentGetDto.builder()
                        .content("Comment này đã bị xoá")
                        .createdBy(userService.toAuthorForPostDto(userIsDelete))
                        .build();
            }
        } else if (commentPost.getReplies().size() > 0) {
            //cho cho parent comment
            boolean childOfParentCommentNotSoftDelete = commentPost.getReplies()
                    .stream().anyMatch(it -> !it.isDelete());
            // check xem cac comment cap dưới hơn isDelete = true bằng cách lấy thông tin từ parent
            boolean checkChildrenOfSupperComment = commentPost.getSupperComment().getListOfSupperComment()
                    .stream().anyMatch(it -> !it.isDelete());
            if (childOfParentCommentNotSoftDelete || checkChildrenOfSupperComment) {
                commentPost.setDelete(true);
                commentRepository.save(commentPost);
                return CommentGetDto.builder()
                        .content("Comment này đã bị xoá")
                        .createdBy(userService.toAuthorForPostDto(userIsDelete))
                        .build();
            }
        } else {
            boolean checkChildrenOfSupperComment = commentPost.getSupperComment().getListOfSupperComment()
                    .stream().filter(it -> !it.getUuid().equals(commentPost.getUuid())).anyMatch(it -> !it.isDelete());
            //xoá khi tất cả comment của supper comment isDelete = true
            if (!checkChildrenOfSupperComment && commentPost.isDelete()) {
                commentRepository.delete(commentPost.getSupperComment());
                return CommentGetDto.builder()
                        .content("Comment này đã bị xoá")
                        .createdBy(userService.toAuthorForPostDto(userIsDelete))
                        .build();
            }
            //xoá khi tất cả comment của parent comment isDelete = true
            if (commentPost.getParent() != null) {
                boolean checkChildrenOfParentComment = commentPost.getParent().getReplies()
                        .stream().filter(it -> !it.getUuid().equals(commentPost.getUuid())).anyMatch(it -> !it.isDelete());
                if (!checkChildrenOfParentComment && commentPost.getParent().isDelete()) {
                    commentRepository.delete(commentPost.getParent());
                    return CommentGetDto.builder()
                            .content("Comment này đã bị xoá")
                            .createdBy(userService.toAuthorForPostDto(userIsDelete))
                            .build();
                }
            }
        }
        commentRepository.delete(commentPost);
        return CommentGetDto.builder()
                .content("Comment này đã bị xoá")
                .createdBy(userService.toAuthorForPostDto(userIsDelete))
                .build();
    }

    @Caching(evict = {
            @CacheEvict(value = "getCommentOfPost", key = "#commentPost.post.uuid"),
//            @CacheEvict(value = "getCommentOfPost", key = "#commentPost.parent.uuid + '-' +#commentPost.post.uuid")
    })

    public CommentGetDto update(CommentPost commentPost, CommentSaveDto comment) {
        User user = userService.getUserFromToken();
        if (!commentPost.getUser().equals(user)) {
            throw new ApiRequestException("Đây không phải comment của bạn!");
        }
        commentPost.setEditedAt(LocalDateTime.now());
        commentPost.setContent(comment.getContent());
        commentRepository.save(commentPost);
        return toCommentDto(commentPost, user);
    }

    public CommentGetDto toCommentDto(CommentPost commentPost, User user) {
        Pageable pageable = PageRequest.of(0, 5);
        if(commentPost.isDelete()){
            pageable = PageRequest.of(0, 100);
        }
        User userIsDelete = User.builder()
                .name("Ẩn danh")
                .avatar("https://res.cloudinary.com/teepublic/image/private/s--IkWqQmTn--/t_Preview/b_rgb:0195c3,c_limit,f_auto,h_630,q_90,w_630/v1570281377/production/designs/6215195_0.jpg")
                .build();
        Page<CommentPost> commentPostPage = commentRepository.findByPostAndSupperComment(commentPost.getPost(), commentPost, pageable);
        List<CommentGetDto> commentReplyGetDtoList = commentPostPage.getContent().stream()
                .map(it -> toCommentDto(it, user))
                .collect(Collectors.toList());
        if (commentPost.isDelete()) {
            return CommentGetDto.builder()
                    .content("Comment này đã bị xoá")
                    .replies(commentReplyGetDtoList)
                    .createdBy(userService.toAuthorForPostDto(userIsDelete))
                    .isLastReply(true)
                    .isDeleted(commentPost.isDelete())
                    .build();
        }

        return CommentGetDto.builder()
                .uuid(commentPost.getUuid())
                .content(commentPost.getContent())
                .createdBy(userService.toAuthorForPostDto(commentPost.getUser()))
                .parentComment(commentPost.getParent() == null ? null : commentParentDto(commentPost.getParent()))
                .replyCount(commentPost.getReplies().size())
                .editedAt(commentPost.getEditedAt())
                .replies(commentReplyGetDtoList)
                .createdAt(commentPost.getCreatedAt())
                .reactionResDto(reactionService.getReactionOfCommentDto(user, commentPost))
                .isAuthor(commentPost.getUser().equals(commentPost.getPost().getAuthor()))
                .isLastReply(commentPostPage.isLast())
                .isDeleted(commentPost.isDelete())
                .build();
    }

    public CommentGetDto commentParentDto(CommentPost commentPost) {
        return CommentGetDto.builder()
                .uuid(commentPost.getUuid())
                .content(commentPost.getContent())
                .createdBy(userService.toAuthorForPostDto(commentPost.getUser()))
                .build();
    }

    public CommentPost findCommentByUUid(UUID uuid) {
        Optional<CommentPost> commentPost = Optional.ofNullable(commentRepository.findFistByUuid(uuid));
        if (!commentPost.isPresent()) {
            throw new ApiRequestException("Không tìm thấy comment");
        }
        return commentPost.get();
    }
}
