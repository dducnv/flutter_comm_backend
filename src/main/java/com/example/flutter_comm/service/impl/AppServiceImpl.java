package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.BlackWorkDto;
import com.example.flutter_comm.dto.report.ReportSaveDto;
import com.example.flutter_comm.entity.BlackWord;
import com.example.flutter_comm.entity.Post;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.UserReport;
import com.example.flutter_comm.repository.BlackWordRepository;
import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.repository.ReportRepository;
import com.example.flutter_comm.service.AppService;
import com.example.flutter_comm.utils.BlackWordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    PostRepository postRepository;
    BlackWordRepository blackWordRepository;
    UserServiceImpl userService;
    ReportRepository reportRepository;

    @Autowired
    public AppServiceImpl(PostRepository postRepository, BlackWordRepository blackWordRepository, ReportRepository reportRepository, UserServiceImpl userService) {
        this.postRepository = postRepository;
        this.blackWordRepository = blackWordRepository;


        this.reportRepository = reportRepository;
        this.userService = userService;
    }
    @Cacheable(value = "black_words")
    public List<BlackWord> blackWordList() {
        return blackWordRepository.findAll();
    }

    public BlackWorkDto blackWorkFilter(String inputText) {
        BlackWordFilter blackWordFilter = new BlackWordFilter();
        blackWordFilter.start(blackWordList());
        if (inputText.isEmpty()) {
            return null;
        }
        return blackWordFilter.filterBadWords(inputText);
    }

    ;

    @Override
    public List<Post> searchPost(String keyword) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    @Override
    public boolean reportSave(ReportSaveDto reportSaveDto) {
        User user = userService.getUserFromToken();
        UserReport userReport = UserReport.builder()
                .user(user)
                .reportType(reportSaveDto.getReportType())
                .reasonReport(reportSaveDto.getReasonReport())
                .uuidOfType(reportSaveDto.getUuidOfType())
                .message(reportSaveDto.getMessage())
                .build();
        reportRepository.save(userReport);
        return true;
    }
}
