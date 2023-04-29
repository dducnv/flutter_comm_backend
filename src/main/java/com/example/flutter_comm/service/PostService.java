package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.post.PostSaveDto;
import org.springframework.data.domain.Page;

import java.util.List;


public interface PostService {
    Page<PostGetDto> getPaginate(int pageNum, int pageSize);
    List<PostGetDto> get();
    boolean save(PostSaveDto postSaveDto);
}
