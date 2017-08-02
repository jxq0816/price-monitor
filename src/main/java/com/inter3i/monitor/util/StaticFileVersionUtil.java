package com.inter3i.monitor.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/2 16:48
 */
public class StaticFileVersionUtil {
    private static String[] ignore_folder = new String[]{
            "bootstrap",
            "angular",
            "layer",
            "china",
            "FusionChart",
            "highcharts",
            "highmaps",
            "jqtransformplugin",
            "My97DatePicker",
            "treeTable",
            "ztree",
            "jquery",
            "plugins"
    };


    public static void main(String[] args) {
        String version = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File viewFolder = new File(System.getProperty("user.dir"));
        reaplaceFolder(version, viewFolder);
    }

    public static void reaplaceFolder(String version,File file){
        try {
            //读取指定文件夹下的所有文件
            if (file.isDirectory()) {
                String[] filelist = file.list();
                if(filelist!=null){
                    for (int i = 0; i < filelist.length; i++) {
                        File readfile = new File(file.getAbsoluteFile() + "\\" + filelist[i]);
                        if(readfile.isDirectory()){
                            reaplaceFolder(version, readfile);
                        }else{
                            if(readfile.getName().endsWith(".vm")){
                                replaceContent(version, readfile);
                            }
                        }
                    }
                }
            } else {
                if(file.getName().endsWith(".vm")){
                    replaceContent(version, file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void replaceContent(String version,File file){
        try{
            String encoding = "UTF-8";//设置文件的编码
            String absolutepath = file.getAbsolutePath();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutepath), encoding));//数据流读取文件
            StringBuffer strBuffer = new StringBuffer();
            int count = 0;
            boolean hasChange = false;
            for (String temp = null; (temp = bufReader.readLine()) != null; temp = null) {
                String line = temp.trim();
                if(line.toLowerCase().startsWith("<link ") && (line.endsWith("/>") || line.toLowerCase().endsWith("</link>")) && line.toLowerCase().indexOf("href")!=-1){
                    String href = getDocmentAttrVal(line, "href");
                    if(href != null){
                        if(href.indexOf("?") != -1 && href.indexOf("v=") == -1 ){

                        }else{
                            if(!isIgnore(href)){
                                if(href.indexOf("?v=") == -1){
                                    temp = temp.replace(href, href + "?v=" + version);//替换为你想要的东东
                                }else{
                                    temp = temp.replace(href, href.subSequence(0, href.indexOf("?v=")) + "?v=" + version);
                                }
                                hasChange = true;
                            }
                        }
                    }
                }else if(line.toLowerCase().startsWith("<script ") && (line.trim().endsWith("/>") || line.toLowerCase().trim().endsWith("</script>")) && line.toLowerCase().indexOf("src")!=-1){
                    String src = getDocmentAttrVal(line, "src");
                    if(src != null){
                        if(src.indexOf("?") != -1 && src.indexOf("v=") == -1 ){

                        }else{
                            if(!isIgnore(src)){
                                if(src.indexOf("?v=") == -1){
                                    temp = temp.replace(src, src + "?v=" + version);//替换为你想要的东东
                                }else{
                                    temp = temp.replace(src, src.subSequence(0, src.indexOf("?v=")) + "?v=" + version);
                                }
                                hasChange = true;
                            }
                        }
                    }
                }
                if(count ++ != 0){
                    strBuffer.append(System.getProperty("line.separator"));//行与行之间的分割
                }
                strBuffer.append(temp);
            }
            bufReader.close();
            if(hasChange){
                OutputStreamWriter outstream = new OutputStreamWriter(new FileOutputStream(absolutepath), encoding);
                PrintWriter printWriter = new PrintWriter(outstream);//替换后输出的文件位置（切记这里的E:/ttt 在你的本地必须有这个文件夹）
                printWriter.write(strBuffer.toString().toCharArray());
                printWriter.flush();
                printWriter.close();
                System.out.println("版本号生成成功 :" + absolutepath);
            }else{
                System.out.println("无引用的css和js所以版本号未生成 :" + absolutepath);
            }
        }catch(Exception e){
            System.out.println("--------出错了出错了-------");
            System.out.println(file.getAbsolutePath());
            System.out.println("---------------");
            e.printStackTrace();
        }
    }

    private static boolean isIgnore(String src){
        boolean isIgnore = false;
        for(String ignore : ignore_folder){
            if(src.toLowerCase().contains(ignore.toLowerCase())){
                isIgnore  = true;
                break;
            }
        }
        return isIgnore;
    }

    private static String getDocmentAttrVal(String xml,String attr){
        try {
            Document doc = DocumentHelper.parseText(xml.trim());
            return doc.getRootElement().attributeValue(attr);
        } catch (Exception e) {
        }
        return null;
    }
}
