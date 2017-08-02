package com.inter3i.monitor.entity;

import java.util.Date;


public class ProductLibrary implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;  //产品库信息表ID
	private String categoryName;  //品类名称
	private String platform;  //平台
	private String productDescription;  //产品描述
	private String pageUrl;  //网页地址
	private String brand;  //品牌
	private String sellerName;  //商家名称
	private String models;  //款型
	private String year;  //年份
	private String season;  //季节
	private String style;  //风格
	private String features;  //皮质
	private String material;  //材质
	private Boolean isFrame;  //是否下架  0下架1未下架
	private Boolean availability;  //是否可用   0可用1不可用
	private Date createdOn;
	private String distinctValue;



	/**
	 * 描述: 产品库信息表ID
	 */
	public String getId(){
 		return this.id;
	}

	/**
	 * 描述: 产品库信息表ID
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
	 * 描述: 平台
	 */
	public String getPlatform(){
 		return this.platform;
	}

	/**
	 * 描述: 平台
	 */
	public void setPlatform(String platform){
		this.platform = platform;
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
	 * 描述: 品牌
	 */
	public String getBrand(){
 		return this.brand;
	}

	/**
	 * 描述: 品牌
	 */
	public void setBrand(String brand){
		this.brand = brand;
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
	 * 描述: 款型
	 */
	public String getModels(){
 		return this.models;
	}

	/**
	 * 描述: 款型
	 */
	public void setModels(String models){
		this.models = models;
	}

	/**
	 * 描述: 年份
	 */
	public String getYear(){
 		return this.year;
	}

	/**
	 * 描述: 年份
	 */
	public void setYear(String year){
		this.year = year;
	}

	/**
	 * 描述: 季节
	 */
	public String getSeason(){
 		return this.season;
	}

	/**
	 * 描述: 季节
	 */
	public void setSeason(String season){
		this.season = season;
	}

	/**
	 * 描述: 风格
	 */
	public String getStyle(){
 		return this.style;
	}

	/**
	 * 描述: 风格
	 */
	public void setStyle(String style){
		this.style = style;
	}

	/**
	 * 描述: 皮质
	 */
	public String getFeatures(){
 		return this.features;
	}

	/**
	 * 描述: 皮质
	 */
	public void setFeatures(String features){
		this.features = features;
	}

	/**
	 * 描述: 材质
	 */
	public String getMaterial(){
 		return this.material;
	}

	/**
	 * 描述: 材质
	 */
	public void setMaterial(String material){
		this.material = material;
	}

	/**
	 * 描述: 是否下架  0下架1未下架
	 */
	public Boolean getIsFrame(){
 		return this.isFrame;
	}

	/**
	 * 描述: 是否下架  0下架1未下架
	 */
	public void setIsFrame(Boolean isFrame){
		this.isFrame = isFrame;
	}

	/**
	 * 描述: 是否可用   0可用1不可用
	 */
	public Boolean getAvailability(){
 		return this.availability;
	}

	/**
	 * 描述: 是否可用   0可用1不可用
	 */
	public void setAvailability(Boolean availability){
		this.availability = availability;
	}

	public Date getCreatedOn(){
 		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn){
		this.createdOn = createdOn;
	}

	public Boolean getFrame() {
		return isFrame;
	}

	public void setFrame(Boolean frame) {
		isFrame = frame;
	}

	public String getDistinctValue() {
		return distinctValue;
	}

	public void setDistinctValue(String distinctValue) {
		this.distinctValue = distinctValue;
	}
}

