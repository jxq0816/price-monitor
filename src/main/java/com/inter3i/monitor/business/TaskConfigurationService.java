package com.inter3i.monitor.business;

import java.util.List;
import java.util.Map;

/**
 * Created by boxiaotong on 2017/6/14.
 */
public interface TaskConfigurationService {

    List<Map<String,Object>> findList(String userId,String categoryName);

    Map<String,Object> findLastOne(String userId);

    Map<String,Object> findOne(String taskId,String categoryName);
}
