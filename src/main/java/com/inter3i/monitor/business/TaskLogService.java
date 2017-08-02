package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.MonitorAlertEntity;

import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 12:19
 */
public interface TaskLogService {

    /**
     * 插入预警日志
     * @param monitorAlertEntityList 预警的对象集合
     * @return 是否成功
     */
    boolean addTaskLog(List<MonitorAlertEntity> monitorAlertEntityList,Map<String,Object> map);
}
