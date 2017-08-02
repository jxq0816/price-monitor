package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.TaskConfigurationZbService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.MonitorAlertEntity;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 13:06
 */
@Service
public class TaskConfigurationZbServiceImpl implements TaskConfigurationZbService{

    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void updateAlertInfo(List<MonitorAlertEntity> monitorAlertEntityList) {
        if(CollectionUtils.isNotEmpty(monitorAlertEntityList)){
            String sql = "UPDATE task_configuration_zb SET warning_number = (IFNULL(warning_number,0) + 1),warning_time = NOW() WHERE id = ?";
            for(MonitorAlertEntity monitorAlertEntity : monitorAlertEntityList){
                if(monitorAlertEntity == null){
                    continue;
                }
                jdbcBaseDao.executeUpdate(sql,monitorAlertEntity.getTaskConfigurationZbId());
            }
        }
    }

    public JDBCBaseDao getJdbcBaseDao() {
        return jdbcBaseDao;
    }
    @Autowired
    public void setJdbcBaseDao(JDBCBaseDao jdbcBaseDao) {
        this.jdbcBaseDao = jdbcBaseDao;
    }
}
