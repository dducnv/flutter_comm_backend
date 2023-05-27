package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Tag;
import com.example.flutter_comm.entity.TopicInterest;
import com.example.flutter_comm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TopicInterestRepository extends JpaRepository<TopicInterest, Long> {
    List<TopicInterest> findAllByTagInAndUser(List<Tag> tagList, User user);

    List<TopicInterest> findAllByTagIn(List<Tag> tagList);
}
