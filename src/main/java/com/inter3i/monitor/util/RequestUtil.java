package com.inter3i.monitor.util;

import javax.servlet.http.HttpServletRequest;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/20 18:16
 */
public class RequestUtil {

    public static String getHost(HttpServletRequest request){
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
