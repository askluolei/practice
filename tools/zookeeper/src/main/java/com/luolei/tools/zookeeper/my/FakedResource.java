package com.luolei.tools.zookeeper.my;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟临界资源
 *
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 13:52
 */
public class FakedResource {

    private static final int max_size = 5;
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static int maxSize() {
        return max_size;
    }

    public static void use() {
        if (counter.incrementAndGet() <= max_size) {
            System.out.println("i am in use");
        } else {
            throw new IllegalStateException("非法操作, 处理线程数超过上线");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        System.out.println("i am use over");
        counter.decrementAndGet();
    }
}
