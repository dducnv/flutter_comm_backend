package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewRepository  extends JpaRepository<PostView, Long> {
    boolean existsByPostAndIpAddress(Post post, String ipAddress);
    PostView findByPostAndIpAddress(Post post, String ipAddress);
}
