package com.inter3i.monitor.business;

import java.util.Date;
import java.util.List;

/**
 * 计算商品类型下的所有商品的降价趋势及降价幅度
 * author :   yu
 * date   :   2017/6/14.
 */
public interface BrandAvgChangeService {
    /**
     * 计算所有商品的品牌下的价格变化趋势
     *
     * @return 返回结果
     */
    Boolean handBrandAvgChange();

    /**
     * 计算商品的品牌下的价格变化趋势
     *
     * @param goodsName 商品名
     * @return 返回结果
     */
    Boolean handBrandAvgChange(String goodsName);

    /**
     * 得到品牌趋势结果表中的最大时间戳
     *
     * @param goodsName 商品名
     * @return 查询的数据结果集
     */
    List getMaxBrandAvgChange(String goodsName);

    /**
     * 查询商品下的价格趋势
     *
     * @param num       查询条数
     * @param goodsName 商品名
     * @param time      查询时间
     * @return 查询的数据结果集
     */
    List getBrandAvgRatioRank(int num, String goodsName, Date time);
}
