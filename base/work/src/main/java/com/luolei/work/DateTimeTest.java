package com.luolei.work;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/**
 * @author 罗雷
 * @date 2017/11/10 0010
 * @time 15:24
 */
public class DateTimeTest {

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        String str = "2017-11-10 11:00:05.853";
        LocalDateTime time = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(time.toString());
    }
}
