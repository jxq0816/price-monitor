package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.MonitorAlertEntity;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 13:05
 */
public interface TaskConfigurationZbService {

    void updateAlertInfo(List<MonitorAlertEntity> monitorAlertEntityList);
}
