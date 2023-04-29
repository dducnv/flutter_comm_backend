package com.example.flutter_comm.api;

import com.example.flutter_comm.service.impl.AppServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.PREFIX_SEARCH_PATH;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class ApiController {
    @Autowired
    AppServiceImpl appService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> myTest() {

        return ResponseEntity.ok("Hello Test");
    }

    @RequestMapping(value = PREFIX_SEARCH_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> search(@RequestParam(name = "q", defaultValue = "") String keyword) {
        return ResponseEntity.ok(appService.searchPostUseElasticsearch(keyword));
    }



}
