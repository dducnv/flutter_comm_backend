package com.example.flutter_comm.api.tag;

import com.example.flutter_comm.dto.ApiResDto;
import com.example.flutter_comm.service.impl.TagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.PREFIX_TAG;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class TagApiController {
    TagServiceImpl tagService;

    @Autowired
    public TagApiController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }

    @RequestMapping(value = PREFIX_TAG, method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam(name = "q", defaultValue = "") String keyword) {
        return ResponseEntity.ok(ApiResDto.builder()
                .message("Lấy danh sách tag thành công!")
                .httpStatus(HttpStatus.OK)
                .data(tagService.getList(keyword))
                .build());
    }

}
