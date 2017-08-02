package com.inter3i.monitor.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/16 11:20
 */
public class MonitorAlertEntity {

    private Integer commodityId;
    private String taskConfigurationZbId;
    private String sysUserId;
    private String userName;
    private String email;
    private String taskName;
    private Date createdOn;
    private String createdOnStr;
    private String categoryName;
    private String brand;
    private String models;
    private String season;
    private String style;
    private String features;
    private String material;
    private Double warningPrice;
    private Double stickPrice;
    private String stickPromotioninfos;  //成交价促销信息
    private String pageUrl;

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getTaskConfigurationZbId() {
        return taskConfigurationZbId;
    }

    public void setTaskConfigurationZbId(String taskConfigurationZbId) {
        this.taskConfigurationZbId = taskConfigurationZbId;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getWarningPrice() {
        return warningPrice;
    }

    public void setWarningPrice(Double warningPrice) {
        this.warningPrice = warningPrice;
    }

    public Double getStickPrice() {
        return stickPrice;
    }

    public void setStickPrice(Double stickPrice) {
        this.stickPrice = stickPrice;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getCreatedOnStr() {
        this.createdOnStr = new SimpleDateFormat("yyyy年MM月dd日").format(this.createdOn);
        return createdOnStr;
    }

    public void setCreatedOnStr(String createdOnStr) {
        this.createdOnStr = createdOnStr;
    }

    public String getStickPromotioninfos() {
        return stickPromotioninfos;
    }

    public void setStickPromotioninfos(String stickPromotioninfos) {
        this.stickPromotioninfos = stickPromotioninfos;
    }
}
