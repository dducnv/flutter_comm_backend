package com.example.flutter_comm.entity;

import com.example.flutter_comm.entity.my_enum.ReasonReport;
import com.example.flutter_comm.entity.my_enum.ReportType;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "user_report")
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuidOfType;
    private String message;
    private ReportType reportType;
    private ReasonReport reasonReport;
}
