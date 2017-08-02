package com.inter3i.monitor.common;

import com.inter3i.monitor.entity.account.Account;
import com.inter3i.monitor.entity.account.Authorization;

import javax.servlet.http.HttpServletRequest;


/*
 * DESCRIPTION : 所有的缓存信息，如果需要缓存信息，请通过在此类中定义方法来调用，即保证所有的key都是在这个类中维护
 * USER : zhouhui
 * DATE : 2017/5/11 17:33
 */
public class ApplicationSessionFactory {


    public static final String SESSION_KEY_ACCOUNT = "account";
    public static final String SESSION_KEY_AUTHORIZATION = "authorization";
    public static final String SESSION_KEY_ORIGINAL_URL= "originalUrl";




    /**
     * 清除redis中的session信息
     */
    public static void clearSession(HttpServletRequest request){
        setAccount(request,null);
        setAuthorization(request,null);
        setOriginalUrl(request,null);
    }



    /**
     * 获取登录的用户信息
     */
    public static Account getAccount(HttpServletRequest request){
        Object accountObject = request.getSession().getAttribute(SESSION_KEY_ACCOUNT);
        if(accountObject != null){
            return (Account)accountObject;
        }
        return null;
    }

    /**
     * 获取登录的用户信息
     */
    public static String getAccountId(HttpServletRequest request){
        Account account = getAccount(request);
        if(account != null){
            return account.getAccountid();
        }
        return null;
    }

    /**
     * 获取用户信息
     */
    public static void setAccount(HttpServletRequest request, Account account) {
        request.getSession().setAttribute(SESSION_KEY_ACCOUNT,account);
        request.getSession().setMaxInactiveInterval(Constant.DEFAULT_EXPIRE_SECONDS);
    }

    /**
     * 获取登录的权限信息
     */
    public static Authorization getAuthorization(HttpServletRequest request){
        Object authorizationObject = request.getSession().getAttribute(SESSION_KEY_AUTHORIZATION);
        if(authorizationObject != null){
            return (Authorization)authorizationObject;
        }
        return null;
    }

    /**
     * 获取用户权限
     */
    public static void setAuthorization(HttpServletRequest request, Authorization authorization){
        request.getSession().setAttribute(SESSION_KEY_AUTHORIZATION,authorization);
        request.getSession().setMaxInactiveInterval(Constant.DEFAULT_EXPIRE_SECONDS);
    }


    public static void setOriginalUrl(HttpServletRequest request, String originalUrl) {
        request.getSession().setAttribute(SESSION_KEY_ORIGINAL_URL,originalUrl);
        request.getSession().setMaxInactiveInterval(Constant.DEFAULT_EXPIRE_SECONDS);
    }

    public static String getOriginalUrl(HttpServletRequest request) {
        Object authorizationObject = request.getSession().getAttribute(SESSION_KEY_ORIGINAL_URL);
        if(authorizationObject != null){
            return (String)authorizationObject;
        }
        return "/";
    }
}
