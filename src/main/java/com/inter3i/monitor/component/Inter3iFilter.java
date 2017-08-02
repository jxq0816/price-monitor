package com.inter3i.monitor.component;

import com.inter3i.monitor.common.ApplicationSessionFactory;
import com.inter3i.monitor.entity.account.Account;

import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.inter3i.monitor.common.ApplicationSessionFactory.SESSION_KEY_ACCOUNT;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/11 16:25
 */
public class Inter3iFilter implements Filter{

    public static Set<String> LOGIN_LIMIT_URL_SET = new HashSet<String>();
    public static final String STATIC_FOLDER = "/static";
    static {
        LOGIN_LIMIT_URL_SET.add("/limit");
        LOGIN_LIMIT_URL_SET.add("/monitors/addmonitor");
        LOGIN_LIMIT_URL_SET.add("/monitors/getAttribute");
        LOGIN_LIMIT_URL_SET.add("/monitors/getAttribute/value");
        LOGIN_LIMIT_URL_SET.add("/monitors/getAttribute/isTaskNameExists");
        LOGIN_LIMIT_URL_SET.add("/monitors/getAttribute/isTaskConditionExists");
        LOGIN_LIMIT_URL_SET.add("/monitors/getAttribute/saveTask");
        LOGIN_LIMIT_URL_SET.add("/monitors/taskConfiguration");
        LOGIN_LIMIT_URL_SET.add("/realtime/category/list");
        LOGIN_LIMIT_URL_SET.add("/realtime/last/task");
        LOGIN_LIMIT_URL_SET.add("/realtime/task");
        LOGIN_LIMIT_URL_SET.add("/task/child");
        LOGIN_LIMIT_URL_SET.add("/task/child/detail");
        LOGIN_LIMIT_URL_SET.add("/task/child/detail/more");
        LOGIN_LIMIT_URL_SET.add("/task/child/detail/more/page");
        LOGIN_LIMIT_URL_SET.add("/industryList/getCategoryName");
        LOGIN_LIMIT_URL_SET.add("/industryList/getIndustryInfo");


    }

    private void setAttribute2Request(HttpServletRequest request, HttpServletResponse response){
        Account account = ApplicationSessionFactory.getAccount(request);
        request.setAttribute(SESSION_KEY_ACCOUNT,account);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        if(!uri.startsWith(STATIC_FOLDER)){

            setAttribute2Request(request,response);

            Set<String> limitUrlSet = null;
            if(ApplicationSessionFactory.getAuthorization(request) != null && CollectionUtils.isNotEmpty(ApplicationSessionFactory.getAuthorization(request).getUnauthorizedMenuUrlSet())){
                limitUrlSet = ApplicationSessionFactory.getAuthorization(request).getUnauthorizedMenuUrlSet();
                LOGIN_LIMIT_URL_SET.addAll(ApplicationSessionFactory.getAuthorization(request).getUnauthorizedMenuUrlSet());
            }

            if(isLimit(uri,limitUrlSet)){
                response.sendRedirect("/system/error?message=" + URLEncoder.encode("您没有权限访问该页面","utf-8"));
            }else {
                if(isLimit(uri,LOGIN_LIMIT_URL_SET)){
                    Account account = ApplicationSessionFactory.getAccount(request);
                    if(account == null){
                        response.sendRedirect("/");
                    }else{
                        chain.doFilter(request, response);
                    }
                }else {
                    chain.doFilter(request, response);
                }
            }
        }else{
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {

    }

    private boolean isLimit(String uri,Set<String> limitUrlList){
        boolean isLimitPage = false;
        if(CollectionUtils.isNotEmpty(limitUrlList)){
            // 判断受限制页面
            for (String url : limitUrlList) {
                if (uri.indexOf(url) >= 0) {
                    isLimitPage = true;
                    break;
                }
            }
        }
        return isLimitPage;
    }
}
