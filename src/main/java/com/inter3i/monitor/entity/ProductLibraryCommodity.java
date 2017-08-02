package com.inter3i.monitor.entity;

/*
 * DESCRIPTION : 电商价格监测-产品库与商品表关联表
 * USER : liuxiaolei
 * DATE : 2017/6/16 12:37
 */
public class ProductLibraryCommodity {
    private String pageUrl;  //页面地址
    private Double stickPrice;  //成交价
    private String sellerName;  //商家名称
    private String productDescription;  //产品描述

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Double getStickPrice() {
        return stickPrice;
    }

    public void setStickPrice(Double stickPrice) {
        this.stickPrice = stickPrice;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
