package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.report.ReportSaveDto;
import org.springframework.data.domain.Page;

public interface AppService {
    Page<PostGetDto> searchPost(String keyword, int page, int size);
    boolean reportSave(ReportSaveDto reportSaveDto);
}
