package com.inter3i.monitor.util;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/25 15:15
 */
public class SMSUtil {

    public static void main(String[] args) {
        String host = "http://sms.market.alicloudapi.com";
        String path = "/singleSendSms";
        String method = "GET";
        String appcode = "141487c53ba442c6b332a0fbfb799838";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("ParamString", "{\"no\":\"123456\"}");
        querys.put("RecNum", "15313196319");
        querys.put("SignName", "博晓通");
        querys.put("TemplateCode", "短信验证码${no}");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
