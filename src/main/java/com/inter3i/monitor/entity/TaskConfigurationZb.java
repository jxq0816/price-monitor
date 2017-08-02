package com.inter3i.monitor.entity;

import java.util.Date;


public class TaskConfigurationZb implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;  //电商价格监测任务子表id
	private String taskConfigurationId;  //电商价格监测任务信息表ID
	private String productDescription;  //产品描述
	private String sellerName;  //商家名称
	private Double warningPrice;  // 预警价格
	private Integer warningNumber;  //预警次数
	private Date warningTime;  //预警时间
	private String pageUrl;  //网页地址
	private Boolean taskStatus;  //0 启动1停用
	private Boolean deleteMark;
	private Date updateTime;  //修改时间
	private Date createdOn;  //创建时间
	private Double stickPrice;  //成交价


	public Double getStickPrice() {
		return stickPrice;
	}

	public void setStickPrice(Double stickPrice) {
		this.stickPrice = stickPrice;
	}

	/**
	 * 描述: 电商价格监测任务子表id
	 */
	public String getId(){
 		return this.id;
	}

	/**
	 * 描述: 电商价格监测任务子表id
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * 描述: 电商价格监测任务信息表ID
	 */
	public String getTaskConfigurationId(){
 		return this.taskConfigurationId;
	}

	/**
	 * 描述: 电商价格监测任务信息表ID
	 */
	public void setTaskConfigurationId(String taskConfigurationId){
		this.taskConfigurationId = taskConfigurationId;
	}

	/**
	 * 描述: 产品描述
	 */
	public String getProductDescription(){
 		return this.productDescription;
	}

	/**
	 * 描述: 产品描述
	 */
	public void setProductDescription(String productDescription){
		this.productDescription = productDescription;
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
	 * 描述:  预警价格
	 */
	public Double getWarningPrice(){
 		return this.warningPrice;
	}

	/**
	 * 描述:  预警价格
	 */
	public void setWarningPrice(Double warningPrice){
		this.warningPrice = warningPrice;
	}

	/**
	 * 描述: 预警次数
	 */
	public Integer getWarningNumber(){
 		return this.warningNumber;
	}

	/**
	 * 描述: 预警次数
	 */
	public void setWarningNumber(Integer warningNumber){
		this.warningNumber = warningNumber;
	}

	/**
	 * 描述: 预警时间
	 */
	public Date getWarningTime(){
 		return this.warningTime;
	}

	/**
	 * 描述: 预警时间
	 */
	public void setWarningTime(Date warningTime){
		this.warningTime = warningTime;
	}

	/**
	 * 描述: 网页地址
	 */
	public String getPageUrl(){
 		return this.pageUrl;
	}

	/**
	 * 描述: 网页地址
	 */
	public void setPageUrl(String pageUrl){
		this.pageUrl = pageUrl;
	}

	/**
	 * 描述: 0 启动1停用
	 */
	public Boolean getTaskStatus(){
 		return this.taskStatus;
	}

	/**
	 * 描述: 0 启动1停用
	 */
	public void setTaskStatus(Boolean taskStatus){
		this.taskStatus = taskStatus;
	}

	public Boolean getDeleteMark(){
 		return this.deleteMark;
	}

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

