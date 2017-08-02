package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.IndexData;

import java.util.List;

/*
 * DESCRIPTION : 电商价格监测-首页Service
 * USER : liuxiaolei
 * DATE : 2017/6/21 11:238
 */
public interface IndexService {

    /**
     * 定时计算首页面显示的的数据(预警次数、监测产品、注册用户、监测活动)
     *
     */
    void calculationIndexData();

    /**
     * 定时计算首页面显示的的数据(预警次数、监测产品、注册用户、监测活动)
     *@return List<IndexData> 首页数据集合
     */
    List<IndexData> getIndexData();
}
