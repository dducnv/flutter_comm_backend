package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.dto.tag.TagInfoDto;
import com.example.flutter_comm.entity.Tag;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.repository.TagRepository;
import com.example.flutter_comm.utils.SlugGenerating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl {

    final
    TagRepository tagRepository;
    final
    UserServiceImpl userService;

    public TagServiceImpl(TagRepository tagRepository, UserServiceImpl userService) {
        this.tagRepository = tagRepository;
        this.userService = userService;
    }

    public List<Tag> findTagBySlugs(String slug){
        List<String> tagSlugs = Arrays.asList(slug.split(","));
        return tagRepository.findAllBySlugIn(tagSlugs);
    }

    public List<Tag> tagSeedList(User userOptional) {

        List<Tag> tagSeederList = new ArrayList<>();
        tagSeederList.add(Tag.builder()
                .id(1L)
                .name("widget")
                .slug("widget")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(2L)
                .name("state")
                .slug("state")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(3L)
                .name("flutter")
                .slug("flutter")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(4L)
                .name("dart")
                .slug("dart")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(5L)
                .name("android")
                .slug("android")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(6L)
                .name("ios")
                .slug("ios")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(7L)
                .name("firebase")
                .slug("firebase")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(8L)
                .name("api")
                .slug("api")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(9L)
                .name("http")
                .slug("http")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(10L)
                .name("json")
                .slug("json")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(11L)
                .name("provider")
                .slug("provider")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(12L)
                .name("bloc")
                .slug("bloc")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(13L)
                .name("getx")
                .slug("getx")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(14L)
                .name("riverpod")
                .slug("riverpod")
                .created_by(userOptional)
                .build());
        tagSeederList.add(Tag.builder()
                .id(15L)
                .name("state management")
                .slug("state-management")
                .created_by(userOptional)
                .build());
        return tagSeederList;
    }

    public List<TagInfoDto> getList(String name) {
        Pageable pageable = PageRequest.of(0, 10);
        if (!name.isEmpty()) {
            Page<Tag> tagPage = tagRepository.findTagByNameCountPosts(name , pageable);
            List<Tag> tags = tagPage.getContent();
            return tags.stream().map(this::toTagInfoDto).collect(Collectors.toList());
        }

        Page<Tag> tagPage = tagRepository.findAllOrderByPostCounts(pageable);
        List<Tag> tags = tagPage.getContent();
        return tags.stream().map(this::toTagInfoDto).collect(Collectors.toList());
    }

    public boolean save(TagGetDto tag) {
        User user = userService.getUserFromToken();
        String slug = SlugGenerating.toSlug(tag.getName());
        Tag tagSave = new Tag();
        tagSave.setName(tag.getName());
        tagSave.setCreated_by(user);
        tagSave.setSlug(slug);
        tagRepository.save(tagSave);
        return true;
    }


    public TagInfoDto toTagInfoDto(Tag tag) {
        return TagInfoDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .useCount(tag.getPosts().size())
                .build();
    }

    public TagGetDto toTagGetDto(Tag tag) {
        return TagGetDto.builder()
                .name(tag.getName())
                .slug(tag.getSlug())
                .build();
    }
}
