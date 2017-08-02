package com.inter3i.monitor.entity;

import java.util.Date;


public class MoreContent implements java.io.Serializable {
	public static final String WARNING_TYPE="0";//预警
	public static final String ACTIVE_TYPE="1";//活动
	private String content;  //活动内容
	private Double price;  // 价格(成交价)
	private Date createdOn;  //创建时间

	private static final long serialVersionUID = 1L;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


}

