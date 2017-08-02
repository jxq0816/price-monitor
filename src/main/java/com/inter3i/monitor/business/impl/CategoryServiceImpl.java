package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.CategoryService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by boxiaotong on 2017/6/13
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<Category> findAllList() {
        String sql="select ID as id,CATEGORY_NAME as categoryName,CATEGORY_NAME_EXPLAIN as categoryNameExplain,CREATED_ON as createOn from category ORDER BY level ASC";
        List<Category> list=jdbcBaseDao.queryList(Category.class,sql);
        return list;
    }

    @Override
    public List<Category> getCategroyName(){
        String selCategoryNameSql = "SELECT  category_name_explain as categoryName  FROM category";
        return jdbcBaseDao.queryList(Category.class, selCategoryNameSql);
    }
}
