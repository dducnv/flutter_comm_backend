package com.example.flutter_comm.advice;

import com.example.flutter_comm.dto.ApiResDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationException {
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResDto handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String,String> errMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return ApiResDto
                .builder()
                .message("Lỗi dữ liệu!")
                .data(errMap)
                .build();
    }
}
