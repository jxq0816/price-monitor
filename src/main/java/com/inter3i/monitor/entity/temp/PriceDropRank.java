package com.inter3i.monitor.entity.temp;

import java.util.Date;


@Deprecated
public class PriceDropRank implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;  //产品价格降幅排名id
	private String categoryName;  //品类名称
	private String brandName;  //品牌名称
	private Double newestTransactionPrice;  //最新成交价
	private Double priceRate;  //均价变化率
	private String productDescription;  //产品描述
	private Date createdOn;  //创建时间



	/**
	 * 描述: 产品价格降幅排名id
	 */
	public Integer getId(){
 		return this.id;
	}

	/**
	 * 描述: 产品价格降幅排名id
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
	 * 描述: 最新成交价
	 */
	public Double getNewestTransactionPrice(){
 		return this.newestTransactionPrice;
	}

	/**
	 * 描述: 最新成交价
	 */
	public void setNewestTransactionPrice(Double newestTransactionPrice){
		this.newestTransactionPrice = newestTransactionPrice;
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

