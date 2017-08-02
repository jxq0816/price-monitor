package com.inter3i.monitor.business;

import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.entity.TaskConfigurationZb;

import java.util.List;
import java.util.Map;

/**
 * Created by boxiaotong on 2017/6/14.
 */
public interface TaskConfigurationChildService {
    /**
     * 根据父任务ID,查询所有的子任务
     * @param taskId
     * @return
     */
    List<TaskConfigurationZb> findList(String taskId);

    /**
     * 根绝子任务ID，获取对应的子任务
     * @param childTaskId
     * @return
     */
    TaskConfigurationZb findOne(String childTaskId);

    /**
     * 根绝子任务ID，获取对应的子任务
     * @param childTaskId
     * @return
     */
    Map<String,Object> findOneMap(String childTaskId);

    /**
     * 根据父任务ID,查询所有的子任务,但不支持分页，故废弃
     * @param taskId
     * @return
     */
    List<Map<String,Object>> queryMapList(String taskId);

    Page findPage(PageBean pageBean, String taskId,String orderParam,String orderType);

    List priceTrend(String childTaskId);

    List supplementPriceTrend(List<Map<String,Object>> list,String childTaskId);
}
