package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.CategoryAttributeService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangxingqi on 2017/6/26.
 */
@Service
public class CategoryAttributeServiceImpl implements CategoryAttributeService{
    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    /**
     * 品类名称得到该品类下的所有属性值
     *
     * @param categoryName 品类名称
     * @return CategoryAttribute       数据结果级
     */
    @Override
    public List<Map<String,Object>> getAttribute(String categoryName) {
        String sql = "select t.chinese_field AS chineseField,t.english_field AS englishField,t.level AS level " +
                "from category c,category_attribute t " +
                "where c.category_name_explain=? and c.id = t.category_id " +
                "order by t.LEVEL asc";
        List<Map<String,Object>> list=jdbcBaseDao.queryMapList(sql,categoryName);
        return list;
    }
}
