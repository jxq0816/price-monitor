package com.inter3i.monitor.component;

import com.inter3i.monitor.business.BrandAvgChangeService;
import com.inter3i.monitor.business.IndexService;
import com.inter3i.monitor.business.PriceChangeRankService;
import com.inter3i.monitor.business.PriceTrendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

/**
 * Created by koreyoshi on 2017/6/13.
 */

@org.springframework.stereotype.Component("taskScheduler")
@Primary
public class ScheduleJob {
    @Autowired
    private PriceTrendService priceTrendService;

    @Autowired
    private PriceChangeRankService priceChangeRankService;

    @Autowired
    private BrandAvgChangeService brandAvgChangeService;

    @Autowired
    private IndexService indexService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

    /**
     * 定时计算价格趋势计算
     */
    public void priceTrend() {
        logger.info("in priceTrend : the job  is start ! the current time is:[" + System.currentTimeMillis() + "]");
        try {
            Boolean res = priceTrendService.handlePriceTrend();
            logger.info("in priceTrend : the result is:" + res);
            logger.info("in priceTrend : the job is end.");
        } catch (Exception e) {
            logger.error("in priceTrend : the job error is:", e);
            e.printStackTrace();
        }
    }

    /**
     * 定时计算价格变化排名 && 产品价格降幅
     */
    public void priceChangeRank() {
        logger.info("in priceChangeRank : the job  is start ! the current time is:[" + System.currentTimeMillis() + "]");
        try {
            Boolean res = priceChangeRankService.handlePriceChangeRank();
            logger.info("in priceChangeRank : the result is:" + res);
            logger.info("in priceChangeRank : the job is end.");
        } catch (Exception e) {
            logger.error("in priceChangeRank : the job error is:", e);
            e.printStackTrace();
        }
    }

    /**
     * 定时计算品牌均价变化
     */
    public void brandAvgChange() {
        logger.info("in brandAvgChange : the job  is start ! the current time is:[" + System.currentTimeMillis() + "]");
        try {
            Boolean res = brandAvgChangeService.handBrandAvgChange();
            logger.info("in brandAvgChange : the result is:" + res);
            logger.info("in brandAvgChange : the job is end.");
        } catch (Exception e) {
            logger.error("in brandAvgChange : the job error is:", e);
            e.printStackTrace();
        }
    }

    /**
     * 定时计算首页面显示的的数据(预警次数、监测产品、注册用户、监测活动)
     */
    public void indexData() {
        try {
            indexService.calculationIndexData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
