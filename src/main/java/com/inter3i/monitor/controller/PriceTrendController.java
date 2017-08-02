package com.inter3i.monitor.controller;

import com.inter3i.monitor.business.impl.PriceTrendServicelmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by koreyoshi on 2017/6/15.
 */
@Controller
@RequestMapping("/priceTrend")
public class PriceTrendController {
    @Autowired
    private PriceTrendServicelmpl priceTrendServicelmpl;


    @RequestMapping(produces = "text/xml;charset=utf8", value = "/handleData", method = {RequestMethod.GET})
    @ResponseBody
    public void handlePriceTrend(@RequestParam("goodsName") String goodsName) {
        try {
            priceTrendServicelmpl.handlePriceTrend(goodsName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //// TODO: 2017/6/15 返回信息，现在会报错误信息，找不到页面
        // unable to find resource 'error.vm' in any resource loader.
    }

}
