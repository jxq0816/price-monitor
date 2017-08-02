package com.inter3i.monitor.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by jiangxingqi on 2017/6/23.
 */
public class DateComparatorHelp implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Map m1=(Map)o1;
        Map m2=(Map)o2;
        String date1=(String)m1.get("date");
        String date2=(String)m2.get("date");
        if(date1.compareTo(date2)>0){
            return 1;
        }else{
            return -1;
        }
    }
}
