package com.inter3i.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by koreyoshi on 2017/6/14.
 */
public class GetTimeUtil {
    //获取n天前零点的时间戳
    public Long getLastDayStartTime(Long receiveTime, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(receiveTime - (86400000 * n));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long LastDayStartTime = calendar.getTimeInMillis();
        return LastDayStartTime;
    }

    //获取今天的date日期
    public String getTodayDate(Date receiveDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(receiveDate);
        return dateNowStr;
    }

    //时间戳转日期 返回年月日
    public String changeLongtimeToDate(Long receiveTime) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(receiveTime);
        res = simpleDateFormat.format(date);
        return res;
    }


}
