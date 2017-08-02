package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.WarningContentService;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.MonitorAlertEntity;
import com.inter3i.monitor.entity.MoreContent;
import com.inter3i.monitor.entity.WarningContent;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 13:07
 */
@Service
public class WarningContentServiceImpl implements WarningContentService {

    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void buildWarningContent(List<MonitorAlertEntity> monitorAlertEntityList) {
        if(CollectionUtils.isNotEmpty(monitorAlertEntityList)){
            String sql = "INSERT INTO warning_content(task_configuration_zb_id,promotionlnfos,price,created_on) VALUES (:task_configuration_zb_id,:promotionlnfos,:price,:created_on)";
            List<WarningContent> warningContentList = new ArrayList<WarningContent>();
            for (MonitorAlertEntity monitorAlertEntity : monitorAlertEntityList){
                WarningContent warningContent = new WarningContent();
                warningContent.setTaskConfigurationZbId(monitorAlertEntity.getTaskConfigurationZbId());  //电商价格监测任务子表ID
                warningContent.setPromotionlnfos(monitorAlertEntity.getStickPromotioninfos());  //活动内容
                warningContent.setPrice(monitorAlertEntity.getStickPrice());  // 价格(成交价)
                warningContent.setCreatedOn(new Date());  //创建时间
                warningContentList.add(warningContent);
            }
            jdbcBaseDao.executeBach(sql,warningContentList);
        }
    }

    /**
     * 通过子任务id得到预警内容
     *
     * @param taskId 子任务id
     * @param limit 显示条数
     * @return List<ActiveContent> 任务列表
     */
    @Override
    public List<WarningContent> getTaskWarningContent(String taskId,Integer limit) {
        String sql = "select promotionlnfos,price,created_on from warning_content where task_configuration_zb_id =?  ORDER BY created_on desc LIMIT ?";
        return jdbcBaseDao.queryList(WarningContent.class,sql,taskId,limit);
    }

    @Override
    public Page findPage(PageBean pageBean, String childTaskId) {
        String sql = "select promotionlnfos as content,price,created_on as createdOn from warning_content where task_configuration_zb_id =?  ORDER BY created_on desc";
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
