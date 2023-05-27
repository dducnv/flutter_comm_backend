package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<UserReport, Long> {
}
