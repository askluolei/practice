package com.luolei.base.thread.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ArrayList 在多线程下的问题  因为ArrayList在扩容的过程中，内部一致性被破坏
 * 改进方法 Vector,或者使用Collections工具类的unmodify***方法变成不可变集合
 * HashMap也有同样的问题
 *
 * @author luolei
 * @date 2017-03-13 16:03
 */
public class Base09 {
    static ArrayList<Integer> al = new ArrayList<>();

    static Map<String, String> map = new HashMap<>();

    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 100_0000; i++) {
                al.add(i);
                //map.put(Integer.toString(i), Integer.toBinaryString(i));
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread());
        Thread t2 = new Thread(new AddThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(map.size());
    }
}
