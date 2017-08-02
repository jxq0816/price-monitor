package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.ProductLibrary;

import java.util.List;

/**
 * Created by boxiaotong on 2017/6/16.
 */
public interface ProductLibraryService {
    ProductLibrary findProduct(String url);

    /**
     * 得到产品库中的商品所有可用的url
     *
     * @param goodsName   商品名
     * @return
     */
    List<ProductLibrary> getUrlFromLibrary(String goodsName);
}
