package com.inter3i.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.inter3i.monitor.common.ApplicationSessionFactory;
import com.inter3i.monitor.common.Constant;
import com.inter3i.monitor.entity.APIResponse;
import com.inter3i.monitor.entity.account.Account;
import com.inter3i.monitor.entity.account.Authorization;
import com.inter3i.monitor.util.CookieUtil;
import com.inter3i.monitor.util.HttpUtil;
import com.inter3i.monitor.util.RequestUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.inter3i.monitor.common.Constant.ACCOUNT_SERVER_AUTO_LOGIN_URL;
import static com.inter3i.monitor.common.Constant.ACCOUNT_SERVER_LOGIN_PAGE_URL;
import static com.inter3i.monitor.common.Constant.ACCOUNT_SERVER_LOGOUT_PAGE_URL;
import static com.inter3i.monitor.common.Constant.ACCOUNT_SERVER_REGISTER_PAGE_URL;
import static com.inter3i.monitor.common.Constant.ACCOUNT_SERVER_USER_INFORMATION_URL;
import static com.inter3i.monitor.common.Constant.APPLICATION_ID;
import static com.inter3i.monitor.common.Constant.APPLICATION_SERVER_CALLBACK_URL;
import static com.inter3i.monitor.common.Constant.SESSIONID_COOKIE_NAME;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/11 11:18
 */
@Controller
@RequestMapping("/account")
public class AccountController {


    /**
     * 用户登录
     * @param request request对象
     * @param response response对象
     * @param originalUrl 用户点击登录时的页面url
     * @throws IOException 重定向异常
     */
    @RequestMapping(value="/login")
    public void login(HttpServletRequest request, HttpServletResponse response, String originalUrl) throws IOException {
        Account account = ApplicationSessionFactory.getAccount(request);
        if(account == null || StringUtils.isEmpty(originalUrl)){
            ApplicationSessionFactory.setOriginalUrl(request,originalUrl);
            String serverName = RequestUtil.getHost(request);
            String urlToRedirectTo = ACCOUNT_SERVER_LOGIN_PAGE_URL + "?callback_url=" + serverName + APPLICATION_SERVER_CALLBACK_URL + "&application_id=" + APPLICATION_ID;
            response.sendRedirect(urlToRedirectTo);
        }else {
            response.sendRedirect(originalUrl);
        }
    }

    /**
     * 单点自动登录
     * @param request request对象
     * @param originalUrl 用户自动登录时的页面
     * @return 登录ticket
     */
    @ResponseBody
    @RequestMapping(value="/autoLogin",method = RequestMethod.POST)
    public String autoLogin(HttpServletRequest request, String originalUrl) {
        String sessionID = CookieUtil.getCookie(request,SESSIONID_COOKIE_NAME);
        if(StringUtils.isNotEmpty(sessionID)){
            ApplicationSessionFactory.setOriginalUrl(request,originalUrl);
            JSONObject json = new JSONObject();
            json.put("sessionID",sessionID);
            json.put("application_id", Constant.APPLICATION_ID);
            json.put("callback_url", originalUrl);
            String responseStr = HttpUtil.doPost(ACCOUNT_SERVER_AUTO_LOGIN_URL,json);
            if(StringUtils.isNotEmpty(responseStr) && responseStr.length() == 24){
                return responseStr;
            }
        }
        return null;
    }

    /**
     * 单点自动登录
     * @param request request对象
     * @param response response对象
     * @param originalUrl 用户自动登录时的页面
     */
    @RequestMapping(value="/register")
    public void register(HttpServletRequest request, HttpServletResponse response, String originalUrl) throws IOException {
        Account account = ApplicationSessionFactory.getAccount(request);
        if(account == null || StringUtils.isEmpty(originalUrl)){
            ApplicationSessionFactory.setOriginalUrl(request,originalUrl);
            String serverName = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String urlToRedirectTo = ACCOUNT_SERVER_REGISTER_PAGE_URL + "?callback_url=" + serverName + APPLICATION_SERVER_CALLBACK_URL + "&application_id=" + APPLICATION_ID;
            response.sendRedirect(urlToRedirectTo);
        }else {
            response.sendRedirect(originalUrl);
        }
    }

    /**
     * 退出
     * @param request request对象
     * @return 退出是否成功信息
     */
    @ResponseBody
    @RequestMapping(value="/logout",method = RequestMethod.POST)
    public Map<String,Object> logout(HttpServletRequest request) {
        Map<String,Object> map = new java.util.HashMap<String,Object>();
        ApplicationSessionFactory.clearSession(request);
        String sessionID = CookieUtil.getCookie(request,SESSIONID_COOKIE_NAME);
        if(StringUtils.isNotEmpty(sessionID)){
            JSONObject json = new JSONObject();
            json.put("sessionID",sessionID);
            String responseStr = HttpUtil.doPost(ACCOUNT_SERVER_LOGOUT_PAGE_URL,json);
            if(StringUtils.isNotEmpty(responseStr)){
                JSONObject responseJson = JSONObject.parseObject(responseStr);
                map.put("success",responseJson.getBoolean("success"));
            }
        }else{
            map.put("success",true);
        }
        return map;
    }

    /**
     * 登录成功之后的回调方法
     * @param ticket 登录ticket
     * @return
     */
    @RequestMapping(value="/callback")
    public ModelAndView casCallback(String ticket){
        ModelAndView mv = new ModelAndView("/account/cas-index");
        mv.addObject("ticket",ticket);
        return mv;
    }

    /**
     * 执行登录
     * @param request request对象
     * @param ticket 登录ticket
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/executeLogin")
    public Map<String,Object> executeLogin(HttpServletRequest request,String ticket){
        Map<String,Object> map = new java.util.HashMap<String,Object>();
        map.put("success",false);
        String originalUrl = ApplicationSessionFactory.getOriginalUrl(request);
        JSONObject json = new JSONObject();
        json.put("application_id",Constant.APPLICATION_ID);
        json.put("ticket",ticket);
        String responseStr = HttpUtil.doPost(ACCOUNT_SERVER_USER_INFORMATION_URL,json);
        if(StringUtils.isNotEmpty(responseStr)){
            APIResponse APIResponse = JSONObject.toJavaObject(JSONObject.parseObject(responseStr), APIResponse.class);
            Integer statusCode = APIResponse.getStatusCode();
            if(statusCode == HttpStatus.OK.value()){
                String datas = APIResponse.getDatas();
                Authorization authorization = JSONObject.toJavaObject(JSONObject.parseObject(datas), Authorization.class);
                Account account = authorization.getAccount();
                ApplicationSessionFactory.setAccount(request,account);
                ApplicationSessionFactory.setAuthorization(request,authorization);
                map.put("url",originalUrl);
                map.put("success",true);
            }else{
                map.put("message",APIResponse.getMessage());
            }
        }else{
            map.put("message","account.inter3i.com服务器返回为空");
        }
        return map;
    }

}
