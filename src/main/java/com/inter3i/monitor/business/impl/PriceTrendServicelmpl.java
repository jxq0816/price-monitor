package com.inter3i.monitor.business.impl;

import com.alibaba.fastjson.JSON;
import com.inter3i.monitor.business.CategoryService;
import com.inter3i.monitor.business.CommodityService;
import com.inter3i.monitor.business.PriceTrendService;
import com.inter3i.monitor.business.ProductLibraryService;
import com.inter3i.monitor.component.ScheduleJob;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.Category;
import com.inter3i.monitor.entity.Commodity;
import com.inter3i.monitor.entity.PriceChangeRatio;
import com.inter3i.monitor.entity.ProductLibrary;
import com.inter3i.monitor.util.GetTimeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 计算商品类型的价格降价趋势
 * author :   yu
 * date   :   2017/6/14.
 */
@Service
public class PriceTrendServicelmpl implements PriceTrendService {
    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductLibraryService productLibraryService;

    @Autowired
    private CommodityService commodityService;

    public final int DAY_NUM = 2;
    public final Long TIME_ZONE_LONG = DAY_NUM * 24 * 3600 * 1000L;
    public final Long COUNT_START_TIME = 1497369600000L;   //2017.6.15  数据开始抓取时间

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

    /**
     * 计算指定商品名的价格变化趋势
     *
     * @return 执行结果
     */
    public Boolean handlePriceTrend() {
        logger.info("in priceTrend : handlePriceTrend start ~~");
        Boolean handleRes = true;
        List<Category> goodsNames = categoryService.getCategroyName();
        logger.info("in priceTrend : the CategroyNameData size is:[" + goodsNames.size() + "]");
        if (goodsNames.size() > 0) {
            for (int i = 0; i < goodsNames.size(); i++) {
                Category goodsNameData = goodsNames.get(i);
                String goodsName = goodsNameData.getCategoryName();
                handlePriceTrend(goodsName);
            }
        } else {
            throw new RuntimeException("in priceTrend : handlePriceTrend error : the goodsName is null.");
        }
        return handleRes;
    }


    /**
     * 计算指定商品名的价格变化趋势
     *
     * @param goodsName 商品名
     * @return 执行结果
     */
    public Boolean handlePriceTrend(String goodsName) {
        logger.info("in priceTrend : the goodsName is:[" + goodsName + "]");
        Boolean handleRes = true;
        int changeNum = 0;
        //从产品库中拿到对应商品所有需要监控的url
        List<ProductLibrary> datas = productLibraryService.getUrlFromLibrary(goodsName);
        logger.info("in priceTrend : the getUrlFromLibraryData size is:[" + datas.size() + "]");
        int goodsTotalNum = datas.size();
        logger.info("in priceTrend : the goodsTotalNum is:[" + goodsTotalNum + "]");
        if (datas.size() == 0) {
            logger.info("in priceTrend : the goodsName :[" + goodsName + "] in ProductLibrary is null");
            return null;
        }
        GetTimeUtil getTimeUtil = new GetTimeUtil();
        Long startTime = getTimeUtil.getLastDayStartTime(System.currentTimeMillis(), DAY_NUM);
        Long endTime = startTime + TIME_ZONE_LONG;
        //从抽取表中拿到近两天数据
        List<Commodity> priceDatas = commodityService.getDatas(startTime, endTime);
        logger.info("in priceTrend : the getDatas size is:[" + priceDatas.size() + "]");
        Map parentMap = new HashMap();
        //然后以page_url为键名，放到ParentMap中,在根据键名page_url,来把今天(stickPrice)和昨天(lastStickPrice)的数据区分开
        for (int i = 0; i < priceDatas.size(); i++) {
            Map childMap = new HashMap();
            Commodity priceData = priceDatas.get(i);
            String commodityPageUrl = priceData.getPageUrl();
            Double stickPrice = priceData.getStickPrice();
            //如果查询结果中url为空，则跳过。
            if (StringUtils.isEmpty(commodityPageUrl)) {
                continue;
            }
            //查询结果中价格为空，也跳过，不处理
            if (stickPrice == null) {
                continue;
            }
            if (parentMap.containsKey(commodityPageUrl)) {
                childMap.put("stickPrice", ((Map) parentMap.get(commodityPageUrl)).get("stickPrice"));
                childMap.put("lastStickPrice", stickPrice);
            } else {
                childMap.put("stickPrice", stickPrice);
            }
            parentMap.put(commodityPageUrl, childMap);
        }
        if (parentMap.size() == 0) {
            return null;
        }
        //循环从产品库中取出的url，判断parentMap是否存在其近两天的数据，如果有一天数据缺失，则跳过
        for (int i = 0; i < datas.size(); i++) {
            ProductLibrary libraryData = datas.get(i);
            String pageUrl = libraryData.getPageUrl();
            if (!parentMap.containsKey(pageUrl)) {
                continue;
            }
            Map data = (Map) parentMap.get(pageUrl);
            if ((data.containsKey("lastStickPrice") && data.get("lastStickPrice") != null) & (data.containsKey("stickPrice") && data.get("stickPrice") != null)) {
                if (!data.get("stickPrice").equals(data.get("lastStickPrice"))) {
                    changeNum += 1;
                }
            } else {
                continue;
            }
        }
        logger.info("in priceTrend : the changeNum is:[" + changeNum + "]");
        Long storageTime = getTimeUtil.getLastDayStartTime(System.currentTimeMillis(), 1);
        String storageDateTime = getTimeUtil.changeLongtimeToDate(storageTime);
        //入库之前查询，若结果表中有今天的此商品的数据，则update，没有则insert
        String selPriceChangeRatioSql = "SELECT id FROM price_change_ratio where created_on = ? and category_name = ?";
        List<PriceChangeRatio> isExist = jdbcBaseDao.queryList(PriceChangeRatio.class, selPriceChangeRatioSql, storageDateTime, goodsName);
        //计算出该商品价格变化趋势，保留两位小数点
        double priceChangeRatio = (double) changeNum / (double) goodsTotalNum;
        DecimalFormat df = new DecimalFormat("0.00");
        String priceChangeRatio1 = df.format(priceChangeRatio);
        if (isExist.size() > 0) {
            int id = isExist.get(0).getId();
            logger.info("in priceTrend :  the data in  [ price_change_ratio ] is existent.the id is:[" + id + "]");
            String updPriceChangeRatioSql = "UPDATE price_change_ratio set change_ratio = ? where id = ?";
            jdbcBaseDao.executeUpdate(updPriceChangeRatioSql, priceChangeRatio1, id);
        } else {
            logger.info("in priceTrend : the data in  [ price_change_ratio ] is Non-existent ");
            String insertPriceChangeRatioSql = "INSERT INTO price_change_ratio(category_name,change_ratio,product_description,created_on) VALUES (?,?,?,?)";
            jdbcBaseDao.executeUpdate(insertPriceChangeRatioSql, goodsName, priceChangeRatio1, "", storageDateTime);
        }
        return handleRes;
    }

    /**
     * 查询指定条数的商品价格
     *
     * @param goodsName 商品名
     * @param selNum    查询条数
     * @return 返回包含查询结果的list
     */
    public List getPriceTrendData(String goodsName, int selNum) {
        String selPriceChangeRatioSql = "SELECT change_ratio,created_on FROM price_change_ratio WHERE category_name = ? ORDER BY created_on DESC LIMIT " + selNum;
        List<PriceChangeRatio> priceChangeRatioDatas = jdbcBaseDao.queryList(PriceChangeRatio.class, selPriceChangeRatioSql, goodsName);
        return priceChangeRatioDatas;
    }

    /**
     * 补全商品下面的价格趋势
     *
     * @param priceChangeRatioDatas 数据结果集
     * @return 返回有序的日期和降价趋势结果
     */
    public String complementPriceTrend(List<PriceChangeRatio> priceChangeRatioDatas, int selNum) {
        GetTimeUtil getTimeUtil = new GetTimeUtil();
        String res = null;
        Map selRes = new HashMap();
        List timeList = new ArrayList();
        List ratioList = new ArrayList();
        Map priceDataMap = new HashMap();
        //循环查询结果
        for (int i = 0; i < priceChangeRatioDatas.size(); i++) {
            PriceChangeRatio priceChangeRatioData = priceChangeRatioDatas.get(i);
            //价格，日期都不为空，则放到新的map中
            if (priceChangeRatioData.getChangeRatio() != 0 && priceChangeRatioData.getCreatedOn() != null) {
                String changeDateTime = getTimeUtil.getTodayDate(priceChangeRatioData.getCreatedOn());
                priceDataMap.put(changeDateTime, priceChangeRatioData.getChangeRatio());
            }
        }
        Long startTime = System.currentTimeMillis();
        List noValueList = new ArrayList();
        List newPriceDataMap = new ArrayList();
        //while循环，时间从昨天凌晨开始，查询在map中，是否存在key为昨天凌晨时间的map
        while (true) {
            Map childMap1 = new HashMap();
            Long lastLongTime = getTimeUtil.getLastDayStartTime(startTime, 1);
            String lastDayTime = getTimeUtil.changeLongtimeToDate(lastLongTime);
            //用前台请求的条数来作为循环的出口，如果连续三十天没有数据
            if (noValueList.size() >= selNum) {
                break;
            }
            //如果新的map大于等于前台要的数据量
            if (newPriceDataMap.size() >= selNum) {
                break;
            }
            //大于数据抓取的起始时间
            if (lastLongTime.equals(COUNT_START_TIME)) {
                break;
            }
            if (priceDataMap.size() <= selNum) {
                //因为考虑到中间可能会有缺失，所以有以下补数据逻辑,如果当前时间价格趋势为空，则向前找，以前面不为空的代替.
                //如果不包含以时间为key值的map，，则把他的时间放到noValueList 中  .
                //如果包含以时间为key值的map，则判断noValueList，
                //如果为空，就取出key对应的map，放到newPriceDataMap中。
                //如果不为空，则循环noValueList，全部设置到newPriceDataMap上，值设为当前日期的值
                if (priceDataMap.containsKey(lastDayTime) && priceDataMap.get(lastDayTime) != null && (Double) priceDataMap.get(lastDayTime) != 0) {
                    childMap1.put(lastDayTime, priceDataMap.get(lastDayTime));
                    if (noValueList.size() > 0) {
                        for (int i = 0; i < noValueList.size(); i++) {
                            Map childMap = new HashMap();
                            String time = (String) noValueList.get(i);
                            childMap.put(time, priceDataMap.get(lastDayTime));
                            newPriceDataMap.add(childMap);
                        }
                        newPriceDataMap.add(childMap1);
                        noValueList.clear();
                    } else {
                        newPriceDataMap.add(childMap1);
                    }
                } else {
                    noValueList.add(lastDayTime);
                }
            } else {
                break;
            }
            startTime = lastLongTime;
        }
        //循环newPriceDataMap数组，把日期和降价趋势分别取出，放到list中，这样map里面的数据则是有序的。最后在转string，返回前台需要的格式
        for (int i = 0; i < newPriceDataMap.size(); i++) {
            //限制条数，确认只返回前台指定的条数
            if (i <= (selNum - 1)) {
                Map m = (Map) newPriceDataMap.get(i);
                Iterator entries = m.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String time = (String) entry.getKey();
                    Double value = (Double) entry.getValue();
                    timeList.add(time);
                    ratioList.add(value);
                }
            } else {
                break;
            }
        }
        selRes.put("timeList", timeList);
        selRes.put("ratioList", ratioList);
        res = JSON.toJSONString(selRes);
        return res;
    }


}
