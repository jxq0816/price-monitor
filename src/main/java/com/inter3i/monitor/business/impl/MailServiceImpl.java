package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.MailService;
import com.inter3i.monitor.component.MailHelper;
import com.inter3i.monitor.entity.MonitorAlertEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/20 13:08
 */
@Service
public class MailServiceImpl implements MailService{

    private MailHelper mailHelper;

    @Override
    public Map<String, Object> monitorAlertEmail(List<MonitorAlertEntity> monitorAlertEntityList) {
        Map<String,Object> map = new java.util.HashMap<String,Object>();
        map.put("success",true);
        List<String> successEmailList = new ArrayList<String>();
        List<String> failedEmailList = new ArrayList<String>();
        final String subject = "博晓通价格监测报警通知（系统自动邮件，请勿答复）";
        int count = 0;
        for(MonitorAlertEntity monitorAlertEntity : monitorAlertEntityList){
            System.out.println("正在发送邮件:" + (++count) + "/" + monitorAlertEntityList.size());
            Map<String, Object> model = new java.util.HashMap<String,Object>();
            model.put("alert",monitorAlertEntity);

            Map<String,Object> tempMap = mailHelper.sendHtmlMail(subject,monitorAlertEntity.getEmail(),"velocity/monitorAlertEmail.vm",model);
            Boolean success = (Boolean) tempMap.get("success");
            if(success){
                System.out.println("邮件发送成功:" + monitorAlertEntity.getEmail());
                successEmailList.add(monitorAlertEntity.getEmail());
                map.put("successEmail",successEmailList);
            }else{
                System.out.println("邮件发送失败:" + monitorAlertEntity.getEmail());
                failedEmailList.add(monitorAlertEntity.getEmail());
                map.put("failedEmail",failedEmailList);
                map.put("message",tempMap.get("message"));
            }
        }
        return map;
    }

    public MailHelper getMailHelper() {
        return mailHelper;
    }
    @Autowired
    public void setMailHelper(MailHelper mailHelper) {
        this.mailHelper = mailHelper;
    }

}
