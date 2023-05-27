package com.example.flutter_comm.service;

import com.example.flutter_comm.config.oauth2.user.OAuth2UserInfo;
import com.example.flutter_comm.dto.UserInfoDto;
import com.example.flutter_comm.dto.user.GetOtpDto;
import com.example.flutter_comm.dto.user.MyInfoDto;
import com.example.flutter_comm.dto.user.OtpResDto;
import com.example.flutter_comm.entity.User;
import com.example.flutter_comm.entity.my_enum.AuthProvider;


public interface UserService {
    MyInfoDto myInfo();
    UserInfoDto findByUsername(String username);
    OtpResDto getOtp(GetOtpDto getOtpDto);
    MyInfoDto toUserDTO(User user);
    User findUserByEmail(String email);
//    LoginDto.otpResDto getOtp(LoginDto.getOtpDto getOtpDto);
    User registerNewUserForSocial(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo);
}
