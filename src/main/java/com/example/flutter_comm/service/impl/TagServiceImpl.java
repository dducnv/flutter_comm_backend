package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.entity.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl {
    public TagGetDto toTagGetDto(Tag tag){
        return TagGetDto.builder()
                .name(tag.getName())
                .slug(tag.getSlug())
                .build();
    }
}
