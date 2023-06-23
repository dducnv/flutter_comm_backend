package com.example.flutter_comm.config.constant.routes.apiv1;

import static com.example.flutter_comm.config.constant.routes.apiv1.MainRoutes.PREFIX_API_V1;

public class ClientRoutes {
    public static final String PREFIX_USER_DETAILS = "/user/{username}";
    public static final String USER_DETAILS_PAM = PREFIX_API_V1.concat("/user/**");


    public static final String PREFIX_SEARCH_PATH = "/search";
    public static final String SEARCH_PAM = PREFIX_API_V1.concat(PREFIX_SEARCH_PATH+"/**");

    public static final String PREFIX_TAG = "/tags";
    //post (param: page, type<posts,questions,discussion,job>, sort<all, suggest>)
    public static final String PREFIX_POST_API = "/posts"; // get(get all), post (add new), delete( remove)
    public static final String PREFIX_GET_POST_UN_PUBLIC_API = "/posts/un_public"; // get(get all), post (add new), delete( remove)
    public static final String PREFIX_POST_DETAILS_API = "/posts/{slug}/details"; //get(detail by slug)
    public static final String PREFIX_POST_UPDATE_API = "/posts/{post_uuid}"; //put(update)
    public static final String PREFIX_POST_PUBLIC_API = "/posts/{post_uuid}/public"; //put(update)
    public static final String PREFIX_POST_UN_PUBLIC_API = "/posts/{post_uuid}/un_public"; //put(update)

    //PAM
    public static final String POSTS_GET_ALL_PAM = PREFIX_API_V1.concat(PREFIX_POST_API);
    public static final String POSTS_GET_DETAILS_ALL_PAM = PREFIX_API_V1.concat(PREFIX_POST_API+"/**/details");
    //comment
    public static final String PREFIX_COMMENT_API = "/comments";
    public static final String PREFIX_COMMENT_PARENT_API = "/{post_uuid}/comments";
    public static final String PREFIX_COMMENT_GET_MORE_API = "/{post_uuid}/comments/more";
    public static final String PREFIX_COMMENT_REPLY_GET_MORE_API = "/{post_uuid}/comments/{parent_uuid}/reply/more";
    public static final String PREFIX_COMMENT_REPLY_GET_API = "/{post_uuid}/comments/{parent_uuid}/reply";
    public static final String PREFIX_COMMENT_REPLY_API = "/{post_uuid}/comments/reply";
    public static final String PREFIX_COMMENT_DETAILS_API = "/comments/{uuid}/details";
    public static final String PREFIX_COMMENT_UPDATE_API = "/comments/{comment_uuid}";
    //PAM

    public static final String COMMENTS_OF_POST_GET_ALL_PAM = PREFIX_API_V1.concat("/**/"+PREFIX_COMMENT_API);
    public static final String COMMENTS_OF_POST_GET_MORE_PAM = PREFIX_API_V1.concat("/**/"+PREFIX_COMMENT_API+"/more");
    public static final String COMMENTS_REPLY_OF_POST_GET_MORE_PAM = PREFIX_API_V1.concat("/**/"+PREFIX_COMMENT_API+"/reply/more");
    public static final String COMMENTS_SEE_MORE_PAM= PREFIX_API_V1.concat("/**/"+PREFIX_COMMENT_API+"/**/reply");
    public static final String COMMENTS_SEE_DETAILS_PAM= PREFIX_API_V1.concat(PREFIX_COMMENT_API+"/**/details");


    //reaction
    public static final String PREFIX_POST_REACTION = "/posts/{post_uuid}/reactions";
    public static final String PREFIX_COMMENT_REACTION = "/comments/{comment_uuid}/reactions";

    //PAM
    public static final String POST_REACTIONS_GET_PAM = PREFIX_API_V1.concat(PREFIX_POST_API+"/**/reactions");

}
