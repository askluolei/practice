package com.luolei.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 罗雷
 * @date 2018/1/16 0016
 * @time 15:57
 */
public class DateTest {

    private static final int MINUTE = 5;

    public static boolean isAroundZero() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        System.out.println(hour);
        System.out.println(minute);

        boolean result = false;
        if (hour == 23) {
            result = minute > 55;
        } else if (hour == 24) {
            result = minute < 5;
        } else if (hour == 0) {
            result = minute < 5;
        }
        return result;
    }

    public static boolean isAroundZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        System.out.println(hour);
        System.out.println(minute);

        boolean result = false;
        if (hour == 23) {
            result = minute > 55;
        } else if (hour == 24) {
            result = minute < 5;
        } else if (hour == 0) {
            result = minute < 5;
        }
        return result;
    }

    @Test
    public void test() {
        System.out.println(isAroundZero());
        long timestamp = System.currentTimeMillis();
        long time = timestamp + (3600 * 7 + 60 * 51) * 1000;
        Date date = new Date(time);
        System.out.println(isAroundZero(date));
    }
}
