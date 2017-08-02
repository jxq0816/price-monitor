package com.inter3i.monitor.controller;

import com.inter3i.monitor.business.BrandAvgChangeService;
import com.inter3i.monitor.business.PriceChangeRankService;
import com.inter3i.monitor.business.TaskManageService;
import com.inter3i.monitor.business.impl.PriceTrendServicelmpl;
import com.inter3i.monitor.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by koreyoshi on 2017/6/20.
 */
@Controller
@RequestMapping("/industryList")
public class IndustryListController {
    @Autowired
    TaskManageService taskManageService;

    @Autowired
    PriceChangeRankService priceChangeRankService;
    @Autowired
    BrandAvgChangeService brandAvgChangeService;

    @Autowired
    PriceTrendServicelmpl priceTrendServicelmpl;


    /**
     * 获取行业榜单商品列表数据（头部）
     *
     * @return ModelAndView 返回页面
     */
    @RequestMapping(value = "/getCategoryName")
    public ModelAndView getCategoryName() throws Exception {
        ModelAndView mv = new ModelAndView("/monitorfile/industryList/industry_list");
        //查询所有的商品名称
        List<Category> categories = taskManageService.getAllCategory();
        mv.addObject("categories", categories);
        return mv;
    }

    /**
     * 返回三个显示列表需要的数据
     *
     * @param goodsName 商品名
     * @param selNum    查询条数
     * @return 返回页面
     */
    @RequestMapping(value = "/getIndustryInfo", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    public ModelAndView getNew(@RequestParam("goodsName") String goodsName, @RequestParam("selNum") int selNum) {
        ModelAndView mv = new ModelAndView("/monitorfile/industryList/industry_info");
        //获取降价幅度排名
        List<PriceChangeRank> maxChangeRankTime = priceChangeRankService.getMaxTimeFromChangeRank(goodsName);
        List<PriceChangeRank> priceChangeRank = null;
        if (maxChangeRankTime.size() > 0) {
            Date time = maxChangeRankTime.get(0).getCreatedOn();
            priceChangeRank = priceChangeRankService.getChangeRank(selNum, goodsName, time);
        }

        //获取降价趋势排名
        List<PriceDropRank> MaxTimeFromChangeRatioRank = priceChangeRankService.getMaxTimeFromChangeRatioRank(goodsName);
        List<PriceDropRank> priceDropRank = null;
        if (maxChangeRankTime.size() > 0) {
            Date time = MaxTimeFromChangeRatioRank.get(0).getCreatedOn();
            priceDropRank = priceChangeRankService.getChangeRatioRank(selNum, goodsName, time);
        }
        //获取品牌趋势排名
        List<BrandAveragePrice> MaxBrandAvgTime = brandAvgChangeService.getMaxBrandAvgChange(goodsName);
        List<BrandAveragePrice> brandAvgChangeRank = null;
        if (MaxBrandAvgTime.size() > 0) {
            Date time = MaxBrandAvgTime.get(0).getCreatedOn();
            brandAvgChangeRank = brandAvgChangeService.getBrandAvgRatioRank(selNum, goodsName, time);
        }
        mv.addObject("priceChangeRank", priceChangeRank);
        mv.addObject("priceDropRank", priceDropRank);
        mv.addObject("brandAvgChangeRank", brandAvgChangeRank);
        return mv;
    }

    /**
     * 返回行业榜单商品降价趋势折线图数据
     *
     * @param goodsName 商品名
     * @param selNum    查询条数
     * @return 返回string
     */
    @RequestMapping(value = "/getPriceRatioRank", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPriceRatioRank(@RequestParam("goodsName") String goodsName, @RequestParam("selNum") int selNum) {
        String res = null;
        List<PriceChangeRatio> priceChangeRatioDatas = null;
        //获取商品的价格趋势
        priceChangeRatioDatas = priceTrendServicelmpl.getPriceTrendData(goodsName, selNum);
        if (priceChangeRatioDatas.size() > 0) {
            //补全价格趋势
            res = priceTrendServicelmpl.complementPriceTrend(priceChangeRatioDatas,selNum);
        }
        return res;
    }
}
