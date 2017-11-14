package com.luolei.metadata;

import org.junit.Test;

/**
 * @author 罗雷
 * @date 2017/11/14 0014
 * @time 19:06
 */
public class ApplicationTest {

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 500; i++) {
            sb.append("private String value").append(i).append(";\r\n");
        }
        System.out.println(sb.toString());
    }
}
