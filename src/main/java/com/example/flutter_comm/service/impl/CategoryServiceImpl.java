package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.category.CategoryGetDto;
import com.example.flutter_comm.entity.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl {
    public List<Category> categorySeedList(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1L,"Hỏi đáp","hoi-dap"));
        categoryList.add(new Category(2L,"Bài viết","bai-viet"));
        categoryList.add(new Category(3L,"Thảo luận","thao-luan"));
        categoryList.add(new Category(4L,"Nghề nghiệp","nghe-nghiep"));
        return categoryList;
    }
    public CategoryGetDto toCategoryGetDto(Category category){
        return CategoryGetDto.builder()
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}
