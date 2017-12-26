package com.luolei.utils;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author 罗雷
 * @date 2017/11/20 0020
 * @time 9:19
 */
public class BigDecimalTest {

    @Test
    public void test() {
        BigDecimal a = new BigDecimal("1.00");
        BigDecimal b = new BigDecimal("2.00");

        BigDecimal c = a.add(b);

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }
}
