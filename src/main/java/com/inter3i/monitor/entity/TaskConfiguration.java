package com.inter3i.monitor.entity;

import java.util.Date;


public class TaskConfiguration implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;  //任务信息表id
	private String sysUserId;  //用户表ID
	private String taskName;  //任务名称
	private String categoryName;  //品类名称
	private String brand;  //品牌
	private String models;  //款型
	private String year;  //年份
	private String season;  //季节
	private String style;  //风格
	private String features;  //特征
	private String material;  //材质
	private String sellerName;  //商家名称
	private String monitorType;  //监测类型
	private String platform;  //平台
	private String updateFrequency;  //更新频率
	private Boolean warningStatus;  //预警状态0跟踪中1已停止
	private Boolean taskStatus;  //任务状态0启动1停用
	private String email;  //邮箱
	private String userName;  //用户名称
	private Boolean deleteMark;  //删除标识 0删除1未删除
	private Date updateTime;  //修改时间
	private Date createdOn;  //创建时间
	private String taskInfo; //任务信息

	/**
	 * 描述: 任务信息
	 */
	public String getTaskInfo() {
		return taskInfo;
	}

	/**
	 * 描述: 任务信息
	 */
	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}

	/**
	 * 描述: 任务信息表id
	 */
	public String getId(){
 		return this.id;
	}

	/**
	 * 描述: 任务信息表id
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * 描述: 用户表ID
	 */
	public String getSysUserId(){
 		return this.sysUserId;
	}

	/**
	 * 描述: 用户表ID
	 */
	public void setSysUserId(String sysUserId){
		this.sysUserId = sysUserId;
	}

	/**
	 * 描述: 任务名称
	 */
	public String getTaskName(){
 		return this.taskName;
	}

	/**
	 * 描述: 任务名称
	 */
	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	/**
	 * 描述: 品类名称
	 */
	public String getCategoryName(){
 		return this.categoryName;
	}

	/**
	 * 描述: 品类名称
	 */
	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	/**
	 * 描述: 品牌
	 */
	public String getBrand(){
 		return this.brand;
	}

	/**
	 * 描述: 品牌
	 */
	public void setBrand(String brand){
		this.brand = brand;
	}

	/**
	 * 描述: 款型
	 */
	public String getModels(){
 		return this.models;
	}

	/**
	 * 描述: 款型
	 */
	public void setModels(String models){
		this.models = models;
	}

	/**
	 * 描述: 年份
	 */
	public String getYear(){
 		return this.year;
	}

	/**
	 * 描述: 年份
	 */
	public void setYear(String year){
		this.year = year;
	}

	/**
	 * 描述: 季节
	 */
	public String getSeason(){
 		return this.season;
	}

	/**
	 * 描述: 季节
	 */
	public void setSeason(String season){
		this.season = season;
	}

	/**
	 * 描述: 风格
	 */
	public String getStyle(){
 		return this.style;
	}

	/**
	 * 描述: 风格
	 */
	public void setStyle(String style){
		this.style = style;
	}

	/**
	 * 描述: 特征
	 */
	public String getFeatures(){
 		return this.features;
	}

	/**
	 * 描述: 特征
	 */
	public void setFeatures(String features){
		this.features = features;
	}

	/**
	 * 描述: 材质
	 */
	public String getMaterial(){
 		return this.material;
	}

	/**
	 * 描述: 材质
	 */
	public void setMaterial(String material){
		this.material = material;
	}

	/**
	 * 描述: 商家名称
	 */
	public String getSellerName(){
 		return this.sellerName;
	}

	/**
	 * 描述: 商家名称
	 */
	public void setSellerName(String sellerName){
		this.sellerName = sellerName;
	}

	/**
	 * 描述: 监测类型
	 */
	public String getMonitorType(){
 		return this.monitorType;
	}

	/**
	 * 描述: 监测类型
	 */
	public void setMonitorType(String monitorType){
		this.monitorType = monitorType;
	}

	/**
	 * 描述: 平台
	 */
	public String getPlatform(){
 		return this.platform;
	}

	/**
	 * 描述: 平台
	 */
	public void setPlatform(String platform){
		this.platform = platform;
	}

	/**
	 * 描述: 更新频率
	 */
	public String getUpdateFrequency(){
 		return this.updateFrequency;
	}

	/**
	 * 描述: 更新频率
	 */
	public void setUpdateFrequency(String updateFrequency){
		this.updateFrequency = updateFrequency;
	}

	/**
	 * 描述: 预警状态0跟踪中1已停止
	 */
	public Boolean getWarningStatus(){
 		return this.warningStatus;
	}

	/**
	 * 描述: 预警状态0跟踪中1已停止
	 */
	public void setWarningStatus(Boolean warningStatus){
		this.warningStatus = warningStatus;
	}

	/**
	 * 描述: 任务状态0启动1停用
	 */
	public Boolean getTaskStatus(){
 		return this.taskStatus;
	}

	/**
	 * 描述: 任务状态0启动1停用
	 */
	public void setTaskStatus(Boolean taskStatus){
		this.taskStatus = taskStatus;
	}

	/**
	 * 描述: 邮箱
	 */
	public String getEmail(){
 		return this.email;
	}

	/**
	 * 描述: 邮箱
	 */
	public void setEmail(String email){
		this.email = email;
	}

	/**
	 * 描述: 用户名称
	 */
	public String getUserName(){
 		return this.userName;
	}

	/**
	 * 描述: 用户名称
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}

	/**
	 * 描述: 删除标识 0删除1未删除
	 */
	public Boolean getDeleteMark(){
 		return this.deleteMark;
	}

	/**
	 * 描述: 删除标识 0删除1未删除
	 */
	public void setDeleteMark(Boolean deleteMark){
		this.deleteMark = deleteMark;
	}

	/**
	 * 描述: 修改时间
	 */
	public Date getUpdateTime(){
 		return this.updateTime;
	}

	/**
	 * 描述: 修改时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	/**
	 * 描述: 创建时间
	 */
	public Date getCreatedOn(){
 		return this.createdOn;
	}

	/**
	 * 描述: 创建时间
	 */
	public void setCreatedOn(Date createdOn){
		this.createdOn = createdOn;
	}


}

