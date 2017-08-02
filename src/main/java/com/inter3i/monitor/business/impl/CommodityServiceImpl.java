package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.CommodityService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.Commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/15 15:15
 */
@Service
public class CommodityServiceImpl implements CommodityService{

    private JDBCBaseDao jdbcBaseDao;


    @Override
    public void deleteDuplicate() {
        try {
            String sql = "DELETE b.* FROM commodity b LEFT JOIN ( SELECT a.page_url, MIN(a.storage_time) AS min_storage_time FROM commodity a WHERE a.storage_time >= ? AND a.storage_time < ? GROUP BY a.page_url ) v ON b.page_url = v.page_url WHERE b.storage_time >= ? AND b.storage_time < ? AND v.min_storage_time != b.storage_time";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = sdf.parse(sdf.format(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Date endDate = calendar.getTime();
            Long beginTime = beginDate.getTime();
            Long endTime = endDate.getTime();
            jdbcBaseDao.executeUpdate(sql,beginTime,endTime,beginTime,endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNullStickPrice(String dataFlag) {
        String sql = "DELETE FROM commodity WHERE last_data_flag <= ? AND (stick_price IS NULL OR stick_price < 0)";
        jdbcBaseDao.executeUpdate(sql,dataFlag);
    }

    public List<Commodity> getDatas(Long startTime, Long endTime){
        String selCommoditySql = "SELECT product_category as productCategory,stick_price as stickPrice, page_url as pageUrl  FROM commodity where storage_time between ? and ?  ORDER BY storage_time DESC";
        return jdbcBaseDao.queryList(Commodity.class, selCommoditySql, startTime, endTime);
    }

    public JDBCBaseDao getJdbcBaseDao() {
        return jdbcBaseDao;
    }
    @Autowired
    public void setJdbcBaseDao(JDBCBaseDao jdbcBaseDao) {
        this.jdbcBaseDao = jdbcBaseDao;
    }
}
