package com.luolei.utils;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author 罗雷
 * @date 2018/1/16 0016
 * @time 14:29
 */
public class StringValueOfTest {

    /**
     * 精度丢失问题
     */
    @Test
    public void test() {
        System.out.println(new BigDecimal(17.13).multiply(new BigDecimal(5)));
        System.out.println(new BigDecimal(String.valueOf("17.13")).multiply(new BigDecimal(String.valueOf("5"))));
        System.out.println(new BigDecimal(17.13).multiply(new BigDecimal(5)).setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println(String.valueOf(17.13 * 5));
    }
}
