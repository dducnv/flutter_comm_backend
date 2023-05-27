package com.example.flutter_comm.dto.report;

import com.example.flutter_comm.entity.my_enum.ReasonReport;
import com.example.flutter_comm.entity.my_enum.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportSaveDto {
    private ReportType reportType;
    private ReasonReport reasonReport;
    private UUID uuidOfType;
    private String message;
}
