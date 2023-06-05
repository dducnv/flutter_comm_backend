package com.example.flutter_comm.config;

import com.example.flutter_comm.entity.PostView;
import com.example.flutter_comm.repository.PostViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class CountJob {
    PostViewRepository postViewRepository;

    @Autowired
    public CountJob(PostViewRepository postViewRepository) {
        this.postViewRepository = postViewRepository;
    }

    @Scheduled(cron = "0 0 1 * * *") // run every day at 1 AM
    public void cleanupPostViews() {
        Instant cutoff = Instant.now().minus(Duration.ofMillis(15));
        List<PostView> postViewList = postViewRepository.findAll()
                .stream().filter(it -> it.getCreatedAt().isBefore(ChronoLocalDateTime.from(cutoff))).collect(Collectors.toList());
        postViewRepository.deleteAll(postViewList);
    }

    @Scheduled(cron = "0 0 1 * * *")
    @CacheEvict(value = {"postsCategory", "postsSuggest"}, allEntries = true)
    public void clearSuggestPost() {

    }

    @Scheduled(cron = "0 0 1 3 * *")
    @CacheEvict(value = {"getCommentOfPost","posts","tags"}, allEntries = true)
    public void clearComment() {

    }
}
