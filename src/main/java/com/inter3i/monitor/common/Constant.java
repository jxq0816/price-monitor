package com.inter3i.monitor.common;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/11 17:28
 */
public class Constant {


    /**
     * 缓存失效时间
     */
    public final static int DEFAULT_EXPIRE_SECONDS = 1 * 60 * 60;

    public final static String SESSIONID_COOKIE_NAME = "SESSIONID";
    public final static String APPLICATION_ID = "593525ceca95953160e3f24a";
//        public final static String ACCOUNT_SERVER = "http://myaccount.inter3i.com:8888/";
    private final static String ACCOUNT_SERVER = "http://account.inter3i.com";
    public final static String ACCOUNT_SERVER_LOGIN_PAGE_URL = ACCOUNT_SERVER + "/service/login";
    public final static String ACCOUNT_SERVER_REGISTER_PAGE_URL = ACCOUNT_SERVER + "/service/register";
    public final static String ACCOUNT_SERVER_USER_INFORMATION_URL = ACCOUNT_SERVER + "/service/ticket";
    public final static String ACCOUNT_SERVER_LOGOUT_PAGE_URL = ACCOUNT_SERVER + "/service/logout";
    public final static String ACCOUNT_SERVER_FEEDBACK_URL = ACCOUNT_SERVER + "/service/feedback";
    public final static String ACCOUNT_SERVER_AUTO_LOGIN_URL = ACCOUNT_SERVER + "/service/autoLogin";

    public final static String APPLICATION_SERVER_CALLBACK_URL = "/account/callback";

    public final static String INTER3I_DOMAIN = ".inter3i.com";

    public final static String AUTHORITY_TYPE_MENU = "menu";

    public final static String AUTHORITY_TYPE_RESOURCE = "resource";


}
