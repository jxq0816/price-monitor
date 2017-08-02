package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.BrandAvgChangeService;
import com.inter3i.monitor.business.CategoryService;
import com.inter3i.monitor.business.CommodityService;
import com.inter3i.monitor.business.ProductLibraryService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.BrandAveragePrice;
import com.inter3i.monitor.entity.Category;
import com.inter3i.monitor.entity.Commodity;
import com.inter3i.monitor.entity.ProductLibrary;
import com.inter3i.monitor.util.GetTimeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 计算商品类型下的所有商品的降价趋势及降价幅度
 * author :   yu
 * date   :   2017/6/14.
 */
@Service
public class BrandAvgChangeServiceImpl implements BrandAvgChangeService {
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

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BrandAvgChangeServiceImpl.class);

    /**
     * 查询该商品的入库最大时间
     *
     * @param goodsName 商品名
     * @return 查询的数据结果集
     */
    public List getMaxBrandAvgChange(String goodsName) {
        String selBrankAvgPriceSql = "SELECT created_on FROM brand_average_price WHERE category_name = ? ORDER BY created_on DESC LIMIT 1";
        List<BrandAveragePrice> MaxBrandAvgChangeTime = jdbcBaseDao.queryList(BrandAveragePrice.class, selBrankAvgPriceSql, goodsName);
        return MaxBrandAvgChangeTime;
    }

    /**
     * 获取商品名指定时间下的数据
     *
     * @param num       查询条数
     * @param goodsName 商品名
     * @param time      查询时间
     * @return 返回包含品牌名和价格趋势的list
     */
    public List getBrandAvgRatioRank(int num, String goodsName, Date time) {
        String selBrankAvgPriceSql = "SELECT brand_name,price_rate FROM brand_average_price WHERE category_name = ? AND created_on = ? ORDER BY ABS(price_rate) DESC LIMIT " + num;
        List<BrandAveragePrice> brandAveragePrice = jdbcBaseDao.queryList(BrandAveragePrice.class, selBrankAvgPriceSql, goodsName, time);
        return brandAveragePrice;
    }

    /**
     * 计算所有商品的品牌下的价格变化趋势
     *
     * @return 计算结果
     */
    public Boolean handBrandAvgChange() {
        List<Category> goodsNames = categoryService.getCategroyName();
        logger.info("in brandAvgChange : the CategroyNameData size is:[" + goodsNames.size() + "]");
        for (int i = 0; i < goodsNames.size(); i++) {
            Category goodsNameData = goodsNames.get(i);
            String goodsName = goodsNameData.getCategoryName();
            handBrandAvgChange(goodsName);
        }
        return true;
    }

    /**
     * 计算商品的品牌下的价格变化趋势
     *
     * @param goodsName 商品名
     * @return 计算结果
     */
    public Boolean handBrandAvgChange(String goodsName) {
        logger.info("in brandAvgChange : the goodsName is:[" + goodsName + "]");
        //从产品库中拿到对应商品所有需要监控的url
        List<ProductLibrary> datas = productLibraryService.getUrlFromLibrary(goodsName);
        logger.info("in brandAvgChange : the getUrlFromLibraryData size is:[" + datas.size() + "]");
        if (datas.size() == 0) {
            System.out.println("in brandAvgChange : the goodsName :[" + goodsName + "] in ProductLibrary is null");
            return null;
        }
        GetTimeUtil getTimeUtil = new GetTimeUtil();
        Long startTime = getTimeUtil.getLastDayStartTime(System.currentTimeMillis(), priceTrendServicelmpl.DAY_NUM);
        Long endTime = startTime + priceTrendServicelmpl.TIME_ZONE_LONG;
        //从抽取表中拿到近两天数据倒叙查询，然后以page_url为键名，放到map中
        List<Commodity> priceDatas = commodityService.getDatas(startTime, endTime);
        logger.info("in brandAvgChange : the getDatas size is:[" + priceDatas.size() + "]");
        Map parentMap = new HashMap();
        for (int i = 0; i < priceDatas.size(); i++) {
            Map childMap = new HashMap();
            Commodity priceData = priceDatas.get(i);
            String commodityPageUrl = priceData.getPageUrl();
            Double stickPrice = priceData.getStickPrice();
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
            } else {
                childMap.put("stickPrice", stickPrice);
            }
            parentMap.put(commodityPageUrl, childMap);
        }
        Long lastDayLongTime = getTimeUtil.getLastDayStartTime(System.currentTimeMillis(), 1);
        String lastDayDateTime = getTimeUtil.changeLongtimeToDate(lastDayLongTime);
        DecimalFormat df = new DecimalFormat("0.00");
        Map resMap = new HashMap();
        //再循环从产品库中拿到的url做对比，如果存在则放到新的map中，以品牌为键名，记录num和totalPrice ,不存在，则添加，存在，则num++，totalprice相加
        for (int i = 0; i < datas.size(); i++) {
            Map resChildMap = new HashMap();
            ProductLibrary libraryData = datas.get(i);
            String pageUrl = libraryData.getPageUrl();
            String brand = libraryData.getBrand();
            if (!parentMap.containsKey(pageUrl)) {
                continue;
            }
            Map data = (Map) parentMap.get(pageUrl);
            if (data.size() == 0 || data.isEmpty()) {
                continue;
            } else {
                //结果中包含此品牌的key值的map，若resMap下的lastStickPrice存在，data对象下lastStickPrice也存在，则resmap下的lastStickPrice相加，lastStickPriceNum也+1，
                // 若resMap下的lastStickPrice不存在，data对象下lastStickPrice存在，则put到resmap中
                // 若resMap下的lastStickPrice不存在，data对象下lastStickPrice也不存在，则跳过.
                // stickPrice，逻辑也和上面的lastStickPrice一样.
                if (resMap.containsKey(brand)) {
                    //价格相加，num++
                    Map brandData = (Map) resMap.get(brand);
                    if (brandData.containsKey("lastStickPrice") && brandData.get("lastStickPrice") != null && (Double) brandData.get("lastStickPrice") != 0) {
                        if (data.containsKey("lastStickPrice") && data.get("lastStickPrice") != null) {
                            brandData.put("lastStickPrice", (Double) data.get("lastStickPrice") + (Double) brandData.get("lastStickPrice"));
                            brandData.put("lastStickPriceNum", (Integer) brandData.get("lastStickPriceNum") + 1);
                        }

                    } else {
                        if (data.containsKey("lastStickPrice") && data.get("lastStickPrice") != null) {
                            brandData.put("lastStickPrice", data.get("lastStickPrice"));
                            brandData.put("lastStickPriceNum", 1);
                        }
                    }
                    if (brandData.containsKey("stickPrice") && brandData.get("stickPrice") != null && (Double) brandData.get("stickPrice") != 0) {
                        if (data.containsKey("stickPrice") && data.get("stickPrice") != null) {
                            brandData.put("stickPrice", (Double) data.get("stickPrice") + (Double) brandData.get("stickPrice"));
                            brandData.put("stickPriceNum", (Integer) brandData.get("stickPriceNum") + 1);
                        }
                    } else {
                        if (data.containsKey("stickPrice") && data.get("stickPrice") != null) {
                            brandData.put("stickPrice", data.get("stickPrice"));
                            brandData.put("stickPriceNum", 1);
                        }
                    }
                    resMap.put(brand, brandData);
                } else {
                    //不存在则put进去
                    if (data.containsKey("lastStickPrice") && data.get("lastStickPrice") != null) {
                        resChildMap.put("lastStickPrice", data.get("lastStickPrice"));
                        resChildMap.put("lastStickPriceNum", 1);
                    }
                    if (data.containsKey("stickPrice") && data.get("stickPrice") != null) {
                        resChildMap.put("stickPrice", data.get("stickPrice"));
                        resChildMap.put("stickPriceNum", 1);
                    }
                    resMap.put(brand, resChildMap);
                }
            }
        }
        if (resMap.size() == 0) {
            return null;
        }
        //循环map，拿出num和totalPrice，求出品牌趋势。
        Iterator entries = resMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String brank = (String) entry.getKey();
            Double brankTodayChangeRatio = 0d;
            Double brankLastDayChangeRatio = 0d;
            String subDValue = null;
            Map brankCountData = (Map) entry.getValue();
            //昨天的该商品的品牌均价
            if (brankCountData.containsKey("stickPrice") && brankCountData.get("stickPrice") != null) {
                Double stickPriceCount = (Double) brankCountData.get("stickPrice");
                Integer stickPriceCountNum = (Integer) brankCountData.get("stickPriceNum");
                logger.debug("the stickPrice is:[" + stickPriceCount + "]");
                logger.debug("the stickPriceCountNum is:[" + stickPriceCountNum + "]");
                brankTodayChangeRatio = stickPriceCount / stickPriceCountNum;
            }
            //前天的该商品的品牌均价
            if (brankCountData.containsKey("lastStickPrice") && brankCountData.get("lastStickPrice") != null) {
                Double stickPriceCount1 = (Double) brankCountData.get("lastStickPrice");
                Integer stickPriceCountNum1 = (Integer) brankCountData.get("lastStickPriceNum");
                logger.debug("the stickPriceCount1 is:[" + stickPriceCount1 + "]");
                logger.debug("the stickPriceCountNum1 is:[" + stickPriceCountNum1 + "]");
                brankLastDayChangeRatio = stickPriceCount1 / stickPriceCountNum1;
            }
            //最后得到昨天的品牌价格变化趋势
            if (brankTodayChangeRatio != 0 && brankLastDayChangeRatio != 0) {
                Double ratio = ((brankTodayChangeRatio - brankLastDayChangeRatio) / brankLastDayChangeRatio);
                subDValue = df.format(ratio);
                logger.info("in brandAvgChange : the brand is:[" + brank + "],the ratio is:[" + subDValue + "],the DateTime is:[" + lastDayDateTime + "]");
            } else {
                continue;
            }
            //查询结果表，有则update，无则insert
            String selBrankAvgPriceSql = "SELECT id FROM brand_average_price WHERE category_name = ? AND brand_name = ? AND created_on = ? ";
            List<BrandAveragePrice> isExist = jdbcBaseDao.queryList(BrandAveragePrice.class, selBrankAvgPriceSql, goodsName, brank, lastDayDateTime);
            if (isExist.size() > 0) {
                int id = isExist.get(0).getId();
                logger.info("in brandAvgChange :  the data in  [ brand_average_price ] is existent.the id is:[" + id + "]");
                String updPriceChangeRatioSql = "UPDATE brand_average_price set price_rate = ? where id = ? ";
                jdbcBaseDao.executeUpdate(updPriceChangeRatioSql, subDValue, id);
            } else {
                logger.info("in brandAvgChange : the data in  [ brand_average_price ] is Non-existent ");
                String insertPriceChangeRatioSql = "INSERT INTO brand_average_price(category_name,brand_name,price_rate,   created_on) VALUES (?,?,?,?)";
                jdbcBaseDao.executeUpdate(insertPriceChangeRatioSql, goodsName, brank, subDValue, lastDayDateTime);
            }
        }
        return true;
    }
}
