package com.inter3i.monitor.entity;

import java.util.Date;

/**
 * Created by jiangxingqi on 2017/6/16.
 */
public class TaskChild {
    private String childTaskId;
    private String taskId;
    private String categoryName;
    private String brand;
    private String productDescription;
    private String platform;
    private String sellerName;
    private String monitorType;
    private Double warningPrice;
    private int warningNumber;
    private Date warningTime;

    public Double getWarningPrice() {
        return warningPrice;
    }

    public void setWarningPrice(Double warningPrice) {
        this.warningPrice = warningPrice;
    }

    public int getWarningNumber() {
        return warningNumber;
    }

    public void setWarningNumber(int warningNumber) {
        this.warningNumber = warningNumber;
    }

    public Date getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(Date warningTime) {
        this.warningTime = warningTime;
    }

    public String getChildTaskId() {
        return childTaskId;
    }

    public void setChildTaskId(String childTaskId) {
        this.childTaskId = childTaskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
