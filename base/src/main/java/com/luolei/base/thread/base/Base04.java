package com.luolei.base.thread.base;

/**
 * Thread 对象方法
 * join 当前线程等待，join的这个线程执行完成后再执行
 *
 * @author luolei
 * @date 2017-03-13 15:52
 */
public class Base04 {
    public volatile static int i = 0;

    public static class AddThread extends Thread {
        @Override
        public void run() {
            for (i = 0; i < 100_0000; i++);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread add = new AddThread();
        add.start();
        add.join();
        System.out.println(i);
    }
}
