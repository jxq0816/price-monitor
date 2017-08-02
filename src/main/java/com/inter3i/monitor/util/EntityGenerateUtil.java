package com.inter3i.monitor.util;

import com.inter3i.monitor.dao.JDBCBaseDao;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/14 20:34
 */
public class EntityGenerateUtil {

    private static final String MYSQL_DATA_SOURCE_BEAN = "mysqlServer";
    private static final String APPLICATION_PROJECT_NAME = "monitor";
    private static final String COMPANY_WEBSITE = "com.inter3i";
    private static final String APPLICATION_ENTITY_PACKAGE = "entity.temp";
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String APPLICATION_ENTITY_TEMPLATE_NAME = "Entity.template";
    private static final String APPLICATION_JAVA_PACKAGE = "/src/main/java";


    public static void main(String[] args) {
        Connection conn = null;
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource(MYSQL_DATA_SOURCE_BEAN);
            conn = ds.getConnection();
            List<String> tableList = getDBTableList(conn);
            generateAllEntity(tableList,conn);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            jdbcClose(conn);
        }
    }

    /**
     * 获取所有的表名
     * @param conn
     * @return
     * @throws SQLException
     */
    public static List<String> getDBTableList(java.sql.Connection conn) throws SQLException{
        List<String> tableList=new ArrayList<String>();
        String sql = "show tables";
        PreparedStatement stmt=conn.prepareStatement(sql);
        ResultSet rs=stmt.executeQuery();
        try {
            while(rs.next()) {
                String tableName = rs.getString(1).trim();
                tableList.add(tableName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jdbcClose(stmt,rs);
        }
        return tableList;
    }


    /**
     * 根据实体模板生成实体类
     * @param tableList
     * @param conn
     * @throws Exception
     */
    public static void generateAllEntity(List<String> tableList,java.sql.Connection conn) throws Exception{
        //实体类生成路径
        String generateRootPath = USER_DIR.substring(0,USER_DIR.lastIndexOf("\\"))+"/"+APPLICATION_PROJECT_NAME+APPLICATION_JAVA_PACKAGE +"/"+COMPANY_WEBSITE.replace(".", "/")+"/"+APPLICATION_PROJECT_NAME + "/"+APPLICATION_ENTITY_PACKAGE.replace(".","/");
        File outputDir = new File(generateRootPath);
        if (!outputDir.isDirectory()){
            if (!outputDir.mkdirs()){
                throw new Exception("Cannot create directory! Directory=" + generateRootPath);
            }
        }
        String templatePath = EntityGenerateUtil.class.getClassLoader().getResource(APPLICATION_ENTITY_TEMPLATE_NAME).getPath();
        File templateFile = new File(templatePath.substring(1));
        if (templateFile == null || !templateFile.exists()){
            throw new RuntimeException("模板文件不存在");
        }
        for(String tableName:tableList){
            try {
                generateEntity(tableName,generateRootPath,templateFile,conn);
            } catch (Exception e) {
                throw new RuntimeException("生成实体" + tableName + "失败了！",e);
            }
        }
    }


    public static void generateEntity(String tableName,String generateRootPath,File templateFile,java.sql.Connection conn) throws Exception{
        String fileName = getEntityName(tableName)+".java";
        File outputFile = new File(generateRootPath + "/"+ fileName);
        FileUtil.deleteFile(outputFile.getPath());
        String fileContent = generateSuperEntityClass(tableName,templateFile,conn);
        FileOutputStream os = new FileOutputStream(outputFile);
        os.write(fileContent.getBytes());
        os.write("\n".getBytes());
        os.close();
    }


    public static String generateSuperEntityClass(String tableName,File templateFile,java.sql.Connection conn) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(templateFile));
        StringBuffer strBuf = new StringBuffer();
        String line;
        String[] strs = getEntityFields(tableName,conn);
        while ((line = br.readLine()) != null)
        {
            line = line.replaceAll("\\$PACKAGE_ENTITY\\$", COMPANY_WEBSITE +"."+APPLICATION_PROJECT_NAME+"."+APPLICATION_ENTITY_PACKAGE);
            line = line.replaceAll("\\$ENTITY\\$", getEntityName(tableName));
            line = line.replaceAll("\\$ENTITYFIELDS\\$", strs[0]);
            line = line.replaceAll("\\$ENTITYFIELDSCONS\\$", strs[1]);
            line = line.replaceAll("\\$ENTITYFIELDSSETVALUES\\$", strs[2]);
            line = line.replaceAll("\\$ENTITYFIELDSGETSETMETHOD\\$", strs[3]);
            line = line.replaceAll("\\$ENTITY_IMPORT\\$", strs[4]);
            line = line.replaceAll("\\$ENTITYIDCONS\\$", strs[5]);
            line = line.replaceAll("\\$ENTITYIDSETVALUES\\$", strs[6]);
            strBuf.append(line).append("\n");
        }
        br.close();
        return strBuf.toString();
    }


    private static String[] getEntityFields(String tableName,java.sql.Connection conn)throws Exception{
        List<Map<String, String>> list = getList(tableName,conn);
        String[] strs = new String[7];
        StringBuffer sb0 = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
        StringBuffer sb4 = new StringBuffer();
        StringBuffer sb5 = new StringBuffer();
        StringBuffer sb6 = new StringBuffer();
        Map<String,String> temp = new HashMap<String, String>();
        temp.put("java.math.BigDecimal", "temp");
        int count=0;
        for(Map<String,String> map:list){
            String columnName = map.get("columnName");
            String remarks = map.get("remarks");
            String propertyName = JDBCBaseDao.getEntityPropertyName(columnName);
            String columnClassName = map.get("columnClassName");
            if(columnClassName.contains("java.sql.Timestamp")){
                columnClassName = "java.util.Date";
            }

            String columnTypeName=map.get("columnTypeName");
            String classType = columnClassName.substring(columnClassName.lastIndexOf(".")+1);

            if(!columnClassName.contains("java.lang") && !columnTypeName.equals("NUMBER")){
                temp.put(columnClassName, "true");
            }

            if(columnTypeName.equals("TINYINT")){
                classType ="Integer";
            }
            if(columnTypeName.equals("NUMBER")){
                classType ="Integer";
            }

            sb0.append("	private ").append(classType).append(" ").append(propertyName).append(";");
            if(remarks != null){
                sb0.append("  //").append(remarks);
            }
            sb0.append("\n");
            sb1.append("	"+classType).append("	").append(propertyName).append(",\n");
            sb2.append("	this.").append(propertyName).append(" = ").append(propertyName).append(";\n");
            if(remarks != null){
                sb3.append("	/**\n	 * 描述: ").append(remarks).append("\n	 */\n");
            }

            sb3.append("	public ").append(classType).append(" ").append(getMethodName(propertyName,"get")).append("(){\n 		return this."+propertyName+";\n	}\n\n");
            if(remarks != null){
                sb3.append("	/**\n	 * 描述: ").append(remarks).append("\n	 */\n");
            }
            sb3.append("	public void ").append(getMethodName(propertyName,"set")).append("(").append(classType).append(" ").append(propertyName).append("){\n").append("		this.").append(propertyName).append(" = ").append(propertyName).append(";\n	}\n\n");

            if(count==0){
                sb5.append(classType).append(" ").append(propertyName);
                sb6.append("	this.").append(propertyName).append(" = ").append(propertyName).append(";\n");
            }
            count++;

        }

        for(String t:temp.keySet()){
            if(temp.get(t).equals("true")){
                sb4.append("import ").append(t).append(";\n");
            }
        }
        String condition=sb1.toString();

        strs[0] = sb0.toString();
        strs[1] = condition.substring(0,condition.length()-2);
        strs[2] = sb2.toString();
        strs[3] = sb3.toString();
        strs[4] = sb4.toString();
        strs[5] = sb5.toString();
        strs[6] = sb6.toString();
        return strs;
    }


    public static List<Map<String,String>> getList(String tableName,java.sql.Connection conn) throws SQLException{
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        String sql = "select * from "+tableName + " where 1=2";
        ResultSet rs = null;
        try {
            rs = conn.prepareStatement(sql).executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        Map<String,String> column;
        Map<String,String> colCommentMap = getColCommentMap(tableName, conn);
        for(int i = 1;i<=count;i++){
            column = new HashMap<String,String>();
            column.put("columnName", rsmd.getColumnName(i));
            column.put("columnClassName", rsmd.getColumnClassName(i));
            column.put("columnTypeName", rsmd.getColumnTypeName(i));
            column.put("remarks",colCommentMap.get(rsmd.getColumnName(i)));
            list.add(column);
        }
        jdbcClose(rs);
        return list;
    }

    private static Map<String,String> getColCommentMap(String tableName,java.sql.Connection conn){
        Map<String,String> colCommentMap = new HashMap<String, String>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getColumns(null, "%", tableName, "%");
            while(rs.next()){
                String colname = rs.getString("COLUMN_NAME");
                String remark = rs.getString("REMARKS");
                remark = (remark.equals("") ? null : remark.replace("\n", " ").replace("\r", " ").replace("\t", " "));
                colCommentMap.put(colname, remark);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            jdbcClose(rs);
        }
        return colCommentMap;
    }




    public static String getEntityName(String tableName){
        tableName = tableName.toLowerCase();
        char[] chars = tableName.toCharArray();
        int i = 0;
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

    private static String getMethodName(String name,String prefix)throws Exception{
        if (name == null || name.trim().length() == 0){
            throw new IllegalArgumentException("Name is not specified!");
        }
        String upperCaseVarName = name.replaceFirst(name.substring(0, 1),
                name.substring(0, 1).toUpperCase());
        return prefix + upperCaseVarName;
    }


    public static <T> void jdbcClose(T ... ts) {
        try {
            for(T sourse : ts){
                if(sourse == null){
                    continue;
                }
                if(sourse instanceof Statement){
                    ((Statement) sourse).close();
                }else if(sourse instanceof ResultSet){
                    ((ResultSet) sourse).close();
                }else if(sourse instanceof Connection){
                    ((Connection) sourse).close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
