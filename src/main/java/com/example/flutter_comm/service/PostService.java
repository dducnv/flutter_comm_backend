package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.post.PostSaveDto;
import com.example.flutter_comm.dto.post.PostUpdateDto;

import java.util.UUID;


public interface PostService {


    boolean save(PostSaveDto postSaveDto);
    boolean update(PostUpdateDto postUpdateDto, UUID uuid);
}
