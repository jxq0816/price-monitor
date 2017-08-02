package com.inter3i.monitor.entity;

import java.util.Date;


public class Category implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;  //品类信息表ID
	private String categoryName;  //品类名称
	private String categoryNameExplain;  //品类名称解释
	private Integer level;  //级别
	private Date createdOn;  //创建时间



	/**
	 * 描述: 品类信息表ID
	 */
	public String getId(){
 		return this.id;
	}

	/**
	 * 描述: 品类信息表ID
	 */
	public void setId(String id){
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
	 * 描述: 品类名称解释
	 */
	public String getCategoryNameExplain(){
 		return this.categoryNameExplain;
	}

	/**
	 * 描述: 品类名称解释
	 */
	public void setCategoryNameExplain(String categoryNameExplain){
		this.categoryNameExplain = categoryNameExplain;
	}

	/**
	 * 描述: 级别
	 */
	public Integer getLevel(){
 		return this.level;
	}

	/**
	 * 描述: 级别
	 */
	public void setLevel(Integer level){
		this.level = level;
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

