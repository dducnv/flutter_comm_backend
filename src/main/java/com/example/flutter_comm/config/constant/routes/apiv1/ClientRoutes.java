package com.example.flutter_comm.config.constant.routes.apiv1;

import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

public class ClientRoutes {
    public static final String PREFIX_USER_DETAILS = "/user/{username}";
    public static final String USER_DETAILS_PAM = PREFIX_API_V1.concat("/user/**");

    public static final String PREFIX_POST_API = "/post";

    public static final String PREFIX_POST_REACTION = "/post/{slug}/reactions";

    public static final String PREFIX_SEARCH_PATH = "/search";
}
