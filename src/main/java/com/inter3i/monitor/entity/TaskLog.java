package com.inter3i.monitor.entity;

import java.util.Date;


public class TaskLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;  //任务发送日志id
	private String taskConfigurationZbId;  //电商价格监测任务子表ID
	private String sysUserId;
	private Integer commodityId;
	private String taskName;  //任务名称
	private String receiveEmail;  //接收人邮箱
	private String receiveName;  //接收人名称
	private Date createdOn;  //创建时间
	private Boolean success; //是否邮件发送成功



	/**
	 * 描述: 任务发送日志id
	 */
	public String getId(){
 		return this.id;
	}

	/**
	 * 描述: 任务发送日志id
	 */
	public void setId(String id){
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

	public String getSysUserId(){
 		return this.sysUserId;
	}

	public void setSysUserId(String sysUserId){
		this.sysUserId = sysUserId;
	}

	public Integer getCommodityId(){
 		return this.commodityId;
	}

	public void setCommodityId(Integer commodityId){
		this.commodityId = commodityId;
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
	 * 描述: 接收人邮箱
	 */
	public String getReceiveEmail(){
 		return this.receiveEmail;
	}

	/**
	 * 描述: 接收人邮箱
	 */
	public void setReceiveEmail(String receiveEmail){
		this.receiveEmail = receiveEmail;
	}

	/**
	 * 描述: 接收人名称
	 */
	public String getReceiveName(){
 		return this.receiveName;
	}

	/**
	 * 描述: 接收人名称
	 */
	public void setReceiveName(String receiveName){
		this.receiveName = receiveName;
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


	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}

