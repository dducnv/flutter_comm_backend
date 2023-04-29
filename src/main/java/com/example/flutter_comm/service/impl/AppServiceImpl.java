package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> searchPostUseElasticsearch(String keyword) {
        List<Post> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return posts;
    }
}
