package com.example.flutter_comm.api.admin;

import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.service.impl.UserManageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.flutter_comm.config.constant.routes.apiv1.AdminRoutes.PREFIX_ADMIN;
import static com.example.flutter_comm.config.constant.routes.apiv1.AdminRoutes.PREFIX_USER_LIST;

@RestController
@RequestMapping(value = PREFIX_ADMIN)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class UserManageController {
    @Autowired
    UserManageServiceImpl userManageService;

    @RequestMapping(value = PREFIX_USER_LIST,method = RequestMethod.GET)
    public ResponseEntity<?> listUser() {
        List<User> user = userManageService.findAllUser();
        return ResponseEntity.ok(user);
    }
}
