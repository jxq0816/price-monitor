package com.inter3i.monitor.util;

import com.inter3i.monitor.common.Constant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/19 11:09
 */
public class CookieUtil {

    public static void addCookie(String name, String value, int maxage, String path, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxage);
        cookie.setPath(path);
        cookie.setDomain(Constant.INTER3I_DOMAIN);
        response.addCookie(cookie);
    }

    public static void addCookie(String name, String value, int maxage, HttpServletResponse response)
    {
        addCookie(name, value, maxage, "/", response);
    }

    public static String getCookie(HttpServletRequest request, String name)
    {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(name)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }
}
