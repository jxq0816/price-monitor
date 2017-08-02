package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.CategoryAttributeService;
import com.inter3i.monitor.business.TaskConfigurationService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by boxiaotong on 2017/6/14.
 */
@Service
public class TaskConfigurationServiceImpl implements TaskConfigurationService{
    @Autowired
    private CategoryAttributeService categoryAttributeService;

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<Map<String,Object>> findList(String userId,String categoryName) {
        String sql = "select ID as taskId,TASK_NAME as taskName " +
                "from task_configuration " +
                "where DELETE_MARK='1' AND SYS_USER_ID= ? AND CATEGORY_NAME= ?  ORDER BY CREATED_ON desc";
        List<Map<String, Object>> list = jdbcBaseDao.queryMapList(sql, userId, categoryName);
        return list;
    }

    @Override
    public Map<String, Object> findLastOne(String userId) {
        String sql = "select id as taskId,category_name as categoryName " +
                " from task_configuration " +
                " where DELETE_MARK='1' AND SYS_USER_ID= ? " +
                " ORDER BY CREATED_ON desc " +
                " limit 0,1";
        Map<String, Object> map = jdbcBaseDao.getMapResult(sql, userId);
        if(map==null || map.size()==0){
            return null;
        }
        String taskId=(String) map.get("taskId");
        String categoryName=(String) map.get("categoryName");
        List<Map<String,Object>> attributes=categoryAttributeService.getAttribute(categoryName);
        Map param=new HashMap();
        for(Map<String,Object> a:attributes){
            String englishField=(String)a.get("englishField");
            String chineseField=(String)a.get("chineseField");
            param.put(englishField,chineseField);
        }
        Map<String,Object> rs=this.getTask(taskId,param);
        return rs;
    }
    @Override
    public Map<String, Object> findOne(String taskId,String categoryName) {
        List<Map<String,Object>> attributes=categoryAttributeService.getAttribute(categoryName);
        Map param=new HashMap();
        for(Map<String,Object> a:attributes){
            String englishField=(String)a.get("englishField");
            String chineseField=(String)a.get("chineseField");
            param.put(englishField,chineseField);
        }
        Map<String,Object> rs=this.getTask(taskId,param);
        return rs;
    }

    /**
     * 查询用户最近的一条任务信息
     *
     * @param taskId
     * @return
     */
    private Map<String, Object> getTask(String taskId, Map paramList) {

        Iterator<Map.Entry<String, String>> entries = paramList.entrySet().iterator();
        String paramStr = "";
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String englishField = entry.getKey();
            String chineseField = entry.getValue();
            String param;
            param = "," + englishField + " as " + chineseField;
            paramStr += param;
        }
        paramStr+=", Date(created_on) as 监测时间 ";

        String sql = "select id as taskId,TASK_NAME as taskName,category_name as categoryName" + paramStr +
                " from task_configuration " +
                " where id= ?";
        Map<String, Object> map = jdbcBaseDao.getMapResult(sql, taskId);
        return map;
    }
}
