package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.post.PostSaveDto;
import com.example.flutter_comm.dto.reaction.ReactionDto;
import com.example.flutter_comm.dto.reaction.ReactionGetDto;
import com.example.flutter_comm.dto.reaction.ReactionResDto;
import com.example.flutter_comm.dto.reaction.ReactionStatusResDto;
import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.service.PostService;
import com.example.flutter_comm.utils.SlugGenerating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserServiceImpl userService, ReactionServiceImpl reactionService, CategoryServiceImpl categoryService, TagServiceImpl tagService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.reactionService = reactionService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }
    @Override
    public Page<PostGetDto> getPaginate(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending());
        Page<Post> postGetListDto = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return postGetListDto.map(this::toPostGetDto);
    }

    @Override
    public List<PostGetDto> get() {
        return null;
    }

    @Override
    public boolean save(PostSaveDto postSaveDto) {
        try {
            User user = userService.findUserByEmail("ducbe2k2@gmail.com");
            String descriptionGenerator = generatorDescriptionFromContent(postSaveDto.getContent());
            Post postSave = Post.builder()
                    .title(postSaveDto.getTitle())
                    .slug(SlugGenerating.toSlug(postSaveDto.getTitle()))
                    .content(postSaveDto.getContent())
                    .tags(postSaveDto.getTags())
                    .category(postSaveDto.getCategory())
                    .author(user)
                    .description(descriptionGenerator.concat("..."))
                    .build();
            postRepository.save(postSave);
            return true;
        } catch (Exception exception) {
            logger.debug(exception.getMessage());
            return false;
        }
    }

    public ReactionResDto getReaction(String slug){
        User user = userService.getUserFromToken();
        Post post = postRepository.findFirstBySlug(slug);
        return reactionService.getReactionOfPostDto(user,post);
    }

    public ReactionStatusResDto postReaction(String slug, ReactionDto reactionDto) {
        User user = userService.getUserFromToken();
        Post post = postRepository.findFirstBySlug(slug);
        return reactionService.addReactionToPost(post, reactionDto, user);
    }

    public PostGetDto toPostGetDto(Post post) {
        List<TagGetDto> tagGetDtoList = post.getTags().stream()
                .map(it -> tagService.toTagGetDto(it))
                .collect(Collectors.toList());
        List<ReactionGetDto> reactionGetDtoList = post.getReactionCounts().stream()
                .map(it -> reactionService.toReactionGetDto(it))
                .collect(Collectors.toList());

        return PostGetDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .slug(post.getSlug())
                .commentCount(100)
                .viewCount(1000)
                .author(userService.toAuthorForPostDto(post.getAuthor()))
                .category(categoryService.toCategoryGetDto(post.getCategory()))
                .tags(tagGetDtoList)
                .reactions(reactionGetDtoList)
                .createdAt(post.getCreatedAt())
                .build();
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
