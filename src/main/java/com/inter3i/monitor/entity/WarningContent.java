package com.inter3i.monitor.entity;

import java.util.Date;


public class WarningContent implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;  //预警内容信息表id
	private String taskConfigurationZbId;  //电商价格监测任务子表ID
	private String promotionlnfos;  //活动内容
	private Double price;  // 价格(成交价)
	private Date createdOn;  //创建时间



	/**
	 * 描述: 预警内容信息表id
	 */
	public Integer getId(){
 		return this.id;
	}

	/**
	 * 描述: 预警内容信息表id
	 */
	public void setId(Integer id){
		this.id = id;
	}

	/**
	 * 描述: 电商价格监测任务子表ID
	 */
	public String getTaskConfigurationZbId(){
 		return this.taskConfigurationZbId;
	}

	/**
	 * 描述: 电商价格监测任务子表ID
	 */
	public void setTaskConfigurationZbId(String taskConfigurationZbId){
		this.taskConfigurationZbId = taskConfigurationZbId;
	}

	/**
	 * 描述: 活动内容
	 */
	public String getPromotionlnfos(){
 		return this.promotionlnfos;
	}

	/**
	 * 描述: 活动内容
	 */
	public void setPromotionlnfos(String promotionlnfos){
		this.promotionlnfos = promotionlnfos;
	}

	/**
	 * 描述:  价格(成交价)
	 */
	public Double getPrice(){
 		return this.price;
	}

	/**
	 * 描述:  价格(成交价)
	 */
	public void setPrice(Double price){
		this.price = price;
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

