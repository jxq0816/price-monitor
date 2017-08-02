package com.inter3i.monitor.dao;

import com.inter3i.monitor.Application;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/4/26 15:00
 */
@Repository("jdbcCommonDAO")
public class JDBCBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Resource(name = "dataSource")
    private DataSource dataSource;

    /**
     * 传递多个参数进行查询，sql语句中采用?指定参数 select a,b,c from table1 where a=? and b=?
     */
    public <T> List<T> queryList(Class<T> classes, String sql, final Object... params) {
        List<T> list = new ArrayList<T>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            setStatementParames(pstm, sql, params);
            rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, Method> methodmaps = new HashMap<String, Method>();
            initMethodMap(classes, "set", methodmaps);
            while (rs.next()) {
                T o = classes.newInstance();
                setResultToObj(rs, rsmd, methodmaps, o);
                list.add(o);
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm, rs);
        }
        return list;
    }

    /**
     * 传递多个参数进行查询，sql语句中采用?指定参数 select a,b,c from table1 where a=? and b=?
     *
     */
    public <T> T executeQuery(Class<T> classes, String sql, final Object... params) {
        T result = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            setStatementParames(pstm, sql, params);
            rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String, Method> methodmaps = new HashMap<String, Method>();
            initMethodMap(classes, "set", methodmaps);
            if (rs.next()){
                result = classes.newInstance();
                setResultToObj(rs, rsmd, methodmaps, result);
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm, rs);
        }
        return result;
    }

    /**
     * 获取数据条数 select count(id) from table where id=? 或者 select a from table where id=?
     *
     */
    public Object getColumn(String sql, final Object... params) {
        Object result = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null)
                    params[i] = "";
                pstm.setObject(i + 1, params[i]);
            }
            rs = pstm.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm, rs);
        }
        return result;
    }

    public Integer getCount(String sql, final Object... params) {
        Object countObj = getColumn(sql,params);
        if(countObj != null) {
            return Integer.valueOf(countObj.toString());
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getColumns(Class<T> classes, String sql, final Object... params) {
        List<T> results = new ArrayList<T>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param == null){
                    param = "";
                }
                pstm.setObject(i + 1, param);
            }
            rs = pstm.executeQuery();
            while (rs.next()) {
                results.add((T)rs.getObject(1));
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm, rs);
        }
        return results;
    }

    /**
     * 返回List<Map>对象
     * @param sql sql语句中采用?指定参数 select a,b,c from table1 where a=? and b=?
     */
    public List<Map<String,Object>> queryMapList(String sql, final Object... params) {
        List list = new ArrayList();
        Map<String,Object> map;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            setStatementParames(pstm, sql, params);
            rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int len=rsmd.getColumnCount();
            while (rs.next()) {
                map = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= len; i++) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm, rs);
        }
        return list;
    }

    /**
     * 获取map类型的结果
     * @param sql
     * @param params
     * @return
     */
    public Map<String, Object> getMapResult(String sql, final Object... params) {
        Map<String, Object> map = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            setStatementParames(pstm, sql, params);
            rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int len=rsmd.getColumnCount();
            if (rs.next()) {
                map = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= len; i++) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm, rs);
        }
        return map;
    }

    /**
     * 执行更新/插入操作，传递多个参数 update table set col=? where id=?
     *  insert into diary(userid,username,title,content)values(?,?,?,?)
     *
     * @param sql
     * @throws SQLException
     */
    public int executeUpdate(String sql, final Object... params) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = getConnection();
            pstm = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstm.setObject(i + 1, params[i]);
            }
            result = pstm.executeUpdate();
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm);
        }
        return result;
    }

    /**
     * 批量更新/插入对象列表
     *
     * @param sql
     * @param objects
     */
    public void executeBach(String sql, List<?> objects) {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            if (objects != null && objects.size() > 0) {
                conn = getConnection();
                String sql1 = parsePlaceholder(sql); // 转换为带?号的sql
                pstm = conn.prepareStatement(sql1);
                List<Object> parames = new ArrayList<Object>();
                for (Object object : objects) {
                    parames.clear();
                    initParames(sql, object, parames);
                    int i = 1;
                    for (Object obj : parames) {
                        pstm.setObject(i, obj);
                        i++;
                    }
                    pstm.addBatch();
                }
                pstm.executeBatch();
            } else {
                throw new Exception("更新列表未定义或为空！");
            }
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm);
        }
    }


    /**
     * @描述: 根据对象插入数据
     * @param o 所要新增的对象
     * @param tablename 新增对象对应的表名
     */
    public int insert(Object o, String tablename){
        Connection conn = null;
        PreparedStatement pstm = null;
        int result = 0;
        try {
            conn = getConnection();
            StringBuffer sqlbf = new StringBuffer("insert into ");
            sqlbf.append(tablename);
            StringBuffer filedbf = new StringBuffer(" (");
            StringBuffer valuebf = new StringBuffer(" values (");
            List<Object> values = new ArrayList<Object>();
            Method[] allmethod = o.getClass().getMethods();
            List<String> dbFiledList = getFieldsByTableName(tablename);
            for (Method method : allmethod) {
                String methodname = method.getName();
                if(methodname.startsWith("get") && !methodname.endsWith("Class")){
                    Object value = method.invoke(o);
                    if(value!=null){
                        String filed = methodname.substring(3).toLowerCase();
                        if(dbFiledList.contains(filed)){
                            filedbf.append(filed).append(",");
                            valuebf.append("?").append(",");
                            values.add(value);
                        }
                    }
                }
            }
            filedbf.delete(filedbf.length()-1, filedbf.length()).append(")");
            valuebf.delete(valuebf.length()-1, valuebf.length()).append(")");
            sqlbf.append(filedbf).append(valuebf);
            pstm = conn.prepareStatement(sqlbf.toString());
            for (int i = 0; i < values.size(); i++) {
                pstm.setObject(i + 1, values.get(i));
            }
            result = pstm.executeUpdate();
        } catch (IllegalArgumentException e) {
            logger.error("非法参数错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("非法权限错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm);
        }
        return result;
    }

    /**
     * 根据数据库表名称获取该表的所有字段名
     * @param tablename 数据库表名称
     */
    private List<String> getFieldsByTableName(String tablename){
        String sql ="select COLUMN_NAME from information_schema.COLUMNS where table_name = ?";
        return this.getColumns(String.class, sql, tablename);
    }

    /**
     * 根据对象修改数据
     *
     * @param o 需要修改的对象
     * @param tablename 修改对象的表名
     * @param primarykey 修改对象表的主键
     * @param updatenull 是否更新Null值
     */
    public int update(Object o, String tablename, String primarykey, boolean updatenull){
        Connection conn = null;
        PreparedStatement pstm = null;
        int result = 0;
        try {
            conn = getConnection();
            StringBuffer sqlbf = new StringBuffer("update ");
            sqlbf.append(tablename).append(" ");
            StringBuffer filedbf = new StringBuffer("set ");
            List<Object> values = new ArrayList<Object>();
            Object primarykeyvalue = new Object();
            Method[] allmethod = o.getClass().getMethods();
            for (Method method : allmethod) {
                String methodname = method.getName();
                if(methodname.startsWith("get") && !methodname.endsWith("Class")){
                    Object value = method.invoke(o);
                    String filed = methodname.substring(3).toLowerCase();
                    if(value!=null){
                        if(filed.equals(primarykey)){
                            primarykeyvalue = value;
                        }else{
                            filedbf.append(filed).append("=?,");
                            values.add(value);
                        }
                    }
                    if(value==null && updatenull){
                        filedbf.append(filed).append("=null,");
                    }
                }
            }
            filedbf.delete(filedbf.length()-1, filedbf.length());
            sqlbf.append(filedbf);
            if(StringUtils.isNotEmpty(primarykey)){
                sqlbf.append(" where ").append(primarykey).append("=?");
                values.add(primarykeyvalue);
            }
            pstm = conn.prepareStatement(sqlbf.toString());
            for (int i = 0; i < values.size(); i++) {
                pstm.setObject(i + 1, values.get(i));
            }
            result = pstm.executeUpdate();

        } catch (IllegalArgumentException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } finally {
            this.destoryResource(conn, pstm);
        }
        return result;
    }

    /**
     * 删除查询条件中的order by子句
     *
     * @param queryString
     *            查询SQL语句
     * @return 删除查询语句中的order by子句后的查询语句
     */
    private String delOrderbySQL(String queryString) {

        String result = queryString;

        int idx = queryString.indexOf("order by");

        if (idx > 0) {
            result = queryString.substring(0, idx);
        }
        return result;
    }
    /**
     * 关闭结果集、预处理和数据库连接 destoryResource
     *
     */
    private void destoryResource(Connection conn, PreparedStatement pstm, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (pstm != null)
                pstm.close();
            if (conn != null)
                DataSourceUtils.releaseConnection(conn, dataSource);
        } catch (SQLException e) {
        }
    }

    /**
     * 关闭预处理和数据库连接
     *
     */
    private void destoryResource(Connection conn, PreparedStatement pstm) {
        try {
            if (pstm != null){
                pstm.close();
            }
            if (conn != null){
                DataSourceUtils.releaseConnection(conn, dataSource);
            }
        } catch (SQLException e) {
        }
    }

    /**
     * 跟据sql语句(:name),解析出参数列表
     *
     * @param sql： sql语句
     * @param parames 解析好的参数列表
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws Exception
     */
    private void initParames(String sql, Object obj, List<Object> parames)
            throws Exception {

        if (obj == null)
            return;

        Method[] methods = obj.getClass().getMethods();
        Map<String, Method> methodmaps = new HashMap<String, Method>();
        List<String> methodnames = new ArrayList<String>();
        for (int i = 0; i < methods.length; i++) {
            String methodname = methods[i].getName();
            if (methodname.startsWith("get")) {
                String prop = methodname.substring(3).toLowerCase();
                methodmaps.put(prop, methods[i]);
                methodnames.add(prop);
            }
        }

        Pattern p = Pattern.compile(":\\w+");
        Matcher m = p.matcher(sql);
        while (m.find()) {
            String g = m.group().toLowerCase();
            g = g.substring(1);
            if(methodmaps.get(g) == null){
                g = getEntityPropertyName(g).toLowerCase();
            }
            if (methodnames.contains(g)) {
                Object value = methodmaps.get(g).invoke(obj);
                parames.add(value);
            } else {
                throw new Exception("传递的参数格式错误，其名称必须与字段名称相同！");
            }
        }
    }

    /**
     * 设置参数
     *
     * @param sql
     * @param pstm
     * @param params
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws Exception
     */
    private void setStatementParames(PreparedStatement pstm, String sql, final Object... params)
            throws Exception {
        if (params == null)
            return;

        int count = occursCount(sql, "\\?");
        if (count == params.length) {
            int i = 1;
            for (Object object : params) {
                pstm.setObject(i, object);
                i++;
            }
        } else {
            throw new Exception("传递的参数个数与sql中的接收参数个数不一致！");
        }
    }

    /**
     * 填充对象方法map
     * @param methodmaps
     */
    private void initMethodMap(Class<?> classes, String prefix, Map<String, Method> methodmaps){
        methodmaps.clear();
        Method[] methods = classes.getMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodname = methods[i].getName();
            if (methodname.startsWith(prefix)) {
                String propname = methodname.substring(prefix.length());
                propname = propname.substring(0, 1).toLowerCase() + propname.substring(1);
                methodmaps.put(propname, methods[i]);
            }
        };
    }

    /**
     * 根据sql获取结果集映射到对象
     *
     * @param rs
     */
    private void setResultToObj(ResultSet rs, ResultSetMetaData rsmd, Map<String, Method> methodmaps, Object obj) {

        try	{
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnLabel(i);
                Method m = methodmaps.get(columnName);
                if(m == null){
                    m = methodmaps.get(getEntityPropertyName(columnName));
                }
                if (m != null){
                    m.invoke(obj, rs.getObject(i));
                }
            }
        } catch (SecurityException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error("错误",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将:param类型参数解析为?
     *
     * @param sql
     * @return
     */
    private String parsePlaceholder(String sql) {
        Pattern p = Pattern.compile(":\\w+");
        Matcher m = p.matcher(sql);
        sql = m.replaceAll("?");
        return sql;
    }

    private int occursCount(String sql, String str) {
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(sql);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }


    public <T> Page queryPage(Class<T> classes, PageBean pageBean, String sql, final Object... params){
        Integer page = pageBean.getPageNo();
        Integer pageSize = pageBean.getPageSize();
        String newSql = "select ifnull(count(1),0) from (" + sql + ") as t";
        Object countObj = getColumn(newSql, params);
        Long totalNum = 0L;
        if(countObj != null && StringUtils.isNotEmpty(String.valueOf(countObj))){
            totalNum = Long.valueOf(String.valueOf(countObj));
        }
        pageBean = new PageBean(page,pageSize,totalNum);
        if (pageBean.getTotalNum() == 0) {
            return new Page(new ArrayList(), pageBean);
        }
        sql += " limit " + pageBean.getPageBeginNum() + ", " + pageBean.getPageSize();
        return new Page(queryList(classes, sql, params), pageBean);

    }


    private Connection getConnection(){
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if(connection == null){
            connection = DataSourceUtils.getConnection(dataSource);
        }
        return connection;
    }


    public static String getEntityPropertyName(String propertyName){
        propertyName = propertyName.toLowerCase();
        char[] chars = propertyName.toCharArray();
        int i = 1;
        StringBuffer sb = new StringBuffer();
        for(char c:chars){
            String temp = String.valueOf(c);
            if(temp.equals("_")){
                i = 0;
                continue;
            }
            if(i == 0){
                sb.append(temp.toUpperCase());
            }else{
                sb.append(temp);
            }
            i++;
        }
        return sb.toString();
    }



}
