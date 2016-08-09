package zy.chasegoddness.util;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/8/9.
 */
public class DateUtil {
    public static boolean isSameDay(Calendar day1, Calendar day2) {
        if (day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR)
                && day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR))
            return true;
        return false;
    }
}
