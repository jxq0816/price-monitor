package com.inter3i.monitor.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inter3i.monitor.business.TaskManageService;
import com.inter3i.monitor.common.ApplicationSessionFactory;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.entity.TaskConfiguration;
import com.inter3i.monitor.entity.TaskConfigurationZb;
import com.inter3i.monitor.entity.account.Account;
import com.inter3i.monitor.entity.Category;
import com.inter3i.monitor.entity.CategoryAttribute;
import com.inter3i.monitor.entity.ProductLibrary;
import com.inter3i.monitor.entity.ProductLibraryCommodity;
import com.inter3i.monitor.entity.taskUpdate;
import com.inter3i.monitor.util.UUIDUtil;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/*
 * DESCRIPTION : 电商价格监测-任务配置Controller
 * USER : liuxiaolei
 * DATE : 2017/6/14 12:29
 */
@Controller
@RequestMapping("/monitors")
public class TaskManageController {
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String EMAIL = "email";
    private static final String TASK_NAME = "task_name";
    @Autowired
    TaskManageService taskManageService;

    /**
     * 新增任务
     *
     * @return categories 返回品类集合
     */
    @RequestMapping(value = "/addmonitor")
    public ModelAndView addTask() {
        ModelAndView mv = new ModelAndView("/monitorfile/taskConfig/addnew_monitor");
        List<Category> categories = taskManageService.getAllCategory();
        mv.addObject("categories", categories);
        return mv;
    }

    /**
     * 通过品类名称找到所有的属性
     *
     * @param categoryName 品类名称
     * @return CategoryAttributes 返回品类属性集合
     */
    @RequestMapping(value = "/getAttribute")
    public ModelAndView getAttribute(HttpServletRequest request,String categoryName) {
        ModelAndView mv = new ModelAndView("/monitorfile/taskConfig/task_condition");
        List<CategoryAttribute> categoryAttributes = taskManageService.getAttribute(categoryName);
        mv.addObject("categoryAttributes", categoryAttributes);
        mv.addObject("applicationAccount", ApplicationSessionFactory.getAccount(request));
        return mv;
    }

    /**
     * 通过过滤信息得到每一个条件的数据
     */
    @ResponseBody
    @RequestMapping(value = "/getAttribute/value", produces = "text/plain;charset=UTF-8")
    public String getAttributeValue(String condition) {
        StringBuilder result = new StringBuilder();
        Map<String, String> filterValueMap = new HashedMap(); //存储需要过滤的信息
        String distinctValue = "";  //存储需要去重的信息
        JSONObject jsonObject = JSON.parseObject(condition);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if ("distinctValue".equals(entry.getKey())) {
                distinctValue = entry.getValue().toString();
            } else {
                filterValueMap.put(entry.getKey(), entry.getValue().toString());
            }

        }
        List<ProductLibrary> productLibraries = taskManageService.getAttributeValue(filterValueMap, distinctValue);
        for (ProductLibrary productLibrary : productLibraries) {
            result.append(productLibrary.getDistinctValue() + ",");
        }

        return result.toString();
    }

    /**
     * 校验当前用户下是否存在相同的任务名称
     *
     * @param taskName 任务名称
     * @return true 存在 false不存在
     */
    @ResponseBody
    @RequestMapping(value = "/getAttribute/isTaskNameExists", produces = "text/plain;charset=UTF-8")
    public String isTaskNameExists(String taskName,String taskId, HttpServletRequest servletRequest) {
        Integer taskConfiguration = taskManageService.isTaskNameExists(ApplicationSessionFactory.getAccountId(servletRequest), taskName,taskId);
        if (taskConfiguration > 0) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * 校验该用户是否配置了相同的任务过滤条件，如果已经配置提醒用户不应该重复配置
     */
    @ResponseBody
    @RequestMapping(value = "/getAttribute/isTaskConditionExists", produces = "text/plain;charset=UTF-8")
    public String isTaskConditionExists(String condition, HttpServletRequest servletRequest) {
        Map<String, String> filterValueMap = new HashedMap(); //存储需要过滤的信息
        JSONObject jsonObject = JSON.parseObject(condition);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            filterValueMap.put(entry.getKey(), entry.getValue().toString());

        }
        //判断当前用户的过滤条件是否存在
        Integer taskConfiguration = taskManageService.isTaskConditionExists(filterValueMap, ApplicationSessionFactory.getAccountId(servletRequest));
        if (taskConfiguration > 0) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * 通过过滤条件，匹配出所有的信息，如产品详情、店铺名称、当前成交价、URL
     */
    @ResponseBody
    @RequestMapping(value = "/getAttribute/getStickPrices", produces = "text/plain;charset=UTF-8")
    public ModelAndView getStickPrices(String condition) {
        ModelAndView mv = new ModelAndView("/monitorfile/taskConfig/task_info");
        Map<String, String> filterValueMap = new HashedMap(); //存储需要过滤的信息
        JSONObject jsonObject = JSON.parseObject(condition);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            filterValueMap.put(entry.getKey(), entry.getValue().toString());

        }
        List<ProductLibraryCommodity> productLibraryCommodities = taskManageService.getStickPrices(filterValueMap);
        mv.addObject("productLibraryCommodities", productLibraryCommodities);
        return mv;
    }

    /**
     * 保存电商价格监测-任务配置信息，同时保存主任务与子任务
     *
     * @param condition 主任务信息
     * @param taskInfo  子任务信息
     */
    @ResponseBody
    @RequestMapping(value = "/getAttribute/saveTask", produces = "text/plain;charset=UTF-8")
    public String saveTask(String condition, String taskInfo, HttpServletRequest servletRequest) {
        Account account = ApplicationSessionFactory.getAccount(servletRequest);//得到用户信息
        Map<String, String> taskConfigurationMap = new HashedMap(); //存储需要过滤的信息
        JSONObject taskJsonObject = JSON.parseObject(condition);
        for (Map.Entry<String, Object> entry : taskJsonObject.entrySet()) {
            taskConfigurationMap.put(entry.getKey(), entry.getValue().toString());
        }
        //任务主表生成唯一ID
        String taskConfigurationId = UUIDUtil.getUUID();
        boolean taskResute = taskManageService.saveTaskConfiguration(taskConfigurationMap, account, taskConfigurationId);
        JSONArray taskZbJsonArray = JSON.parseArray(taskInfo);
        if (taskResute) {
            //当主表信息存储后需要对子表进行存储
            Iterator<Object> iterator = taskZbJsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject taskZbJsonObject = (JSONObject) iterator.next();
                taskManageService.saveTaskConfigurationZb(taskZbJsonObject, taskConfigurationId);
            }
        }
        return TRUE;
    }

    /**
     * 任务配置页面
     */
    @RequestMapping(value = "/taskConfiguration", produces = "text/plain;charset=UTF-8")
    public ModelAndView getTtask() {
        return new ModelAndView("/monitorfile/taskConfig/mys_monitor");
    }

    /**
     * 得到任务列表
     */
    @RequestMapping(value = "/taskConfiguration/taskTable", produces = "text/plain;charset=UTF-8")
    public ModelAndView taskTable(Integer pageNo, HttpServletRequest servletRequest) {
        ModelAndView mv = new ModelAndView("/monitorfile/taskConfig/my_monitorTable");
        PageBean pageBean = new PageBean();
        pageBean.setPageNo(pageNo);
        pageBean.setPageSize(10);
        Page page = taskManageService.findTaskListPage(ApplicationSessionFactory.getAccountId(servletRequest), pageBean);
        mv.addObject("page", page);
        return mv;
    }

    /**
     * 删除任务
     */
    @ResponseBody
    @RequestMapping(value = "/taskConfiguration/deleteTask", produces = "text/plain;charset=UTF-8")
    public String deleteTask(String id) {
        return taskManageService.deleteTask(id);
    }

    /**
     * 启动/停止任务
     */
    @ResponseBody
    @RequestMapping(value = "/taskConfiguration/updateTaskStatus", produces = "text/plain;charset=UTF-8")
    public String updateTaskStatus(String id) {
        List<TaskConfiguration> taskConfigurations = taskManageService.getTaskConfiguration(id);
        Boolean taskStatus = taskConfigurations.get(0).getTaskStatus();
        String status;
        if (taskStatus) {
            status = "0";
        } else {
            status = "1";
        }
        String result = taskManageService.updateTaskStatus(id, status);
        if (TRUE.equals(result)) {//执行成功后
            return status;
        } else {
            return "报错";
        }
    }

    /**
     * 用户任务数据校验，现每个用户只能添加十个任务
     */
    @ResponseBody
    @RequestMapping(value = "/taskConfiguration/userTaskNum", produces = "text/plain;charset=UTF-8")
    public String userTaskNum(HttpServletRequest servletRequest) {
        return taskManageService.userTaskNum(ApplicationSessionFactory.getAccountId(servletRequest));
    }

    /**
     * 通过任务id修改用户任务
     */
    @RequestMapping(value = "/taskConfiguration/updateTask/{id}", produces = "text/plain;charset=UTF-8")
    public ModelAndView updateTask(@PathVariable("id") String id) {
        ModelAndView mv = new ModelAndView("/monitorfile/taskConfig/revise_taskCondition");
        //得到品类名称
        List<TaskConfiguration> taskConfigurations = taskManageService.getTaskConfiguration(id);
        //通过品类名称得到所有属性值
        List<CategoryAttribute> categoryAttributes = taskManageService.getAttribute(taskConfigurations.get(0).getCategoryName());
        List<taskUpdate> taskUpdates = new ArrayList<taskUpdate>();
        taskUpdate taskName = new taskUpdate();//放任务名称
        taskName.setChineseName("任务名称");
        taskName.setEnglishName(TASK_NAME);
        String taskNameValue = taskManageService.dynamicValue(id, TASK_NAME);
        taskName.setValue(taskNameValue);
        taskUpdates.add(taskName);
        taskUpdate taskCategoryName = new taskUpdate();//放品类名称
        taskCategoryName.setChineseName("品类");
        taskCategoryName.setEnglishName("category_name");
        taskCategoryName.setValue(taskConfigurations.get(0).getCategoryName());
        taskUpdates.add(taskCategoryName);
        //动态通过品类属性表中得到的数据存储到taskUpdates中
        for (CategoryAttribute categoryAttribute : categoryAttributes) {
            taskUpdate taskUpdate = new taskUpdate(); //放动态生成的数据
            taskNameValue = taskManageService.dynamicValue(id, categoryAttribute.getEnglishField());
            taskUpdate.setChineseName(categoryAttribute.getChineseField());
            taskUpdate.setEnglishName(categoryAttribute.getEnglishField());
            taskUpdate.setValue(taskNameValue);
            taskUpdates.add(taskUpdate);
        }
        taskUpdate taskEmail = new taskUpdate();//放邮箱
        taskEmail.setChineseName("接收邮箱");
        taskEmail.setEnglishName(EMAIL);
        taskNameValue = taskManageService.dynamicValue(id, EMAIL);
        taskEmail.setValue(taskNameValue);
        taskUpdates.add(taskEmail);
        mv.addObject("taskUpdates", taskUpdates);
        mv.addObject("taskConfigurationId", id);
        return mv;
    }

    /**
     * 通过主任务id得到所有子任务的信息
     */
    @ResponseBody
    @RequestMapping(value = "/taskConfiguration/getTaskZb", produces = "text/plain;charset=UTF-8")
    public ModelAndView getTaskZb(String taskConfigurationId) {
        ModelAndView mv = new ModelAndView("/monitorfile/taskConfig/revise_taskInfo");
        List<TaskConfigurationZb> taskConfigurationZbs = taskManageService.getTaskZb(taskConfigurationId);
        mv.addObject("taskConfigurationZbs", taskConfigurationZbs);
        return mv;
    }


    /**
     * 修改电商价格监测主任务与子任务同时删除不用的子任务
     *
     * @param updateTaskConfiguration    修改主任务信息
     * @param updateTaskConfigurationZbs 修改子任务信息的集合
     * @param deleteTaskZbs              删除子表的id集合
     */
    @ResponseBody
    @RequestMapping(value = "/getAttribute/updateTask", produces = "text/plain;charset=UTF-8")
    public String updateTask(String updateTaskConfiguration, String updateTaskConfigurationZbs, String deleteTaskZbs) {
        String result = FALSE;
        JSONObject taskConfiguration = JSON.parseObject(updateTaskConfiguration); //主任务
        JSONArray taskConfigurationZb = JSON.parseArray(updateTaskConfigurationZbs); //子任务信息
        JSONArray deleteTaskZbJsonArray = JSON.parseArray(deleteTaskZbs); //需要删除的子任务信息
        Boolean taskConBoolean = taskManageService.updateTaskConfiguration(taskConfiguration.getString("id"), taskConfiguration.getString(TASK_NAME), taskConfiguration.getString(EMAIL));
        if (taskConBoolean) {
            for (Object obj : taskConfigurationZb) {
                JSONObject taskZbJsonObject = (JSONObject) obj;
                taskManageService.updateTaskConfigurationZb(taskZbJsonObject.getString("id"), taskZbJsonObject.getDouble("warning_price"));
            }
            for (int i = 0; i < deleteTaskZbJsonArray.size(); i++) {
                String id = deleteTaskZbJsonArray.getString(i);
                taskManageService.deleteTaskZb(id);
            }
            result = TRUE;
        }
        return result;
    }
}