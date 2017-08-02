package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.PriceChangeRank;

import java.util.Date;
import java.util.List;

/**
 * 计算商品类型下的所有商品的降价趋势及降价幅度
 * author :   yu
 * date   :   2017/6/14.
 */
public interface PriceChangeRankService {
    /**
     * 计算所有商品的降价趋势及降价幅度
     *
     * @return 执行结果
     */
    Boolean handlePriceChangeRank();

    /**
     * 计算该商品类型的所有商品的降价趋势及降价幅度
     *
     * @param goodsName 商品名
     * @return 执行结果
     */
    Boolean handlePriceChangeRank(String goodsName);

    /**
     * 查询该商品在降价幅度结果表中的最大时间戳
     *
     * @param goodsName 商品名
     * @return 查询的结果集
     */
    List<PriceChangeRank> getMaxTimeFromChangeRank(String goodsName);

    /**
     * 查询该商品在降价趋势结果表中的最大时间戳
     *
     * @param goodsName 商品名
     * @return 查询的结果集
     */
    List getMaxTimeFromChangeRatioRank(String goodsName);

    /**
     * 查询商品在降价幅度结果表中指定时间的数据
     *
     * @param selNum    查询条数
     * @param goodsName 商品名
     * @param Time      指定时间
     * @return 查询的数据结果集
     */
    List getChangeRank(int selNum, String goodsName, Date Time);

    /**
     * 查询商品在降价趋势结果表中指定时间的数据
     *
     * @param selNum    查询条数
     * @param goodsName 商品名
     * @param Time      指定时间
     * @return 查询的数据结果集
     */
    List getChangeRatioRank(int selNum, String goodsName, Date Time);


}
