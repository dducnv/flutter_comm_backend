package com.example.flutter_comm.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResDto<T> {
    private String message;
    private HttpStatus httpStatus;
    private T data;
}
