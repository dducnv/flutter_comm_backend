package com.example.flutter_comm.api.admin;

import com.example.flutter_comm.service.impl.RoleServiceImpl;;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.flutter_comm.config.constant.routes.apiv1.AdminRoutes.*;

@RestController
@RequestMapping(value = PREFIX_ADMIN)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class RoleController {
    @Autowired
    RoleServiceImpl roleService;
    @RequestMapping(value = PREFIX_ROLE_LIST,method = RequestMethod.GET)
    public ResponseEntity<?> listRole() {
        return ResponseEntity.ok(roleService.findAll());
    }
}
