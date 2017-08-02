package com.inter3i.monitor.entity.temp;

import java.util.Date;


@Deprecated
public class CategoryAttribute implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;  //品类属性信息表ID
	private String categoryId;  //品类名称
	private String chineseField;  //中文字段
	private String englishField;  //英文字段
	private Integer level;  //级别
	private Date createdOn;  //创建时间



	/**
	 * 描述: 品类属性信息表ID
	 */
	public String getId(){
 		return this.id;
	}

	/**
	 * 描述: 品类属性信息表ID
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * 描述: 品类名称
	 */
	public String getCategoryId(){
 		return this.categoryId;
	}

	/**
	 * 描述: 品类名称
	 */
	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	/**
	 * 描述: 中文字段
	 */
	public String getChineseField(){
 		return this.chineseField;
	}

	/**
	 * 描述: 中文字段
	 */
	public void setChineseField(String chineseField){
		this.chineseField = chineseField;
	}

	/**
	 * 描述: 英文字段
	 */
	public String getEnglishField(){
 		return this.englishField;
	}

	/**
	 * 描述: 英文字段
	 */
	public void setEnglishField(String englishField){
		this.englishField = englishField;
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

