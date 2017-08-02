package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.ProductLibraryService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.ProductLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by boxiaotong on 2017/6/16.
 */
@Service
public class ProductLibraryServiceImpl implements ProductLibraryService{

    //产品库中属性的定义
    private final int IS_FRAME = 1;              //1.未下架  0.下架
    private final int IS_AVAILABILITY = 0;     //1.不可用  0.可用

    @Autowired
    private JDBCBaseDao dao;

    public ProductLibrary findProduct(String url){
        String sql="select brand,seller_name from product_library where url = ? ";
        ProductLibrary rs=dao.executeQuery(ProductLibrary.class,sql,url);
        return rs;
    }

    public List<ProductLibrary> getUrlFromLibrary(String goodsName) {
        String selProductLibSql = "SELECT  page_url,brand  FROM product_library where AVAILABILITY = ? and IS_FRAME = ? and CATEGORY_NAME = ? ";
        return dao.queryList(ProductLibrary.class, selProductLibSql, IS_AVAILABILITY, IS_FRAME, goodsName);
    }
}
