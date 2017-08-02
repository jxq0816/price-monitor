package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.MonitorAlertEntity;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/15 13:15
 */
public interface ApiMonitorAlertService {

    /**
     * 设置促销价信息
     *
     * @param dataFlag 数据编号
     */
    void buildPromotionPrice(String dataFlag);

    /**
     * 获取预警的数据集合
     * @param dataFlag 数据编号
     * @return
     */
    List<MonitorAlertEntity> queryMonitorAlertEntityList(String dataFlag);

    /**
     * 删除已下架的商品数据
     * @param dataFlag
     */
    void deleteOffProductData(String dataFlag);
}
