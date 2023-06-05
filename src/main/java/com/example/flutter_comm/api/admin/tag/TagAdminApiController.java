package com.example.flutter_comm.api.admin.tag;

import com.example.flutter_comm.dto.tag.TagGetDto;
import com.example.flutter_comm.service.impl.TagServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.flutter_comm.config.constant.routes.apiv1.AdminRoutes.PREFIX_ADMIN;
import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.PREFIX_TAG;

@RestController
@RequestMapping(PREFIX_ADMIN)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TagAdminApiController {
    TagServiceImpl tagService;

    @Autowired
    public TagAdminApiController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }
//    @RequestMapping(value = PREFIX_TAG, method = RequestMethod.GET)
//    public ResponseEntity<?> getAll() {
//        return  ResponseEntity.ok(tagService.getList());
//    }


    @RequestMapping(value = PREFIX_TAG, method = RequestMethod.POST)
    public ResponseEntity<?> savePost(@RequestBody TagGetDto tagGetDto) {
        return  ResponseEntity.ok(tagService.save(tagGetDto));
    }
}
