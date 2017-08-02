package com.inter3i.monitor.entity;

import java.util.Date;


public class Commodity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;  //商品信息表ID
	private String parentId;  //Mongodb外键ID
	private String topicCategory;  //产品分类
	private String productCategory;  //产品描述
	private Double originalPrice;  //原价
	private Double discountPrice;  //促销价
	private String promotioninfos;  //促销信息
	private Double stickPrice;  //成交价
	private String stickPromotioninfos;  //成交价促销信息
	private String keywords;  //关键词
	private String pageUrl;  //页面地址
	private Date lastExtractTime;  //创建时间
	private String lastDataFlag;
	private Long storageTime;



	/**
	 * 描述: 商品信息表ID
	 */
	public Integer getId(){
 		return this.id;
	}

	/**
	 * 描述: 商品信息表ID
	 */
	public void setId(Integer id){
		this.id = id;
	}

	/**
	 * 描述: Mongodb外键ID
	 */
	public String getParentId(){
 		return this.parentId;
	}

	/**
	 * 描述: Mongodb外键ID
	 */
	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	/**
	 * 描述: 产品分类
	 */
	public String getTopicCategory(){
 		return this.topicCategory;
	}

	/**
	 * 描述: 产品分类
	 */
	public void setTopicCategory(String topicCategory){
		this.topicCategory = topicCategory;
	}

	/**
	 * 描述: 产品描述
	 */
	public String getProductCategory(){
 		return this.productCategory;
	}

	/**
	 * 描述: 产品描述
	 */
	public void setProductCategory(String productCategory){
		this.productCategory = productCategory;
	}

	/**
	 * 描述: 原价
	 */
	public Double getOriginalPrice(){
 		return this.originalPrice;
	}

	/**
	 * 描述: 原价
	 */
	public void setOriginalPrice(Double originalPrice){
		this.originalPrice = originalPrice;
	}

	/**
	 * 描述: 促销价
	 */
	public Double getDiscountPrice(){
 		return this.discountPrice;
	}

	/**
	 * 描述: 促销价
	 */
	public void setDiscountPrice(Double discountPrice){
		this.discountPrice = discountPrice;
	}

	/**
	 * 描述: 促销信息
	 */
	public String getPromotioninfos(){
 		return this.promotioninfos;
	}

	/**
	 * 描述: 促销信息
	 */
	public void setPromotioninfos(String promotioninfos){
		this.promotioninfos = promotioninfos;
	}

	/**
	 * 描述: 成交价
	 */
	public Double getStickPrice(){
 		return this.stickPrice;
	}

	/**
	 * 描述: 成交价
	 */
	public void setStickPrice(Double stickPrice){
		this.stickPrice = stickPrice;
	}

	/**
	 * 描述: 成交价促销信息
	 */
	public String getStickPromotioninfos(){
 		return this.stickPromotioninfos;
	}

	/**
	 * 描述: 成交价促销信息
	 */
	public void setStickPromotioninfos(String stickPromotioninfos){
		this.stickPromotioninfos = stickPromotioninfos;
	}

	/**
	 * 描述: 关键词
	 */
	public String getKeywords(){
 		return this.keywords;
	}

	/**
	 * 描述: 关键词
	 */
	public void setKeywords(String keywords){
		this.keywords = keywords;
	}

	/**
	 * 描述: 页面地址
	 */
	public String getPageUrl(){
 		return this.pageUrl;
	}

	/**
	 * 描述: 页面地址
	 */
	public void setPageUrl(String pageUrl){
		this.pageUrl = pageUrl;
	}

	/**
	 * 描述: 创建时间
	 */
	public Date getLastExtractTime(){
 		return this.lastExtractTime;
	}

	/**
	 * 描述: 创建时间
	 */
	public void setLastExtractTime(Date lastExtractTime){
		this.lastExtractTime = lastExtractTime;
	}

	public String getLastDataFlag(){
 		return this.lastDataFlag;
	}

	public void setLastDataFlag(String lastDataFlag){
		this.lastDataFlag = lastDataFlag;
	}

	public Long getStorageTime(){
 		return this.storageTime;
	}

	public void setStorageTime(Long storageTime){
		this.storageTime = storageTime;
	}


}

