package com.example.flutter_comm;

import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes={PostServiceImpl.class, PostRepository.class})
class BackendApplicationTests {
    @Autowired
    PostServiceImpl postService;
    @Test
    void contextLoads() {

//        System.out.println(text);
//        String htmlFromMDX = postService.markdownToHtml(text);
//        System.out.println(htmlFromMDX);
//        System.out.println( postService.removeHtmlTag(htmlFromMDX));
//        System.out.println(postService.generatorDescriptionFromContent( postService.removeHtmlTag(htmlFromMDX)));
    }

}
