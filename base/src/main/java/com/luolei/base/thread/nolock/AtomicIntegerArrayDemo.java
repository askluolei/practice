package com.luolei.base.thread.nolock;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * AtomicIntegerArray
 * AtomicLongArray
 * AtomicReferenceArray
 *
 * 除了基本数据类型外，JDK还有数组等符合结构的原子类
 *
 * AtomicIntegerArray 本质是对int[] 类型的封装，使用Unsafe类通过CAS的方式控制int[]在多线程下的安全性
 *
 * @author luolei
 * @date 2017-03-13 16:54
 */
public class AtomicIntegerArrayDemo {

    static AtomicIntegerArray arr = new AtomicIntegerArray(10);
    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 1_0000; i++) {
                arr.getAndIncrement(i % arr.length());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];
        for (int i = 0; i < 10; i++) {
            ts[i] = new Thread(new AddThread());
        }
        for (int i = 0; i < 10; i++) {
            ts[i].start();
        }
        for (int i = 0; i < 10; i++) {
            ts[i].join();
        }
        System.out.println(arr);
    }
}
