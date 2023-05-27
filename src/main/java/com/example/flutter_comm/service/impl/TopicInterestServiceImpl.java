package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.Tag;
import com.example.flutter_comm.entity.TopicInterest;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.repository.TopicInterestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicInterestServiceImpl {
    private final TopicInterestRepository topicInterestRepository;

    public TopicInterestServiceImpl(TopicInterestRepository topicInterestRepository) {
        this.topicInterestRepository = topicInterestRepository;
    }

    public void updateTopicInterest(Post post, User user) {
        Set<Tag> tags = post.getTags();
        List<Tag> tagList = new ArrayList<>(tags);
        List<TopicInterest> topicInterests = topicInterestRepository.findAllByTagInAndUser(tagList, user);
        for (Tag tag : tags) {
            Optional<TopicInterest> topicInterest = topicInterests.stream().filter(it -> it.getTag() == tag).findFirst();

            if (topicInterest.isPresent()) {
                TopicInterest topicInterestUpdate = topicInterest.get();
                topicInterestUpdate.setViewCount(topicInterestUpdate.getViewCount() + 1);
                topicInterestRepository.save(topicInterestUpdate);
            } else {
                TopicInterest newTopicInterest = new TopicInterest();
                newTopicInterest.setUser(user);
                newTopicInterest.setTag(tag);
                newTopicInterest.setViewCount(1);
                topicInterestRepository.save(newTopicInterest);
            }
        }
    }

    public void filterTopicForUser(User user) {
        LocalDateTime limitDate = LocalDateTime.now().minusDays(15);
        List<TopicInterest> topicInterestsBeforeDay = user.getTopicInterest().stream().filter(it -> it.getUpdatedAt().isBefore(limitDate)).collect(Collectors.toList());
        if (!topicInterestsBeforeDay.isEmpty()) {
            topicInterestRepository.deleteAll(topicInterestsBeforeDay);
        }
        List<TopicInterest> top3 = user.getTopicInterest().stream()
                .sorted(Comparator.comparing(TopicInterest::getViewCount).reversed()
                        .thenComparing(TopicInterest::getUpdatedAt).reversed())
                .limit(3)
                .collect(Collectors.toList());
        List<TopicInterest>  doneFilter =   user.getTopicInterest().stream()
                .filter(ti -> !top3.contains(ti)).collect(Collectors.toList());
        if(!doneFilter.isEmpty()){
            topicInterestRepository.deleteAll(doneFilter);
        }

    }
}
