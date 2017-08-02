package com.inter3i.monitor.entity.temp;

import java.util.Date;


@Deprecated
public class PriceChangeRatio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;  //价格变化占比信息表id
	private String categoryName;  //品类名称
	private Double changeRatio;  // 品类当日变化占比
	private String productDescription;  //产品描述
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
	 * 描述:  品类当日变化占比
	 */
	public Double getChangeRatio(){
 		return this.changeRatio;
	}

	/**
	 * 描述:  品类当日变化占比
	 */
	public void setChangeRatio(Double changeRatio){
		this.changeRatio = changeRatio;
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

