package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.Commodity;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/15 15:15
 */
public interface CommodityService {


    /**
     * 删除一天时间内同一个page_url的数据，保留最新的一条
     */
    void deleteDuplicate();


    /**
     * 删除成交价为空的数据
     *
     * @param dataFlag
     */
    void deleteNullStickPrice(String dataFlag);

    /**
     * 获取指定时间段的数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<Commodity> getDatas(Long startTime, Long endTime);
}
