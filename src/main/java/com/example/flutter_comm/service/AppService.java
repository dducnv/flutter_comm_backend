package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.post.PostGetDto;
import com.example.flutter_comm.dto.report.ReportSaveDto;

import java.util.List;

public interface AppService {
    List<PostGetDto> searchPost(String keyword);
    boolean reportSave(ReportSaveDto reportSaveDto);
}
