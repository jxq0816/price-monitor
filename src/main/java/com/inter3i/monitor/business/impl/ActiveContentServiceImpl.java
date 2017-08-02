package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.ActiveContentService;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.ActiveContent;
import com.inter3i.monitor.entity.MoreContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 13:06
 */
@Service
public class ActiveContentServiceImpl implements ActiveContentService{

    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void buildActiveContent(String dataFlag) {
        String sql = "INSERT INTO active_content ( task_configuration_zb_id, promotionlnfos, price, created_on ) SELECT b.id AS task_configuration_zb_id, a.stick_promotionInfos, a.stick_price, NOW() FROM commodity a INNER JOIN task_configuration_zb b ON a.page_url = b.page_url WHERE a.last_data_flag = ? AND b.delete_mark = 1 AND b.task_status = 0 AND a.stick_promotionInfos IS NOT NULL AND a.stick_promotionInfos <> '' AND a.stick_price IS NOT NULL";
        jdbcBaseDao.executeUpdate(sql,dataFlag);
    }

    /**
     * 通过子任务id得到活动内容
     *
     * @param taskId 子任务id
     * @return List<ActiveContent> 任务列表
     */
    @Override
    public List<ActiveContent> getTaskActiveContent(String taskId,Integer limit) {
        String sql = "select promotionlnfos ,price,created_on from active_content where task_configuration_zb_id =?  ORDER BY created_on desc LIMIT ?";
        return jdbcBaseDao.queryList(ActiveContent.class,sql,taskId,limit);
    }

    @Override
    public Page findPage(PageBean pageBean, String childTaskId) {
        String sql = "select promotionlnfos as content,price,created_on as createdOn from active_content where task_configuration_zb_id =?  ORDER BY created_on desc";
        Page page=jdbcBaseDao.queryPage(MoreContent.class, pageBean,sql,childTaskId);
        return page;
    }

    public JDBCBaseDao getJdbcBaseDao() {
        return jdbcBaseDao;
    }
    @Autowired
    public void setJdbcBaseDao(JDBCBaseDao jdbcBaseDao) {
        this.jdbcBaseDao = jdbcBaseDao;
    }
}
