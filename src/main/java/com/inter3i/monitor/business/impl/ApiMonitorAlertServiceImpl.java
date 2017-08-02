package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.ApiMonitorAlertService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.Commodity;
import com.inter3i.monitor.entity.MonitorAlertEntity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/15 13:16
 */
@Service
public class ApiMonitorAlertServiceImpl implements ApiMonitorAlertService{

    private JDBCBaseDao jdbcBaseDao;

    public void buildPromotionPrice(String dataFlag) {
        String commodity_lastest_sql = "SELECT stick_price,stick_promotionInfos FROM commodity WHERE last_data_flag != ? AND page_url = ? AND stick_price IS NOT NULL ORDER BY last_extract_time DESC LIMIT 1";
        String commodity_sql = "UPDATE commodity SET product_category = ?,stick_price = ?,stick_promotionInfos = ? WHERE id = ?";
        String product_library_sql = "SELECT cast(is_frame as unsigned int) FROM product_library WHERE page_url = ? LIMIT 1";
        String sql = "SELECT id,product_category,original_price,discount_price,promotionInfos,stick_price,stick_promotionInfos,page_url FROM commodity WHERE last_data_flag = ?";
        List<Commodity> commodityList = jdbcBaseDao.queryList(Commodity.class,sql,dataFlag);
        if(CollectionUtils.isNotEmpty(commodityList)){
            int count = 0;
            for(Commodity commodity : commodityList){
                System.out.println( ++count + "/" + commodityList.size() );
                Integer id = commodity.getId();  //商品信息表ID
                String productCategory = commodity.getProductCategory();  //产品描述
                Double originalPrice = commodity.getOriginalPrice();  //原价
                Double discountPrice = commodity.getDiscountPrice();  //促销价
                String promotioninfos = commodity.getPromotioninfos();  //促销信息
                String pageUrl = commodity.getPageUrl();  //页面地址
                if(originalPrice == null && discountPrice == null){//原价和促销价都为空
                    Object productLibraryObj = jdbcBaseDao.getColumn(product_library_sql,pageUrl);
                    if(productLibraryObj != null && StringUtils.isNotEmpty(productLibraryObj.toString())){
                        Boolean isFrame = Boolean.valueOf(productLibraryObj.toString().equals("0"));
                        if(!isFrame){//未下架
                            Commodity lastestCommodity = jdbcBaseDao.executeQuery(Commodity.class,commodity_lastest_sql,dataFlag,pageUrl);
                            if(lastestCommodity != null){
                                jdbcBaseDao.executeUpdate(commodity_sql,productCategory,lastestCommodity.getStickPrice(),lastestCommodity.getStickPromotioninfos(),id);
                            }
                        }
                    }
                }else{
                    Double stickPriceOriginal = (discountPrice == null) ? originalPrice : discountPrice;  //成交价
                    if(StringUtils.isEmpty(promotioninfos)){
                        jdbcBaseDao.executeUpdate(commodity_sql,productCategory,stickPriceOriginal,null,id);
                    }else{
                        PromotionHelper promotionHelper = PromotionHelper.handle(promotioninfos,stickPriceOriginal);
                        if(promotionHelper.getStickPrice() != null){
                            jdbcBaseDao.executeUpdate(commodity_sql,productCategory,promotionHelper.getStickPrice(),promotionHelper.getStickPromotioninfos(),id);
                        }else{
                            jdbcBaseDao.executeUpdate(commodity_sql,productCategory,stickPriceOriginal,null,id);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<MonitorAlertEntity> queryMonitorAlertEntityList(String dataFlag) {
        String sql = "SELECT a.id AS commodity_id, b.id AS task_configuration_zb_id, c.sys_user_id, c.user_name, c.email, c.task_name, c.created_on, c.category_name, c.brand, c.models, c.season, c.style, c.features, c.material, b.warning_price, a.stick_price, a.page_url, a.stick_promotionInfos FROM commodity a INNER JOIN task_configuration_zb b ON a.page_url = b.page_url INNER JOIN task_configuration c ON b.task_configuration_id = c.id WHERE a.last_data_flag = ? AND b.delete_mark = 1 AND b.task_status = 0 AND c.task_status = 0 AND c.delete_mark = 1 AND a.stick_price <= b.warning_price";
        return jdbcBaseDao.queryList(MonitorAlertEntity.class,sql,dataFlag);
    }

    @Override
    public void deleteOffProductData(String dataFlag) {
        String sql = "DELETE a.* FROM commodity a INNER JOIN product_library b ON a.page_url = b.page_url WHERE  a.last_data_flag = ? AND cast(b.is_frame as unsigned int) = 0";
        jdbcBaseDao.executeUpdate(sql,dataFlag);
    }

    public JDBCBaseDao getJdbcBaseDao() {
        return jdbcBaseDao;
    }
    @Autowired
    public void setJdbcBaseDao(JDBCBaseDao jdbcBaseDao) {
        this.jdbcBaseDao = jdbcBaseDao;
    }
}
