package com.luolei.tools.zookeeper;

import org.junit.Test;

/**
 * @author 罗雷
 * @date 2017/11/3 0003
 * @time 17:45
 */
public class StrTest {

    @Test
    public void test() {
        String node = "node-1";
        String[] strings = node.split("-");
        for (String s : strings) {
            System.out.println(s);
        }
    }
}
