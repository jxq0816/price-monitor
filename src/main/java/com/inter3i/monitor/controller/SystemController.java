package com.inter3i.monitor.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URLDecoder;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/8 19:24
 */
@Controller
@RequestMapping("/system")
public class SystemController {


    /**
     * 系统统一的错误提示页面
     * @param message 错误提示信息
     * @return 错误页面
     * @throws IOException 编码异常
     */
    @RequestMapping(value="/error")
    public ModelAndView error(String message) throws IOException {
        ModelAndView mv = new ModelAndView("/system/error");
        mv.addObject("message", ((StringUtils.isNotBlank(message)) ? URLDecoder.decode(message,"utf-8") : "系统错误"));
        return mv;
    }

}
