package com.mzj.mohome.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author admin
 * @ClassName DateUtils
 * @Description 时间工具
 * @Date 2023/11/28 13:58
 */
public class DateUtils {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 获取年月日，时分
     * @param date
     * @return
     */
    public static String getYYYY_MM_DD(Date date){
        if(date != null){
            return simpleDateFormat.format(date);
        }
        return "";
    }

    public static String getSysDateString(String str){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat(str);
        if(date != null){
            return simpleDateFormat_1.format(date);
        }
        return "";
    }
}
