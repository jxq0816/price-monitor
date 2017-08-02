package com.inter3i.monitor.business;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangxingqi on 2017/6/26.
 */
public interface CategoryAttributeService {

    /**
     * 品类名称得到该品类下的所有属性值
     *
     * @param categoryName 品类名称
     * @return CategoryAttribute       数据结果级
     */
    List<Map<String,Object>> getAttribute(String categoryName);
}
