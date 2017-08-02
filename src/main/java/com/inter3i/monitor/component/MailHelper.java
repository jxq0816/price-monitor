package com.inter3i.monitor.component;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

import javax.mail.internet.MimeMessage;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/15 10:55
 */
@org.springframework.stereotype.Component
public class MailHelper {

    private static final String EMAIL_FROM_PERSON = "北京博晓通科技有限公司";

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private VelocityEngine velocityEngine;


    /**
     * 根据velocity的邮件模板发送邮件。
     * @param subject 主题
     * @param email 收件人邮箱
     * @param viewPath 模板的路径
     * @param model 模板中需要替换的属性
     * @return 发送结果
     */
    public Map<String,Object> sendHtmlMail(final String subject,final String email,String viewPath,Map<String, Object> model){
        final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, viewPath,"UTF-8",model);
        return sendMail(subject,text,email);
    }


    private Map<String,Object> sendMail(final String subject,final String text,final String email) {
        Map<String,Object> map = new java.util.HashMap<String,Object>();
        map.put("success",false);
        try {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    message.setTo(email);
                    message.setFrom(mailSender.getUsername(), EMAIL_FROM_PERSON);
                    message.setSubject(subject);
                    message.setText(text, true);
                }
            };
            this.mailSender.send(preparator);
            map.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message",e.getMessage());
        }
        return map;
    }

}
