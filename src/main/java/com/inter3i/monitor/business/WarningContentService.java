package com.inter3i.monitor.business;

import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.entity.MonitorAlertEntity;
import com.inter3i.monitor.entity.WarningContent;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 13:05
 */
public interface WarningContentService {

    /**
     * 构建同一批次的预警信息
     * @param monitorAlertEntityList 需要预警的对象集合
     */
    void buildWarningContent(List<MonitorAlertEntity> monitorAlertEntityList);

    /**
     * 通过子任务id得到预警内容
     *
     * @param taskId 子任务id
     * @param  limit 显示条数
     * @return List<ActiveContent> 任务列表
     */
    List<WarningContent> getTaskWarningContent(String taskId,Integer limit);

    Page findPage(PageBean pageBean, String childTaskId);
}
