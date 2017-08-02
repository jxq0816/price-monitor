package com.inter3i.monitor.component;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.DispatcherType;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/9 16:22
 */
@org.springframework.stereotype.Component
public class Component {

    @Bean
    public FilterRegistrationBean sitemeshFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new com.opensymphony.sitemesh.webapp.SiteMeshFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean inter3iFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new com.inter3i.monitor.component.Inter3iFilter());
        registration.setDispatcherTypes(DispatcherType.FORWARD,DispatcherType.INCLUDE,DispatcherType.REQUEST);
        registration.addUrlPatterns("/*");
        return registration;
    }


    @Bean
    public ServletRegistrationBean getVelocityServlet(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new com.inter3i.monitor.component.MyVelocityDecoratorServlet());
        registrationBean.addUrlMappings("*.vm");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
}
