package com.example.flutter_comm.api;

import com.example.flutter_comm.config.TokenProvider;
import com.example.flutter_comm.dto.user.GetOtpDto;
import com.example.flutter_comm.dto.user.LoginEmailPasswordDto;
import com.example.flutter_comm.dto.user.MyInfoDto;
import com.example.flutter_comm.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.flutter_comm.config.constant.routes.apiv1.AuthRoutes.*;
import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

@RestController
@RequestMapping(PREFIX_API_V1)
@RequiredArgsConstructor
public class AuthController {
    UserServiceImpl userService;
    private TokenProvider jwtTokenUtil;
    AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserServiceImpl userService, TokenProvider jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = PREFIX_MY_INFO, method = RequestMethod.GET)
    public ResponseEntity<?> myInfo() {
        MyInfoDto user = userService.myInfo();
        return  ResponseEntity.ok(user);
    }
    @RequestMapping(value = PREFIX_GET_OTP, method = RequestMethod.POST)
    public ResponseEntity<?> getOtp(@RequestBody GetOtpDto getOtpDto){
        return ResponseEntity.ok(userService.getOtp(getOtpDto));
    }

    @RequestMapping(value = PREFIX_LOGIN_OTP, method = RequestMethod.POST)
    public ResponseEntity<?> loginWithOtp(@RequestBody LoginEmailPasswordDto loginEmailPasswordDto){
        return ResponseEntity.ok(userService.loginWithOTP(loginEmailPasswordDto));
    }
}
