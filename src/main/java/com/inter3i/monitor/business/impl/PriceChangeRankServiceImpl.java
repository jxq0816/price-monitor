package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.CategoryService;
import com.inter3i.monitor.business.CommodityService;
import com.inter3i.monitor.business.PriceChangeRankService;
import com.inter3i.monitor.business.ProductLibraryService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.*;
import com.inter3i.monitor.util.GetTimeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算商品类型下的所有商品的降价趋势及降价幅度
 * author :   yu
 * date   :   2017/6/14.
 */
@Service
public class PriceChangeRankServiceImpl implements PriceChangeRankService {
    @Autowired
    private JDBCBaseDao jdbcBaseDao;
    @Autowired
    private PriceTrendServicelmpl priceTrendServicelmpl;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductLibraryService productLibraryService;
    @Autowired
    private CommodityService commodityService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PriceChangeRankServiceImpl.class);

    /**
     * 计算所有商品的降价趋势及降价幅度
     *
     * @return 执行结果
     */
    public Boolean handlePriceChangeRank() {
        logger.info("in priceChangeRank : handlePriceChangeRank start");
        Boolean handleRes = true;
        List<Category> goodsNames = categoryService.getCategroyName();
        logger.info("in priceChangeRank : the CategroyNameData size is:[" + goodsNames.size() + "]");
        if (goodsNames.size() > 0) {
            for (int i = 0; i < goodsNames.size(); i++) {
                Category goodsNameData = goodsNames.get(i);
                String goodsName = goodsNameData.getCategoryName();
                handlePriceChangeRank(goodsName);
            }
        } else {
            throw new RuntimeException("handlePriceTrend error : the goodsName is null.");
        }
        return handleRes;
    }

    /**
     * 计算该商品类型的所有商品的降价趋势及降价幅度
     *
     * @param goodsName 商品名
     * @return 执行结果
     */
    public Boolean handlePriceChangeRank(String goodsName) {
        logger.info("in priceChangeRank : the goodsName is:[" + goodsName + "]");
        //从产品库中拿到对应商品所有需要监控的url
        List<ProductLibrary> datas = productLibraryService.getUrlFromLibrary(goodsName);
        logger.info("in priceChangeRank : the getUrlFromLibraryData size is:[" + datas.size() + "]");
        if (datas.size() == 0) {
            System.out.println("in priceChangeRank : the goodsName :[" + goodsName + "] in ProductLibrary is null");
            return null;
        }
        GetTimeUtil getTimeUtil = new GetTimeUtil();
        Long startTime = getTimeUtil.getLastDayStartTime(System.currentTimeMillis(), priceTrendServicelmpl.DAY_NUM);
        Long endTime = startTime + priceTrendServicelmpl.TIME_ZONE_LONG;
        //从抽取表中拿到近两天数据倒叙查询，然后以page_url为键名，放到map中
        List<Commodity> priceDatas = commodityService.getDatas(startTime, endTime);
        logger.info("in priceChangeRank : the getDatas size is:[" + priceDatas.size() + "]");
        Map parentMap = new HashMap();
        for (int i = 0; i < priceDatas.size(); i++) {
            Map childMap = new HashMap();
            Commodity priceData = priceDatas.get(i);
            String commodityPageUrl = priceData.getPageUrl();
            Double stickPrice = priceData.getStickPrice();
            String productCategory = priceData.getProductCategory();
            if (commodityPageUrl == null || commodityPageUrl.length() == 0) {
                continue;
            }
            if (stickPrice == null || stickPrice == 0) {
                continue;
            }
            //处理，把今天的数据和昨天的数据，都放到对应的page_url键名下。
            if (parentMap.containsKey(commodityPageUrl)) {
                childMap.put("stickPrice", ((Map) parentMap.get(commodityPageUrl)).get("stickPrice"));
                childMap.put("lastStickPrice", stickPrice);
                childMap.put("productCategory", productCategory);
            } else {
                childMap.put("stickPrice", stickPrice);
                childMap.put("productCategory", productCategory);
            }
            parentMap.put(commodityPageUrl, childMap);
        }
        if (parentMap.size() == 0) {
            return null;
        }
        Long lastDayLongTime = getTimeUtil.getLastDayStartTime(System.currentTimeMillis(), 1);
        String lastDayDateTime = getTimeUtil.changeLongtimeToDate(lastDayLongTime);
        DecimalFormat df = new DecimalFormat("0.00");
        //再循环从产品库中拿到的url做对比，检查map中是否存在，不存在则跳过，存在则取出来，如果昨天或者前天数据缺失也跳过
        for (int i = 0; i < datas.size(); i++) {
            ProductLibrary libraryData = datas.get(i);
            String pageUrl = libraryData.getPageUrl();
            if (!parentMap.containsKey(pageUrl)) {
                continue;
            }
            Map data = (Map) parentMap.get(pageUrl);
            if ((data.containsKey("lastStickPrice") && data.get("lastStickPrice") != null) & (data.containsKey("stickPrice") && data.get("stickPrice") != null)) {
                Double stickPrice = (Double) data.get("stickPrice");
                Double lastStickPrice = (Double) data.get("lastStickPrice");
                //判断昨天和今天的成交价，产品价格降价幅度排名，求差值。产品价格降价趋势排名,求比率。
                if (stickPrice < lastStickPrice && stickPrice > 0) {
                    logger.info("in priceChangeRank : the pageUrl is:[" + pageUrl + "],the stickPrice is:[" + stickPrice + "],the lastStrickPrice is:[" + lastStickPrice + "],the insert DateTime is:[" + lastDayDateTime + "]");
                    //产品价格变化幅度排名
                    Double DValue = lastStickPrice - stickPrice;
                    String subDValue = df.format(DValue);
                    String productCategory = (String) data.get("productCategory");
                    //查询结果表，有则update，无则insert
                    String selPriceRankSql = "SELECT id FROM price_change_rank WHERE page_url = ? AND created_on = ?  and category_name = ? ";
                    List<PriceChangeRank> isExist = jdbcBaseDao.queryList(PriceChangeRank.class, selPriceRankSql, pageUrl, lastDayDateTime, goodsName);
                    if (isExist.size() > 0) {
                        int id = isExist.get(0).getId();
                        logger.info("in priceChangeRank :  the data in  [ price_change_rank ] is existent.the id is:[" + id + "]");
                        String updPriceChangeRatioSql = "UPDATE price_change_rank set transaction_price = ? where id = ?";
                        jdbcBaseDao.executeUpdate(updPriceChangeRatioSql, subDValue, id);
                    } else {
                        logger.info("in priceChangeRank : the data in  [ price_change_rank ] is Non-existent ");
                        String insertPriceChangeRatioSql = "INSERT INTO price_change_rank(category_name,transaction_price,page_url,   product_description,created_on) VALUES (?,?,?,?,?)";
                        jdbcBaseDao.executeUpdate(insertPriceChangeRatioSql, goodsName, subDValue, pageUrl, productCategory, lastDayDateTime);
                    }
                    //产品价格降价趋势排名
                    Double dropPrice = (lastStickPrice - stickPrice) / lastStickPrice;
                    String subDValue2 = df.format(dropPrice);
                    //查询结果表，有则update，无则insert
                    String selPriceDropRankSql = "SELECT id FROM price_drop_rank WHERE page_url = ? AND created_on = ? ";
                    List<PriceDropRank> isExist1 = jdbcBaseDao.queryList(PriceDropRank.class, selPriceDropRankSql, pageUrl, lastDayDateTime);
                    if (isExist1.size() > 0) {
                        int id = isExist1.get(0).getId();
                        logger.info("in priceChangeRank :  the data in  [ price_drop_rank ] is existent.the id is:[" + id + "]");
                        String updPriceDropRatioSql = "UPDATE price_drop_rank set price_rate = ? where id = ?";
                        jdbcBaseDao.executeUpdate(updPriceDropRatioSql, subDValue2, id);
                    } else {
                        logger.info("in priceChangeRank : the data in  [ price_drop_rank ] is Non-existent ");
                        String insertPriceDropRatioSql = "INSERT INTO price_drop_rank(category_name,price_rate,page_url,   product_description,created_on) VALUES (?,?,?,?,?)";
                        jdbcBaseDao.executeUpdate(insertPriceDropRatioSql, goodsName, subDValue2, pageUrl, productCategory, lastDayDateTime);
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    /**
     * 查询该商品在降价幅度结果表(price_change_rank)中的最大时间戳
     *
     * @param goodsName 商品名
     * @return 查询的数据结果集
     */
    public List<PriceChangeRank> getMaxTimeFromChangeRank(String goodsName) {
        String selPriceRankSql = "SELECT created_on FROM price_change_rank WHERE category_name =  ? ORDER BY created_on DESC LIMIT 1";
        return jdbcBaseDao.queryList(PriceChangeRank.class, selPriceRankSql, goodsName);
    }

    /**
     * 查询该商品在 price_drop_rank 表中的最大时间戳
     *
     * @param goodsName 商品名
     * @return 查询的数据结果集
     */
    public List getMaxTimeFromChangeRatioRank(String goodsName) {
        String selPriceRankSql = "SELECT created_on FROM price_drop_rank WHERE category_name =  ? ORDER BY created_on DESC LIMIT 1";
        return jdbcBaseDao.queryList(PriceDropRank.class, selPriceRankSql, goodsName);
    }


    /**
     * 查询该商品在指定时间的数据
     *
     * @param selNum    查询条数
     * @param goodsName 商品名
     * @param Time      时间
     * @return 查询的数据结果集
     */
    public List getChangeRank(int selNum, String goodsName, Date Time) {
        //产品价格化排名
        String selPriceRankSql = "SELECT product_description , transaction_price FROM price_change_rank WHERE created_on = ?  and category_name =  ? ORDER BY transaction_price DESC LIMIT " + selNum;
        return jdbcBaseDao.queryList(PriceChangeRank.class, selPriceRankSql, Time, goodsName);
    }

    /**
     * 查询该商品在指定时间的数据
     *
     * @param selNum    查询条数
     * @param goodsName 商品名
     * @param Time      时间
     * @return 查询的数据结果集
     */
    public List getChangeRatioRank(int selNum, String goodsName, Date Time) {
        //产品价格降幅排名
        String selPriceRatioRankSql = "SELECT price_rate,product_description FROM price_drop_rank WHERE created_on = ?  and category_name =  ? ORDER BY price_rate DESC limit " + selNum;
        List<PriceDropRank> PriceDropRank = jdbcBaseDao.queryList(PriceDropRank.class, selPriceRatioRankSql, Time, goodsName);
        return PriceDropRank;
    }

}
