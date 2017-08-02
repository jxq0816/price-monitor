package com.inter3i.monitor.controller;

import com.inter3i.monitor.business.IndexService;
import com.inter3i.monitor.common.ApplicationSessionFactory;
import com.inter3i.monitor.entity.IndexData;
import com.inter3i.monitor.entity.account.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/22 16:16
 */
@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;
    /**
     * 首页
     */
    @RequestMapping(value = "/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("index");
        Account account = ApplicationSessionFactory.getAccount(request);
        mv.addObject("account", account);
        //得到首页面显示的数据
        List<IndexData> indexDatas =indexService.getIndexData();
        int warningNumber = 0;
        int productNumber = 0;
        int userNumber = 0;
        int activeNumber = 0;
        for (IndexData indexData:indexDatas){
            if ("warning_number".equals(indexData.getEnglishName())){
                warningNumber = indexData.getNumber();
            }else if("product_number".equals(indexData.getEnglishName())){
                productNumber = indexData.getNumber();
            }else if("user_number".equals(indexData.getEnglishName())){
                userNumber = indexData.getNumber();
            }else if("active_number".equals(indexData.getEnglishName())){
                activeNumber = indexData.getNumber();
            }
        }
        mv.addObject("warningNumber",warningNumber);
        mv.addObject("productNumber",productNumber);
        mv.addObject("userNumber",userNumber);
        mv.addObject("activeNumber",activeNumber);
        return mv;
    }

}