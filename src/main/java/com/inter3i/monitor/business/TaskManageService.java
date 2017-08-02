package com.inter3i.monitor.business;

import com.alibaba.fastjson.JSONObject;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.entity.TaskConfiguration;
import com.inter3i.monitor.entity.TaskConfigurationZb;
import com.inter3i.monitor.entity.account.Account;
import com.inter3i.monitor.entity.Category;
import com.inter3i.monitor.entity.CategoryAttribute;
import com.inter3i.monitor.entity.ProductLibrary;
import com.inter3i.monitor.entity.ProductLibraryCommodity;

import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 电商价格监测-任务配置Service
 * USER : liuxiaolei
 * DATE : 2017/6/14 12:29
 */
public interface TaskManageService {

    /**
     * 得到品类信息表中所有品类数据
     *
     * @return Page       数据结果级
     */
    List<Category> getAllCategory();

    /**
     * 品类名称得到该品类下的所有属性值
     *
     * @param categoryName 品类名称
     * @return CategoryAttribute       数据结果级
     */
    List<CategoryAttribute> getAttribute(String categoryName);

    /**
     * 通过过滤值得到某一属性的所有值
     *
     * @param filterValueMap 过滤值
     * @param distinctValue  去重显示字段
     * @return CategoryAttribute       数据结果级
     */
    List<ProductLibrary> getAttributeValue(Map<String, String> filterValueMap, String distinctValue);

    /**
     * 校验当前用户下是否存在相同的任务名称
     *
     * @param accountId 用户id
     * @param taskName  任务名称
     * @param taskId    任务id
     * @return true 存在 false不存在
     */
    Integer isTaskNameExists(String accountId, String taskName,String taskId);

    /**
     * 校验该用户是否配置了相同的任务过滤条件，如果已经配置提醒用户不应该重复配置
     *
     * @param filterValueMap 过滤值
     * @param accountId      用户id
     */
    Integer isTaskConditionExists(Map<String, String> filterValueMap, String accountId);

    /**
     * 通过过滤条件，匹配出所有的信息，如产品详情、店铺名称、当前成交价、URL
     *
     * @param filterValueMap 过滤值
     * @return List<Commodity> 商品信息表列
     */
    List<ProductLibraryCommodity> getStickPrices(Map<String, String> filterValueMap);

    /**
     * 保存主任务信息
     *
     * @param taskConfigurationMap 主任务信息
     * @param account              用户信息
     */
    boolean saveTaskConfiguration(Map<String, String> taskConfigurationMap, Account account, String taskConfigurationId);

    /**
     * 保存子任务信息
     *
     * @param taskZbJsonObject    子任务信息
     * @param taskConfigurationId 主任务Id
     */
    boolean saveTaskConfigurationZb(JSONObject taskZbJsonObject, String taskConfigurationId);

    /**
     * 查询任务列表
     *
     * @param sysUserNameID 用户id
     * @param pageBean      分页信息
     */
    Page findTaskListPage(String sysUserNameID, PageBean pageBean);

    /**
     * 逻辑删除任务
     *
     * @param id 任务id
     */
    String deleteTask(String id);

    /**
     * 通过任务id得到任务信息
     *
     * @param id 任务id
     */
    List<TaskConfiguration> getTaskConfiguration(String id);

    /**
     * 启动/停止任务
     *
     * @param id     任务id
     * @param status 需要修改的状态
     */
    String updateTaskStatus(String id, String status);

    /**
     * 用户任务数据校验，现每个用户只能添加十个任务
     *
     * @param accountId 用户id
     * @return true, false true 大于等于十条,false小于十条
     */
    String userTaskNum(String accountId);

    /**
     * 通过任务id和字段英文名动态取出值。
     *
     * @param id          任务id
     * @param englishName 字段英文名
     * @return true, false true 大于等于十条,false小于十条
     */
    String dynamicValue(String id, String englishName);

    /**
     * 通过任务id得到所有任务子表信息。
     *
     * @param taskConfigurationId 任务id
     * @return List<TaskConfigurationZb> 任务子表列表
     */
    List<TaskConfigurationZb> getTaskZb(String taskConfigurationId);

    /**
     * 修改主表信息。
     *
     * @param id       任务id
     * @param taskName 任务名称
     * @param email    邮箱地址
     */
    Boolean updateTaskConfiguration(String id, String taskName, String email);

    /**
     * 修改子任务表信息。
     *
     * @param id           任务id
     * @param warningPrice 预警价格
     */
    Boolean updateTaskConfigurationZb(String id, Double warningPrice);

    /**
     * 删除子任务信息。
     *
     * @param id 任务id
     */
    Boolean deleteTaskZb(String id);


}
