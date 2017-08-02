package com.inter3i.monitor.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.inter3i.monitor.business.TaskManageService;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.TaskConfiguration;
import com.inter3i.monitor.entity.TaskConfigurationZb;
import com.inter3i.monitor.entity.account.Account;
import com.inter3i.monitor.entity.Category;
import com.inter3i.monitor.entity.CategoryAttribute;
import com.inter3i.monitor.entity.ProductLibrary;
import com.inter3i.monitor.entity.ProductLibraryCommodity;
import com.inter3i.monitor.util.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 电商价格监测-任务配置ServiceImpl
 * USER : liuxiaolei
 * DATE : 2017/6/14 12:29
 */
@Service
public class TaskManageServiceImpl implements TaskManageService {
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String AND = " and ";
    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    /**
     * 得到品类信息表中所有品类数据
     *
     * @return List<Category>       数据结果级
     */
    @Override
    public List<Category> getAllCategory() {
        String sql = "SELECT category_name_explain AS categoryNameExplain FROM category order by level";
        return jdbcBaseDao.queryList(Category.class, sql);
    }

    /**
     * 品类名称得到该品类下的所有属性值
     *
     * @param categoryName 品类名称
     * @return CategoryAttribute       数据结果级
     */
    @Override
    public List<CategoryAttribute> getAttribute(String categoryName) {
        String sql = "select t.chinese_field ,t.english_field ,t.level from category c,category_attribute t where c.category_name_explain=? and c.id = t.category_id order by t.level";
        return jdbcBaseDao.queryList(CategoryAttribute.class, sql, categoryName);
    }

    /**
     * 通过过滤值得到某一属性的所有值
     *
     * @param filterValueMap 过滤值
     * @param distinctValue  去重显示字段
     * @return CategoryAttribute       数据结果级
     */
    @Override
    public List<ProductLibrary> getAttributeValue(Map<String, String> filterValueMap, String distinctValue) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append(distinctValue);
        sql.append(" AS distinctValue from product_library where ");
        for (Map.Entry<String, String> entry : filterValueMap.entrySet()) {
            sql.append(entry.getKey());
            sql.append("=");
            sql.append("'" + entry.getValue() + "'");
            sql.append(AND);
        }
        sql.append("is_frame = 1 and availability = 0");
        sql.append(" order by "+distinctValue);
        return jdbcBaseDao.queryList(ProductLibrary.class, sql.toString());
    }

    /**
     * 校验当前用户下是否存在相同的任务名称
     *
     * @param accountId 用户id
     * @param taskName  任务名称
     * @param taskId    任务id
     * @return true 存在 false不存在
     */
    @Override
    public Integer isTaskNameExists(String accountId, String taskName,String taskId) {
        if ("".equals(taskId) || taskId == null){
            String sql = "select count(1) from task_configuration where task_name=? and sys_user_id=? and delete_mark = 1";
            return jdbcBaseDao.getCount(sql, taskName, accountId);
        }else{
            String sql = "select count(1) from task_configuration where task_name=? and sys_user_id=? and id != ? and delete_mark = 1";
            return jdbcBaseDao.getCount(sql, taskName, accountId,taskId);
        }
    }

    /**
     * 校验该用户是否配置了相同的任务过滤条件，如果已经配置提醒用户不应该重复配置
     *
     * @param filterValueMap 过滤值
     * @param accountId      用户id
     */
    @Override
    public Integer isTaskConditionExists(Map<String, String> filterValueMap, String accountId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append(" count(1) from task_configuration where ");
        for (Map.Entry<String, String> entry : filterValueMap.entrySet()) {
            sql.append(entry.getKey());
            sql.append("=");
            sql.append("'" + entry.getValue() + "'");
            sql.append(AND);
        }
        sql.append("sys_user_id = ? and delete_mark = 1");
        return jdbcBaseDao.getCount(sql.toString(), accountId);
    }

    /**
     * 通过过滤条件，匹配出所有的信息，如产品详情、店铺名称、当前成交价、URL
     *
     * @param filterValueMap 过滤值
     * @return List<Commodity> 商品信息表列
     */
    @Override
    public List<ProductLibraryCommodity> getStickPrices(Map<String, String> filterValueMap) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.page_url,c.stick_price,p.seller_name,p.product_description from " +
                "product_library p,(select c.page_url,c.stick_price from commodity c," +
                "(select page_url,max(storage_time) AS storage_time from commodity group by page_url) " +
                "t where c.page_url =t.page_url and c.storage_time = t.storage_time and c.stick_price is not null and c.stick_price > 0)" +
                " c where p.page_url = c.page_url");
        for (Map.Entry<String, String> entry : filterValueMap.entrySet()) {
            sql.append(AND);
            sql.append(mosaicSql(entry));
        }
        sql.append(" and is_frame = 1 and availability = 0");

        return jdbcBaseDao.queryList(ProductLibraryCommodity.class, sql.toString());
    }

    public String mosaicSql(Map.Entry<String, String> entry){
        String[] platforms; //多个台平用逗号分隔
        StringBuilder sql = new StringBuilder();
        if ("platform".equals(entry.getKey())) { //只有是平台的时候用in
            platforms = entry.getValue().split(",");
            sql.append(entry.getKey());
            for (int i = 0; i < platforms.length; i++) {
                sql.append(" in (");
                sql.append("'" + platforms[i] + "'");
                if (i+1 < platforms.length) {
                    sql.append(",");
                } else {
                    sql.append(")");
                }
            }
        } else {
            sql.append(entry.getKey());
            sql.append("=");
            sql.append("'" + entry.getValue() + "'");
        }
        return sql.toString();
    }

    /**
     * 保存主任务信息
     *
     * @param taskConfigurationMap 主任务信息
     * @param account              用户信息
     */
    @Override
    public boolean saveTaskConfiguration(Map<String, String> taskConfigurationMap, Account account, String taskConfigurationId) {
        StringBuilder sql = new StringBuilder();
        String taskInfos = null;//任务信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        sql.append("INSERT INTO `task_configuration` (`id`, `sys_user_id`,`update_frequency`,`monitor_type`,`warning_status`,`task_status`,`user_name`,`delete_mark`, `update_time`,");
        for (Map.Entry<String, String> entry : taskConfigurationMap.entrySet()) {//得到所有需要存储的key
            sql.append("`" + entry.getKey() + "`,");
        }
        sql.append("`created_on`,`task_info`)");
        sql.append(" VALUES ('" + taskConfigurationId + "','" + account.getAccountid() + "','24小时','低价预警',b'0',b'0','" + account.getNickname() + "',b'1','" + df.format(new Date()) + "',");
        for (Map.Entry<String, String> entry : taskConfigurationMap.entrySet()) {
            sql.append("'" + entry.getValue() + "',");
        }
        //sunjianhui   20170622  得到任务信息
        taskInfos = taskConfigurationMap.get("brand") + taskConfigurationMap.get("models")+ taskConfigurationMap.get("year")+ taskConfigurationMap.get("season")+ taskConfigurationMap.get("style")+ taskConfigurationMap.get("features")+ taskConfigurationMap.get("material")+ taskConfigurationMap.get("platform");
        sql.append("'" + df.format(new Date()) + "','" + taskInfos + "')");
        int result = jdbcBaseDao.executeUpdate(sql.toString());
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存子任务信息
     *
     * @param taskZbJsonObject    子任务信息
     * @param taskConfigurationId 主任务Id
     */
    @Override
    public boolean saveTaskConfigurationZb(JSONObject taskZbJsonObject, String taskConfigurationId) {
        String taskZbId = UUIDUtil.getUUID();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO `task_configuration_zb` (`id`, `task_configuration_id`, `product_description`, `seller_name`, `warning_price`, `" +
                "warning_number`, `page_url`, `task_status`, `delete_mark`, `update_time`, `created_on`) VALUES (");
        sql.append("'" + taskZbId + "','" + taskConfigurationId + "','" + taskZbJsonObject.getString("product_description") + "','" + taskZbJsonObject.getString("seller_name") + "',");
        sql.append("'" + taskZbJsonObject.getString("warning_price") + "',0,'" + taskZbJsonObject.getString("page_url") + "',b'0',b'1','" + df.format(new Date()) + "',");
        sql.append("'" + df.format(new Date()) + "')");
        int result = jdbcBaseDao.executeUpdate(sql.toString());
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询任务列表
     *
     * @param sysUserNameID 用户id
     * @param pageBean      分页信息
     */
    @Override
    public Page findTaskListPage(String sysUserNameID, PageBean pageBean) {
        String sql = "select id,task_name,task_info,email,update_frequency,warning_status,task_status from task_configuration where sys_user_id=? and delete_mark=1";
        return jdbcBaseDao.queryPage(TaskConfiguration.class, pageBean, sql, sysUserNameID);
    }

    /**
     * 逻辑删除任务
     *
     * @param id 任务id
     */
    @Override
    public String deleteTask(String id) {
        String taskSql = "update task_configuration set delete_mark = b'0' where id = ?";
        String taskZbSql = "update task_configuration_zb set delete_mark = b'0' where task_configuration_id =?";
        int taskResult = jdbcBaseDao.executeUpdate(taskSql, id);
        int taskZbResult = 0;
        if (taskResult > 0) {
            taskZbResult = jdbcBaseDao.executeUpdate(taskZbSql, id);
        }
        if (taskZbResult > 0) { //1为删除
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * 启动/停止任务
     * @param id  任务id
     * @param status 需要修改的状态
     */
    @Override
    public String updateTaskStatus(String id,String status) {
        String taskSql = "update task_configuration set warning_status = b'"+status+"',task_status= b'"+status+"' where id = ?";
        String taskZbSql = "update task_configuration_zb  set task_status = b'"+status+"' where task_configuration_id = ?";
        int taskResult = jdbcBaseDao.executeUpdate(taskSql, id);
        int taskZbResult = 0;
        if (taskResult > 0) {
            taskZbResult = jdbcBaseDao.executeUpdate(taskZbSql, id);
        }
        if (taskZbResult > 0) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * 用户任务数据校验，现每个用户只能添加十个任务
     * @param accountId  用户id
     * @return true,false true 大于等于十条,false小于十条
     */
    @Override
    public String userTaskNum(String accountId) {
        String sql = "select count(1) AS count from task_configuration where sys_user_id = ? and delete_mark=1";
        Integer count = jdbcBaseDao.getCount(sql,accountId);
        if (count < 10){
            return FALSE;
        }else {
            return TRUE;
        }
    }

    /**
     * 通过任务id和字段英文名动态取出值。
     * @param id  任务id
     * @param englishName 字段英文名
     * @return true,false true 大于等于十条,false小于十条
     */
    @Override
    public String dynamicValue(String id, String englishName) {
        String sql = "select "+englishName+" from task_configuration where id = ?";
        return (String)jdbcBaseDao.getColumn(sql,id);
    }

    /**
     * 通过任务id得到所有任务子表信息。
     * @param taskConfigurationId  任务id
     * @return List<TaskConfigurationZb> 任务子表列表
     */
    @Override
    public List<TaskConfigurationZb> getTaskZb(String taskConfigurationId) {
        String sql = "select t.id,t.product_description,t.seller_name,l.stick_price,t.warning_price from task_configuration_zb t,product_library p," +
                "(select c.page_url,c.stick_price from commodity c," +
                "(select page_url,max(storage_time) AS storage_time from commodity group by page_url) " +
                "t where c.page_url =t.page_url and c.storage_time = t.storage_time and c.stick_price is not null) l" +
                " where t.page_url = p.page_url and l.page_url = t.page_url and " +
                "p.is_frame = 1 and p.availability = 0 and t.delete_mark = 1 and t.task_configuration_id = ?";
        return jdbcBaseDao.queryList(TaskConfigurationZb.class,sql,taskConfigurationId);
    }

    /**
     * 修改主表信息。
     * @param id  任务id
     * @param taskName 任务名称
     * @param email     邮箱地址
     * @return List<TaskConfigurationZb> 任务子表列表
     */
    @Override
    public Boolean updateTaskConfiguration(String id, String taskName, String email) {
        String sql = "update task_configuration set task_name =?,email =? where id =?";
        int taskResult = jdbcBaseDao.executeUpdate(sql,taskName,email,id);
        if (taskResult > 0){
            return true;
        }
        return false;
    }

    /**
     * 修改子任务表信息。
     * @param id  任务id
     * @param warningPrice 预警价格
     * @return List<TaskConfigurationZb> 任务子表列表
     */
    @Override
    public Boolean updateTaskConfigurationZb(String id, Double warningPrice) {
        String sql = "update task_configuration_zb set warning_price = ? where id = ?";
        int taskResult = jdbcBaseDao.executeUpdate(sql,warningPrice,id);
        if (taskResult > 0){
            return true;
        }
        return false;
    }

    /**
     * 删除子任务信息。
     * @param id  任务id
     * @return
     */
    @Override
    public Boolean deleteTaskZb(String id) {
        String sql = "update task_configuration_zb set delete_mark =b'0' where id =?";
        int taskResult = jdbcBaseDao.executeUpdate(sql, id);
        if (taskResult > 0){
            return true;
        }
        return false;
    }

    /**
     * 通过任务id得到任务信息
     * @param id  任务id
     * @return
     */
    @Override
    public List<TaskConfiguration> getTaskConfiguration(String id) {
        String sql = "select task_status,category_name from task_configuration where id = ?";
        return jdbcBaseDao.queryList(TaskConfiguration.class,sql,id);
    }







}
