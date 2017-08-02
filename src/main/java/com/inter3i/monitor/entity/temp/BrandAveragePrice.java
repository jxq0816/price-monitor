package com.inter3i.monitor.entity.temp;

import java.util.Date;


@Deprecated
public class BrandAveragePrice implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;  //品牌均价变化id
	private String categoryName;  //品类名称
	private String brandName;  //品牌名称
	private Double priceRate;  //均价变化率
	private Double price;  //均价
	private Date createdOn;  //创建时间



	/**
	 * 描述: 品牌均价变化id
	 */
	public Integer getId(){
 		return this.id;
	}

	/**
	 * 描述: 品牌均价变化id
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
	 * 描述: 品牌名称
	 */
	public String getBrandName(){
 		return this.brandName;
	}

	/**
	 * 描述: 品牌名称
	 */
	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	/**
	 * 描述: 均价变化率
	 */
	public Double getPriceRate(){
 		return this.priceRate;
	}

	/**
	 * 描述: 均价变化率
	 */
	public void setPriceRate(Double priceRate){
		this.priceRate = priceRate;
	}

	/**
	 * 描述: 均价
	 */
	public Double getPrice(){
 		return this.price;
	}

	/**
	 * 描述: 均价
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

