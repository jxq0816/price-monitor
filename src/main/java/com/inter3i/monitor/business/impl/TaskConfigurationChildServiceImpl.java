package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.TaskConfigurationChildService;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.TaskChild;
import com.inter3i.monitor.entity.TaskConfigurationZb;
import com.inter3i.monitor.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by boxiaotong on 2017/6/14.
 */
@Service
public class TaskConfigurationChildServiceImpl implements TaskConfigurationChildService {
    @Autowired
    private JDBCBaseDao jdbcBaseDao;


    @Override
    public List<TaskConfigurationZb> findList(String taskId){
        String sql = "select id ,task_configuration_id,product_description,seller_name,warning_price,warning_number,warning_time,page_url,task_status,delete_mark,update_time,created_on from task_configuration_zb where DELETE_MARK='0' AND task_configuration_id = ?";
        List<TaskConfigurationZb> list = jdbcBaseDao.queryList(TaskConfigurationZb.class,sql, taskId);
       return list;
    }

    @Override
    public TaskConfigurationZb findOne(String childTaskId) {
        String sql = "select id ,task_configuration_id,product_description,seller_name,warning_price,warning_number,warning_time,page_url,task_status,delete_mark,update_time,created_on " +
                " from task_configuration_zb " +
                " left join product_library as pro on child.page_url=pro.page_url " +
                " where DELETE_MARK='1' AND id = ?";
        TaskConfigurationZb rs = jdbcBaseDao.executeQuery(TaskConfigurationZb.class,sql, childTaskId);
        return rs;
    }

    @Override
    public Map<String,Object> findOneMap(String childTaskId) {
        String sql = "select child.id  ,pro.product_description,pro.seller_name,warning_price,pro.page_url,pro.platform,pro.brand" +
                " from task_configuration_zb as child " +
                " left join product_library as pro on child.page_url=pro.page_url " +
                " where DELETE_MARK='1' AND child.id = ?";
        Map<String,Object> rs = jdbcBaseDao.getMapResult(sql, childTaskId);
        return rs;
    }

    @Override
    public List<Map<String,Object>> queryMapList(String taskId){
        String sql = "SELECT child.id as childTaskId,task_configuration_id as taskId," +
                " pro.brand,child.product_description as productDescription,pro.platform as platform,parent.MONITOR_TYPE as monitorType," +
                " child.seller_name as sellerName,warning_price as warningPrice,warning_number as warningNumber,Date(warning_time) as warningTime" +
                " FROM task_configuration_zb as child LEFT JOIN product_library as pro on child.page_url=pro.page_url LEFT JOIN task_configuration as parent on parent.id=child.task_configuration_id" +
                " WHERE child.DELETE_MARK = '0' AND task_configuration_id = ?";
        List<Map<String,Object>> list = jdbcBaseDao.queryMapList(sql,taskId);
        return list;
    }

    @Override
    public Page findPage(PageBean pageBean,String taskId,String orderParam,String orderType) {
        if(StringUtils.isEmpty(orderParam)){
            orderParam="warningTime";
        }
        if(StringUtils.isEmpty(orderType)){
            orderType="desc";
        }
        String sql = "SELECT child.id as childTaskId,task_configuration_id as taskId,pro.CATEGORY_NAME as categoryName," +
                " pro.brand,child.product_description as productDescription,pro.platform as platform,parent.MONITOR_TYPE as monitorType," +
                " child.seller_name as sellerName,warning_price as warningPrice,warning_number as warningNumber,Date(warning_time) as warningTime" +
                " FROM task_configuration_zb as child LEFT JOIN product_library as pro on child.page_url=pro.page_url LEFT JOIN task_configuration as parent on parent.id=child.task_configuration_id" +
                " WHERE child.DELETE_MARK = '1' AND task_configuration_id = ?"+
                " order by "+orderParam+"  "+orderType;

        Page page=jdbcBaseDao.queryPage(TaskChild.class, pageBean,sql,taskId);
        return page;
    }

    @Override
    public List  priceTrend(String childTaskId){
        String sql="SELECT from_unixtime(c.storage_time / 1000,'%Y-%m-%d') as date,c.stick_price as price,active.info FROM commodity c " +
                "INNER JOIN (SELECT min(b.storage_time) AS minStorageTime ,b.id FROM commodity b INNER JOIN task_configuration_zb as zb on zb.page_url=b.page_url " +
                "WHERE zb.id = ? AND b.storage_time IS NOT NULL AND b.stick_price IS NOT NULL GROUP BY b.page_url,from_unixtime(b.storage_time / 1000,'%Y-%m-%d') " +
                ")v ON c.id = v.id " +
                "LEFT JOIN (SELECT min(created_on) AS minStorageTime,promotionlnfos as info FROM active_content " +
                "WHERE task_configuration_zb_id = ? GROUP BY task_configuration_zb_id,date(created_on) " +
                ") active ON from_unixtime(c.storage_time / 1000,'%Y-%m-%d') = Date(active.minStorageTime)";
        List<Map<String, Object>> list = jdbcBaseDao.queryMapList(sql, childTaskId,childTaskId);
        return list;
    }

    @Override
    public List supplementPriceTrend(List<Map<String,Object>> priceTrend,String childTaskId) {
        Map startMap=priceTrend.get(0);
        int size=priceTrend.size();
        Map endMap=priceTrend.get(size-1);
        String startDateStr = (String)startMap.get("date");
        String endDateStr = (String)endMap.get("date");
        Date startDate = DateUtils.string2Date(startDateStr);
        Date endDate = DateUtils.string2Date(endDateStr);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        while(true) {

            startCalendar.add(Calendar.DATE, 1);//增加一天
            Date index=startCalendar.getTime();
            String indexString= DateUtils.date2String(index);
            if(index.compareTo(endDate)>0){
                break;
            }
            if(!isExist(childTaskId,index)) {
                //缺失数据
                Calendar beforeCalendar = Calendar.getInstance();
                beforeCalendar.setTime(index);
                Map<String, Object> beforeMap;
                do {
                    beforeCalendar.add(Calendar.DATE, -1);//往前找
                    Date beforeDate = beforeCalendar.getTime();
                    String beforeDateString = DateUtils.date2String(beforeDate);
                    beforeMap = getPriceMap(childTaskId, beforeDateString);//拿到以前的数据
                } while (beforeMap == null);
                beforeMap.put("date", indexString);//用前面的数据代替
                priceTrend.add(beforeMap);
            }
        }
        return priceTrend;
    }

    private boolean isExist(String childTaskId,Date date){

        boolean bool=false;
        String dateString=DateUtils.date2String(date);
        Map<String,Object> rs = getPriceMap(childTaskId,dateString);
        if(rs!=null){
            bool=true;
        }
        return bool;
    }

    private Map<String,Object> getPriceMap(String childTaskId,String dateString) {
        String sql = "select commodity.stick_price as price, " +
                "from_unixtime(commodity.storage_time/1000, '%Y-%m-%d') as date " +
                "from commodity LEFT JOIN task_configuration_zb as zb on zb.page_url=commodity.page_url " +
                "where zb.id= ? and from_unixtime(commodity.storage_time/1000, '%Y-%m-%d')= ? and commodity.stick_price is not null";
        Map<String, Object> rs = jdbcBaseDao.getMapResult(sql, childTaskId, dateString);
        return rs;
    }
}
