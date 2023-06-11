package com.example.flutter_comm.service.impl;

import com.example.flutter_comm.dto.BlackWorkDto;
import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.report.ReportSaveDto;
import com.example.flutter_comm.entity.BlackWord;
import com.example.flutter_comm.entity.Category;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.UserReport;
import com.example.flutter_comm.entity.my_enum.PostType;
import com.example.flutter_comm.repository.BlackWordRepository;
import com.example.flutter_comm.repository.PostRepository;
import com.example.flutter_comm.repository.ReportRepository;
import com.example.flutter_comm.service.AppService;
import com.example.flutter_comm.utils.BlackWordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    PostRepository postRepository;
    CategoryServiceImpl categoryService;
    BlackWordRepository blackWordRepository;
    UserServiceImpl userService;
    PostServiceImpl postService;
    ReportRepository reportRepository;

    @Autowired
    public AppServiceImpl(PostRepository postRepository, BlackWordRepository blackWordRepository, ReportRepository reportRepository, UserServiceImpl userService, PostServiceImpl postService, CategoryServiceImpl categoryService) {
        this.postRepository = postRepository;
        this.blackWordRepository = blackWordRepository;


        this.reportRepository = reportRepository;
        this.userService = userService;
        this.postService = postService;
        this.categoryService = categoryService;
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
    public Page<PostGetDto> searchPost(String keyword, PostType type, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        List<Category> categoryList = categoryService.categorySeedList();
        Category categoryFilter ;
        switch (type){
            case questions:
                categoryFilter = categoryList.get(0);
                break;
            case discussion:
                categoryFilter = categoryList.get(2);
                break;
            case job:
                categoryFilter = categoryList.get(3);
                break;
            default:
                categoryFilter =  categoryList.get(1);
        }
        return postRepository.searchPort(keyword,categoryFilter,pageable).map(it-> postService.toPostGetDto(it));
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
