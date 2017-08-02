package com.inter3i.monitor.business.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * DESCRIPTION : 提取促销信息的辅助类
 * USER : zhouhui
 * DATE : 2017/6/15 17:49
 */
public class PromotionHelper {

    private static  final String KEYWORD1="每满";
    private static  final String KEYWORD2="满";

    //[每满]{2}[0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*[减][0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*
    private static final String KEYWORD1_PATTERN = "[\\u6bcf\\u6ee1]{2}[0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*[\\u51cf][0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*";
    //满[0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*减[0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*
    private static final String KEYWORD2_PATTERN = "[\\u6ee1][0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*[\\u51cf][0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*";
    //满[1一]件，总价打[0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]折
    private static final String KEYWORD3_PATTERN = "[\\u6ee1][1[\\u4e00]]\\u4ef6，\\u603b\\u4ef7\\u6253[0,1,2,3,4,5,6,7,8,9,]+[.]?[0,1,2,3,4,5,6,7,8,9,]*\\u6298";

    private Double stickPrice;  //成交价
    private String stickPromotioninfos;  //成交价促销信息

    public static PromotionHelper handle(String promotionInfos, Double price){
        return buildRegularPromotionHelper(promotionInfos,price);
    }

    private static PromotionHelper buildRegularPromotionHelper(String promotionInfos, Double price) {
        PromotionHelper p1 = new PromotionHelper();
        //首先匹配出“每满”的数据
        List<String> list1 = regularExpressionMatching(promotionInfos, KEYWORD1_PATTERN);
        //把满足“每满”的数据替换
        String str = regularReplaceAll(promotionInfos, KEYWORD1_PATTERN, "-");
        //然后在匹配“满”的数据
        List<String> list2 = regularExpressionMatching(str, KEYWORD2_PATTERN);
        //一件x折
        List<String> list3 = regularExpressionMatching(str, KEYWORD3_PATTERN);

        if(CollectionUtils.isNotEmpty(list1) || CollectionUtils.isNotEmpty(list2)){
            evaluate(price,list1,list2,p1);
            if (CollectionUtils.isNotEmpty(list3)){
                String p = list3.get(0);
                Double d = Double.valueOf(p.substring(p.indexOf("打") + 1, p.indexOf("折")));
                Double c = price * (d / 10);
                if(p1.getStickPrice() > c){
                    p1.setStickPrice(c);
                    p1.setStickPromotioninfos(p);
                }
            }
        }else if (CollectionUtils.isNotEmpty(list3)){
            String p = list3.get(0);
            Double d = Double.valueOf(p.substring(p.indexOf("打") + 1, p.indexOf("折")));
            Double c = price * (d / 10);
            p1.setStickPrice(c);
            p1.setStickPromotioninfos(p);
        }
        return p1;
    }

    public static void evaluate(Double price, List<String> list1,List<String> list2,PromotionHelper p1) {
        Double minPrice = 0D;
        StringBuffer pro = new StringBuffer();
        //每满
        if(CollectionUtils.isNotEmpty(list1)){
            Map<String,Double> map = new HashMap<String, Double>();
            for(String s : list2){
                Double b = Double.valueOf(s.substring(s.indexOf("减") + 1, s.length()));
                map.put(s,b);
            }
            for(Map.Entry<String,Double> entry : sortByValue(map).entrySet()){
                String s = entry.getKey();
                Double a = Double.valueOf(s.substring(s.indexOf(KEYWORD1) + 1, s.indexOf("减")));
                Double b = Double.valueOf(s.substring(s.indexOf("减") + 1, s.length()));
                Double num2 = price - (price / a * b);
                if (minPrice == 0 || minPrice > num2) {
                    minPrice = num2;
                    pro.append(s).append(";");
                }
            }
        }

        //满减
        if(CollectionUtils.isNotEmpty(list2)){
            Map<String,Double> map = new HashMap<String, Double>();
            for(String s : list2){
                Double b = Double.valueOf(s.substring(s.indexOf("减") + 1, s.length()));
                map.put(s,b);
            }
            for(Map.Entry<String,Double> entry : sortByValue(map).entrySet()){
                String s = entry.getKey();
                Double a = Double.valueOf(s.substring(s.indexOf(KEYWORD2) + 1, s.indexOf("减")));
                Double b = Double.valueOf(s.substring(s.indexOf("减") + 1, s.length()));
                if (price > a) {
                    double num2 = price - b;
                    if (minPrice == 0 || minPrice > num2) {
                        minPrice = num2;
                        pro.append(s).append(";");
                        break;
                    }
                }
            }
        }
        if(StringUtils.isNotEmpty(pro.toString())){
            p1.setStickPromotioninfos(pro.toString());
            p1.setStickPrice(minPrice);
        }
    }

    /**
     * map根据value倒序排序
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    /**
     * 正则匹配数据
     *
     * @param data    需要匹配的数据
     * @param pattern 正则表达示
     * @return 匹配的数据集合
     */
    private static List<String> regularExpressionMatching(String data, String pattern) {
        List<String> list = new ArrayList<String>();
        // 创建 Pattern 对象
        Pattern pattern1 = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher matcher = pattern1.matcher(data);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 正则替换所有满足条件的数据
     *
     * @param data          需要匹配的数据
     * @param pattern       正则表达示
     * @param replaceString 替换字段
     * @return 替换匹配的数据
     */
    private static String regularReplaceAll(String data, String pattern, String replaceString) {
        return data.replaceAll(pattern, replaceString);
    }


    public Double getStickPrice() {
        return stickPrice;
    }

    public void setStickPrice(Double stickPrice) {
        this.stickPrice = stickPrice;
    }

    public String getStickPromotioninfos() {
        return stickPromotioninfos;
    }

    public void setStickPromotioninfos(String stickPromotioninfos) {
        this.stickPromotioninfos = stickPromotioninfos;
    }
}
