package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.report.ReportSaveDto;
import com.example.flutter_comm.entity.my_enum.PostType;
import org.springframework.data.domain.Page;

public interface AppService {
    Page<PostGetDto> searchPost(String keyword, PostType type, int page, int size);
    boolean reportSave(ReportSaveDto reportSaveDto);
}
