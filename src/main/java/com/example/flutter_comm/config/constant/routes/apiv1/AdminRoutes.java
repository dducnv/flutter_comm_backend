package com.example.flutter_comm.config.constant.routes.apiv1;

import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

public class AdminRoutes {
    public static final String PREFIX_ADMIN = PREFIX_API_V1 + "/admin";

    public static final String PREFIX_USER_LIST = "/users";
    public static final String PREFIX_ADMIN_LIST = "/admins";

    public static final String PREFIX_ROLE_LIST = "/roles";

    public static final String PREFIX_TAG = "/tags";
    public static final String PREFIX_SET_ROLE_FOR_USER = "/{id}/set-roles";

}
