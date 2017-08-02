package com.inter3i.monitor.entity;

import java.util.Date;


public class PriceChangeRank implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;  //价格变化占比信息表id
	private String categoryName;  //品类名称
	private Double transactionPrice;  //减价
	private String productDescription;  //产品描述
	private String pageUrl;  //产品url
	private Date createdOn;  //创建时间



	/**
	 * 描述: 价格变化占比信息表id
	 */
	public Integer getId(){
 		return this.id;
	}

	/**
	 * 描述: 价格变化占比信息表id
	 */
	public void setId(Integer id){
		this.id = id;
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
	 * 描述: 减价
	 */
	public Double getTransactionPrice(){
 		return this.transactionPrice;
	}

	/**
	 * 描述: 减价
	 */
	public void setTransactionPrice(Double transactionPrice){
		this.transactionPrice = transactionPrice;
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
	 * 描述: 该产品的url
	 */
	public String getPageUrl(){
		return this.pageUrl;
	}

	/**
	 * 描述: 该产品的url
	 */
	public void setPageUrl(String pageUrl){
		this.pageUrl = pageUrl;
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

