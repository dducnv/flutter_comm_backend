package com.example.flutter_comm.config.constant.routes.apiv1;

import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

public class AuthRoutes {
    public static final String PREFIX_MY_INFO = "/me";
    private static final String PREFIX_OTP = "/otp";
    public static final String PREFIX_GET_OTP ="/otp/get";
    public static final String PREFIX_LOGIN_OTP ="/otp/login";
    public static final String MY_INFO_PAM = PREFIX_API_V1.concat(PREFIX_MY_INFO);
    public static final String OTP_AUTH_PAM = PREFIX_API_V1.concat(PREFIX_OTP.concat("/**"));

}
