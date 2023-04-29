package com.example.flutter_comm.exception;

import com.example.flutter_comm.dto.ApiResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiFormExceptionHandler {
    @ExceptionHandler(value = {ApiFormException.class})
    public ResponseEntity<Object> handleApiFormException(ApiFormException e){
        HttpStatus badRequest = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(ApiResDto.builder()
                .message("Lỗi dữ liệu!")
                .data(apiException)
                .build(),badRequest);
    }
}
