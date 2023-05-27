package com.example.flutter_comm.api.tag;

import com.example.flutter_comm.service.impl.TagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.flutter_comm.config.constant.routes.apiv1.AdminRoutes.PREFIX_TAG;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class TagApiController {
    @Autowired
    TagServiceImpl tagService;
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_TAG, method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam(name = "q", defaultValue = "") String keyword) {
        return  ResponseEntity.ok(tagService.getList(keyword));
    }

}
