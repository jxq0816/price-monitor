package com.inter3i.monitor.business;

import com.inter3i.monitor.entity.Category;

import java.util.List;

/**
 * Created by boxiaotong on 2017/6/13.
 */
public interface CategoryService {
    /**
     * 查询列表
     *
     * @return
     */
    List<Category> findAllList();

    /**
     * 得到所有的商品名
     *
     * @return
     */
    List<Category> getCategroyName();
}
