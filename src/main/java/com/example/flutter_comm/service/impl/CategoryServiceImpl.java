package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.category.CategoryGetDto;
import com.example.flutter_comm.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {
    public CategoryGetDto toCategoryGetDto(Category category){
        return CategoryGetDto.builder()
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}
