package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.MonitorAlertEntity;

import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/20 13:07
 */
public interface MailService {

    /**
     * 发送预警邮件接口
     * @param monitorAlertEntityList 需要预警的对象类
     * @return
     */
    Map<String, Object> monitorAlertEmail(List<MonitorAlertEntity> monitorAlertEntityList);

}