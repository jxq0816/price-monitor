package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.PriceChangeRatio;

import java.util.List;

/**
 * 计算商品类型的价格降价趋势
 * author :   yu
 * date   :   2017/6/14.
 */
public interface PriceTrendService {
    /**
     * 计算价格降价趋势
     *
     * @return
     */
    Boolean handlePriceTrend();

    /**
     * 计算价格降价趋势
     *
     * @param goodsName 商品名
     * @return
     */
    Boolean handlePriceTrend(String goodsName);

    /**
     * 查询指定条数的商品价格
     *
     * @param goodsName
     * @param selNum
     * @return
     */
    List getPriceTrendData(String goodsName, int selNum);

    /**
     * 补全商品下面的价格趋势
     *
     * @param priceChangeRatioDatas 数据结果集
     * @return 返回处理的String结果
     */
    String complementPriceTrend(List<PriceChangeRatio> priceChangeRatioDatas, int selNum);
}
