package com.luolei.base.jdk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author 罗雷
 * @date 2018/3/16 0016
 * @time 10:43
 */
public class HashMapStudy {

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static final void newLine() {
        System.out.println("===== 我是分隔符 =======");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Map<String, String> map = new HashMap<>();
        System.out.println("123".hashCode());
        System.out.println(hash("123"));

        newLine();
        System.out.println(2 >>> 16);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<String> future = executorService.submit(() -> {
            System.out.println("I am in a new thread");
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread end");
        }, "this is result");
        System.out.println(future.get());
    }

}
