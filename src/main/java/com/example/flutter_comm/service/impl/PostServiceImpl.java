package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.category.CategoryGetDto;
import com.example.flutter_comm.dto.post.PostDetailDto;
import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.post.PostSaveDto;
import com.example.flutter_comm.dto.post.PostUpdateDto;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionGetDto;
import com.example.flutter_comm.dto.reaction.ReactionResDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.dto.user.UserCommentDto;
import com.example.flutter_comm.entity.*;
import com.example.flutter_comm.entity.my_enum.PostSort;
import com.example.flutter_comm.entity.my_enum.PostType;
import com.example.flutter_comm.exception.ApiRequestException;
import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.repository.PostViewRepository;
import com.example.flutter_comm.service.PostService;
import com.example.flutter_comm.utils.Generating;
import com.example.flutter_comm.utils.SlugGenerating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private Logger logger;
    PostRepository postRepository;
    UserServiceImpl userService;
    ReactionServiceImpl reactionService;

    CategoryServiceImpl categoryService;
    TagServiceImpl tagService;
    PostViewRepository postViewRepository;
    TopicInterestServiceImpl topicInterestService;

    CacheManager cacheManager;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserServiceImpl userService, ReactionServiceImpl reactionService, CategoryServiceImpl categoryService, TagServiceImpl tagService, PostViewRepository postViewRepository, TopicInterestServiceImpl topicInterestService, CacheManager cacheManager) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.reactionService = reactionService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.postViewRepository = postViewRepository;
        this.topicInterestService = topicInterestService;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "posts", key = "#pageNum + '-' + #pageSize+'-'+#type+'-'+#sort")
    public Page<PostGetDto> getPosts(int pageNum, int pageSize, PostType type, PostSort sort) {
        User user = userService.getUserFromToken();
        List<Tag> tagInterest = null;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending());
        Page<Post> postGetListDto = null;
        if (user != null && user.getTopicInterest() != null) {
            tagInterest = user.getTopicInterest().stream().map(TopicInterest::getTag).collect(Collectors.toList());
        }
        if (sort == PostSort.suggest && user == null) {
            throw new ApiRequestException("Vui lòng đăng nhập!");
        }
        Category category;
        switch (type) {
            case posts:
                category = categoryService.categorySeedList().get(1);
                if (sort == PostSort.suggest) {
                    postGetListDto = postRepository.findByTagsInAndCategoryOrderByTagsDescViewCountDesc(tagInterest, category, pageable);
                } else {
                    postGetListDto = postRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
                }
                return postGetListDto.map(this::toPostGetDto);
            case questions:
                category = categoryService.categorySeedList().get(0);
                if (sort == PostSort.suggest) {
                    postGetListDto = postRepository.findByTagsInAndCategoryOrderByTagsDesc(tagInterest, category, pageable);
                } else {
                    postGetListDto = postRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
                }
                return postGetListDto.map(this::toPostGetDto);
            case discussion:
                category = categoryService.categorySeedList().get(2);
                if (sort == PostSort.suggest) {
                    postGetListDto = postRepository.findByTagsInAndCategoryOrderByTagsDesc(tagInterest, category, pageable);
                } else {
                    postGetListDto = postRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
                }
                return postGetListDto.map(this::toPostGetDto);
            default:
                if (sort == PostSort.suggest) {
                    postGetListDto = postRepository.findByTagsInOrderByCreatedAtDesc(tagInterest, pageable);
                } else {
                    postGetListDto = postRepository.findAllByOrderByCreatedAtDesc(pageable);
                }
                return postGetListDto.map(this::toPostGetDto);
        }
    }
    public Page<PostGetDto> getPostsUnPublic(int pageNum, int pageSize){
        User user = userService.getUserFromToken();
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("updatedAt").descending());
        return postRepository.findPostUnPublic(pageable).map(this::toPostGetDto);
    }
    public PostDetailDto getDetails(String slug) {
        Optional<Post> post = Optional.ofNullable(getBySlug(slug));
        if (!post.isPresent()) {
            throw new ApiRequestException("Không tìm thấy post");
        }

        return toPostDetailDto(post.get());
    }

    @Override
    @CacheEvict(value = {"posts"}, allEntries = true)
    public boolean save(PostSaveDto postSaveDto) {
        try {
            User user = userService.getUserFromToken();
            UUID uuid = UUID.randomUUID();
            String descriptionGenerator = generatorDescriptionFromContent(postSaveDto.getContent());
            String descSave = descriptionGenerator.length() > 300 ? descriptionGenerator.concat("..."): descriptionGenerator;
            Post postSave = Post.builder()
                    .title(postSaveDto.getTitle())
                    .slug(SlugGenerating.toSlug(postSaveDto.getTitle()).concat("-" + String.valueOf(Generating.generatePassword(4, false))))
                    .content(postSaveDto.getContent())
                    .tags(postSaveDto.getTags())
                    .category(postSaveDto.getCategory())
                    .uuid(uuid)
                    .author(user)
                    .description(descSave)
                    .build();
            postRepository.save(postSave);
            return true;
        } catch (Exception exception) {
            logger.debug(exception.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"posts"}, allEntries = true)
    public boolean update(PostUpdateDto postUpdateDto, UUID uuid) {
        try {
            Post post = getByUUid(uuid);
            User user = userService.getUserFromToken();
            if (!post.getAuthor().equals(user)) {
                throw new ApiRequestException("Bạn không có quyền sửa bài viết này!");
            }
            String descriptionGenerator = generatorDescriptionFromContent(postUpdateDto.getContent());
            String descSave = descriptionGenerator.length() > 300 ? descriptionGenerator.concat("..."): descriptionGenerator;
            post.setTitle(post.getTitle());
            post.setSlug(SlugGenerating.toSlug(postUpdateDto.getTitle()).concat("-" + String.valueOf(Generating.generatePassword(4, false))));
            post.setTags(postUpdateDto.getTags());
            post.setContent(postUpdateDto.getContent());
            post.setDescription(descSave);
            postRepository.save(post);
            return true;

        } catch (Exception exception) {
            logger.debug(exception.getMessage());
            return false;
        }
    }

    public Post getByUUid(UUID uuid) {
        Optional<Post> post = Optional.ofNullable(postRepository.findFirstByUuid(uuid));
        if (!post.isPresent()) {
            throw new ApiRequestException("Không tìm thấy post");
        }
        return post.get();
    }

    public Post getBySlug(String slug) {
        Optional<Post> post = Optional.ofNullable(postRepository.findFirstBySlug(slug));
        if (!post.isPresent()) {
            throw new ApiRequestException("Không tìm thấy post");
        }
        return post.get();
    }
    @CacheEvict(value = {"posts"}, allEntries = true)
    public boolean changePublicPost(UUID post_uuid, boolean status){
        Post post = getByUUid(post_uuid);
        User user = userService.getUserFromToken();
        if (!post.getAuthor().equals(user)) {
            throw new ApiRequestException("Bạn không có quyền sửa bài viết này!");
        }
        post.setPostPublic(status);
        postRepository.save(post);
        return true;
    }
    @CacheEvict(value = {"posts"}, allEntries = true)
    public boolean removePost(UUID post_uuid){
        Post post = getByUUid(post_uuid);
        User user = userService.getUserFromToken();
        if (!post.getAuthor().equals(user)) {
            throw new ApiRequestException("Bạn không có quyền sửa bài viết này!");
        }
        postRepository.delete(post);
        return false;
    }
//    @Cacheable(value = "getReaction", key = "#uuid")
    public ReactionResDto getReaction(UUID uuid, String ipAddress) {
        User user = userService.getUserFromToken();
        Post post = getByUUid(uuid);
        if (user != null) {
            topicInterestService.updateTopicInterest(post, user);
        }
        viewCount(post, ipAddress);
        return reactionService.getReactionOfPostDto(user, post);
    }

//    @CacheEvict(value = "getReaction", key = "#uuid")
    public ReactionStatusResDto postReaction(UUID uuid, ReactionDto reactionDto) {
        User user = userService.getUserFromToken();
        Post post = getByUUid(uuid);
        return reactionService.addReactionToPost(post, reactionDto, user);
    }

    public void viewCount(Post post, String ipAddress) {
        Optional<PostView> isViewed = Optional.ofNullable(postViewRepository.findByPostAndIpAddress(post, ipAddress));
        LocalDateTime cutoff = LocalDateTime.now().minus(Duration.ofMinutes(15));
        if (!isViewed.isPresent()) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
            PostView postView = new PostView();
            postView.setPost(post);
            postView.setIpAddress(ipAddress);
            postViewRepository.save(postView);

        } else {
            if (isViewed.get().getCreatedAt().isBefore(cutoff)) {
                post.setViewCount(post.getViewCount() + 1);
                postRepository.save(post);
            }
        }

    }


    public PostGetDto toPostGetDto(Post post) {
        List<TagGetDto> tagGetDtoList = post.getTags().stream()
                .map(it -> tagService.toTagGetDto(it))
                .collect(Collectors.toList());
        List<ReactionGetDto> reactionGetDtoList = post.getReactionCounts().stream()
                .map(it -> reactionService.toReactionPostGetDto(it))
                .collect(Collectors.toList());

        return PostGetDto.builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .description(post.getDescription())
                .slug(post.getSlug())
                .commentCount(post.getCommentPosts().size())
                .viewCount(post.getViewCount())
                .author(userService.toAuthorForPostDto(post.getAuthor()))
                .category(categoryService.toCategoryGetDto(post.getCategory()))
                .tags(tagGetDtoList)
                .reactions(reactionGetDtoList)
                .isPublic(post.isPostPublic())
                .usersComment(getUserCommentOfPost(post.getCommentPosts()))
                .createdAt(post.getCreatedAt())
                .build();
    }

    public PostDetailDto toPostDetailDto(Post post) {
        List<TagGetDto> tagGetDtoList = post.getTags().stream()
                .map(it -> tagService.toTagGetDto(it))
                .collect(Collectors.toList());
        List<ReactionGetDto> reactionGetDtoList = post.getReactionCounts().stream()
                .map(it -> reactionService.toReactionPostGetDto(it))
                .collect(Collectors.toList());
        CategoryGetDto category = categoryService.toCategoryGetDto(post.getCategory());
        return new PostDetailDto(
                post.getUuid(),
                post.getTitle(),
                post.getSlug(),
                post.getDescription(),
                post.getContent(),
                category,
                tagGetDtoList,
                reactionGetDtoList,
                userService.toAuthorForPostDto(post.getAuthor()),
                post.isPostPublic(),
                post.getViewCount(),
                post.getCommentPosts().size(),
                getUserCommentOfPost(post.getCommentPosts()),
                post.getCreatedAt()
        );
    }

    public List<UserCommentDto> getUserCommentOfPost(Set<CommentPost> commentPostSet){
        Map<Long, UserCommentDto> userCommentMap = commentPostSet.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getUser().getId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                userComments -> {
                                    UserCommentDto userComment = new UserCommentDto();
                                    userComment.setAvatar(userComments.get(0).getUser().getAvatar());
                                    userComment.setName(userComments.get(0).getUser().getName());
                                    userComment.setUsername(userComments.get(0).getUser().getUsername());
                                    userComment.setCommentCount(userComments.size());
                                    return userComment;
                                }
                        )
                ));

        return new ArrayList<>(userCommentMap.values());
    }

    public String generatorDescriptionFromContent(String content) {
        String markdownToText = markdownToHtml(content);
        String removeHtmlTag = removeHtmlTag(markdownToText);
        String[] words = removeHtmlTag.split("\\s+");
        int numWords = Math.min(50, words.length);
        StringBuilder newParagraph = new StringBuilder();
        for (int i = 0; i < numWords; i++) {
            newParagraph.append(words[i]);
            newParagraph.append(" ");
        }
        return newParagraph.toString();
    }

    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public String removeHtmlTag(String html) {
        return Jsoup.parse(html).text();
    }

}
