package com.example.flutter_comm.service;

import com.example.flutter_comm.dto.report.ReportSaveDto;
import com.example.flutter_comm.entity.Post;

import java.util.List;

public interface AppService {
    List<Post> searchPost(String keyword);
    boolean reportSave(ReportSaveDto reportSaveDto);
}
