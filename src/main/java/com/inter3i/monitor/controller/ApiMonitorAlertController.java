package com.inter3i.monitor.controller;

import com.inter3i.monitor.business.ActiveContentService;
import com.inter3i.monitor.business.ApiMonitorAlertService;
import com.inter3i.monitor.business.CommodityService;
import com.inter3i.monitor.business.MailService;
import com.inter3i.monitor.business.TaskConfigurationZbService;
import com.inter3i.monitor.business.TaskLogService;
import com.inter3i.monitor.business.WarningContentService;
import com.inter3i.monitor.entity.APIResponse;
import com.inter3i.monitor.entity.MonitorAlertEntity;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/15 12:34
 */
@Controller
@RequestMapping("/api/monitor")
public class ApiMonitorAlertController {

    private ApiMonitorAlertService apiMonitorAlertService;
    private CommodityService commodityService;
    private MailService mailService;
    private TaskLogService taskLogService;
    private ActiveContentService activeContentService;
    private TaskConfigurationZbService taskConfigurationZbService;
    private WarningContentService warningContentService;

    @ResponseBody
    @RequestMapping(value="/handle",method = RequestMethod.POST)
    public APIResponse handle(String dataFlag) throws IOException {
        System.out.println("======================进入handle方法===========================");
        long begin = new Date().getTime();
        APIResponse apiResponse = new APIResponse();
        //删除已经下架的商品数据
        apiMonitorAlertService.deleteOffProductData(dataFlag);
        //抽取满减信息，填充到空缺字段
        apiMonitorAlertService.buildPromotionPrice(dataFlag);
        //删除成交价为空的数据
        commodityService.deleteNullStickPrice(dataFlag);
        //删除成交价为空的数据
        commodityService.deleteDuplicate();
        //填充活动信息，此时不需要预警
        activeContentService.buildActiveContent(dataFlag);
        //获取符合条件的预警数据
        List<MonitorAlertEntity> monitorAlertEntityList = apiMonitorAlertService.queryMonitorAlertEntityList(dataFlag);
        if(CollectionUtils.isNotEmpty(monitorAlertEntityList)){
            //记录预警信息
            warningContentService.buildWarningContent(monitorAlertEntityList);
            //更新子任务表
            taskConfigurationZbService.updateAlertInfo(monitorAlertEntityList);
            //发送邮件
            Map<String,Object> map = mailService.monitorAlertEmail(monitorAlertEntityList);
            //记录邮件日志
            taskLogService.addTaskLog(monitorAlertEntityList,map);
            apiResponse.setStatusCode(HttpStatus.OK.value());
        }else{
            apiResponse.setMessage("没有预警数据");
            apiResponse.setStatusCode(HttpStatus.OK.value());
        }
        long end = new Date().getTime();
        apiResponse.setTime(end - begin);
        System.out.println("==============================handle方法结束，耗时"+(end - begin) / 1000+"s=====================================");
        return apiResponse;
    }


    public ApiMonitorAlertService getApiMonitorAlertService() {
        return apiMonitorAlertService;
    }
    @Autowired
    public void setApiMonitorAlertService(ApiMonitorAlertService apiMonitorAlertService) {
        this.apiMonitorAlertService = apiMonitorAlertService;
    }

    public CommodityService getCommodityService() {
        return commodityService;
    }
    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    public MailService getMailService() {
        return mailService;
    }
    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public TaskLogService getTaskLogService() {
        return taskLogService;
    }
    @Autowired
    public void setTaskLogService(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    public ActiveContentService getActiveContentService() {
        return activeContentService;
    }
    @Autowired
    public void setActiveContentService(ActiveContentService activeContentService) {
        this.activeContentService = activeContentService;
    }

    public TaskConfigurationZbService getTaskConfigurationZbService() {
        return taskConfigurationZbService;
    }
    @Autowired
    public void setTaskConfigurationZbService(TaskConfigurationZbService taskConfigurationZbService) {
        this.taskConfigurationZbService = taskConfigurationZbService;
    }

    public WarningContentService getWarningContentService() {
        return warningContentService;
    }
    @Autowired
    public void setWarningContentService(WarningContentService warningContentService) {
        this.warningContentService = warningContentService;
    }
}
