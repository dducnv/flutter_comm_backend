package com.example.flutter_comm.api;

import com.example.flutter_comm.dto.report.ReportSaveDto;
import com.example.flutter_comm.service.impl.AppServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.flutter_comm.config.constant.routes.apiv1.ClientRoutes.PREFIX_SEARCH_PATH;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class ApiController {
    @Autowired
    AppServiceImpl appService;

    @RequestMapping(value = "/test/123", method = RequestMethod.GET)
    public ResponseEntity<?> myTest() {

        return ResponseEntity.ok("Hello Test");
    }

    @RequestMapping(value = PREFIX_SEARCH_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> search(@RequestParam(name = "q", defaultValue = "") String keyword, @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(appService.searchPost(keyword, page, 10));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseEntity<?> report(@RequestBody ReportSaveDto reportSaveDto) {
        return ResponseEntity.ok(appService.reportSave(reportSaveDto));
    }

}
