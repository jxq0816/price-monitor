package com.inter3i.monitor.business;

import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.entity.ActiveContent;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 13:04
 */
public interface ActiveContentService {

    /**
     * 构建同一批数据的活动信息
     * @param dataFlag
     */
    void buildActiveContent(String dataFlag);

    /**
     * 通过子任务id得到活动内容
     *
     * @param taskId 子任务id
     * @param limit  条数
     * @return List<ActiveContent> 任务列表
     */
    List<ActiveContent> getTaskActiveContent(String taskId,Integer limit);

    /**
     * 子任务 活动列表分页
     * @param pageBean
     * @param childTaskId
     * @return
     */
    Page findPage(PageBean pageBean, String childTaskId);
}
