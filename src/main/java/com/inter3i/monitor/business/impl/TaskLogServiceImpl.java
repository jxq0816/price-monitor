package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.TaskLogService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.MonitorAlertEntity;
import com.inter3i.monitor.entity.TaskLog;
import com.inter3i.monitor.util.UUIDUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 12:19
 */
@Service
public class TaskLogServiceImpl implements TaskLogService{

    private JDBCBaseDao jdbcBaseDao;

    @Override
    public boolean addTaskLog(List<MonitorAlertEntity> monitorAlertEntityList,Map<String,Object> map) {
        if(CollectionUtils.isNotEmpty(monitorAlertEntityList)){
            List<String> successEmailList = new ArrayList<String>();

            Object successEmailObject = map.get("successEmail");
            if(successEmailObject != null && successEmailObject instanceof List){
                successEmailList.addAll((List<String>) successEmailObject);
            }

            String sql = "INSERT task_log(id,task_configuration_zb_id,sys_user_id,commodity_id,task_name,receive_email,receive_name,created_on,success) VALUES(:id,:task_configuration_zb_id,:sys_user_id,:commodity_id,:task_name,:receive_email,:receive_name,:created_on,:success)";
            List<TaskLog> taskLogList = new ArrayList<TaskLog>();
            for (MonitorAlertEntity monitorAlertEntity : monitorAlertEntityList){
                boolean success = (successEmailList.contains(monitorAlertEntity.getEmail()));
                TaskLog taskLog = new TaskLog();
                taskLog.setId(UUIDUtil.getUUID());  //任务发送日志id
                taskLog.setTaskConfigurationZbId(monitorAlertEntity.getTaskConfigurationZbId());  //电商价格监测任务子表ID
                taskLog.setSysUserId(monitorAlertEntity.getSysUserId());
                taskLog.setCommodityId(monitorAlertEntity.getCommodityId());
                taskLog.setTaskName(monitorAlertEntity.getTaskName());  //任务名称
                taskLog.setReceiveEmail(monitorAlertEntity.getEmail());  //接收人邮箱
                taskLog.setReceiveName(monitorAlertEntity.getUserName());  //接收人名称
                taskLog.setCreatedOn(new Date());  //创建时间
                taskLog.setSuccess(success);
                taskLogList.add(taskLog);
            }
            jdbcBaseDao.executeBach(sql,taskLogList);
        }
        return true;
    }

    public JDBCBaseDao getJdbcBaseDao() {
        return jdbcBaseDao;
    }
    @Autowired
    public void setJdbcBaseDao(JDBCBaseDao jdbcBaseDao) {
        this.jdbcBaseDao = jdbcBaseDao;
    }
}
