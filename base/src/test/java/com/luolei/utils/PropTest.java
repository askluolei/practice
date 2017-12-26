package com.luolei.utils;

import org.junit.Test;

import java.util.Properties;

/**
 * @author 罗雷
 * @date 2017/11/24 0024
 * @time 10:27
 */
public class PropTest {

    @Test
    public void test() {
        Properties properties = new Properties();
        String result = properties.getProperty("abc");
        System.out.println(result == null);
    }
}
