package com.inter3i.monitor.entity;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/11 15:03
 */
public class APIResponse {

    private Integer statusCode;
    private String message;
    private String datas;
    private Long time;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
