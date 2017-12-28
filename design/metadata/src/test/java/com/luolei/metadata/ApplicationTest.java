package com.luolei.metadata;

import org.junit.Test;

/**
 * @author 罗雷
 * @date 2017/11/14 0014
 * @time 19:06
 */
public class ApplicationTest {

    /**
     * 生成501个 value字段 value0 到 value500
     */
    @Test
    public void gen501Value() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 100; i++) {
            sb.append("private String value").append(i).append(";\r\n");
        }
        System.out.println(sb.toString());
    }

    @Test
    public void testGenSetValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("switch(num) { \r\n");
        for (int i = 0; i <= 100; i++) {
            sb.append("case ").append(i).append(" : ").append("setValue").append(i).append("(value);break;\r\n");
        }
        sb.append("default: throw new IllegalArgumentException(\"invalid index num:\" + num);\r\n");
        sb.append("}");
        System.out.println(sb.toString());
    }

    @Test
    public void testGenGetValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("switch(num) { \r\n");
        for (int i = 0; i <= 100; i++) {
            sb.append("case ").append(i).append(" : ").append("return getValue").append(i).append("();\r\n");
        }
        sb.append("default: throw new IllegalArgumentException(\"invalid index num:\" + num);\r\n");
        sb.append("}");
        System.out.println(sb.toString());
    }
}
