package com.luolei.base.thread.base;

/**
 * 线程优先级
 * 通过Thread对象方法
 * setPriority
 * 设置线程优先级
 * 优先级高的线程有更大可能获取到锁  只是获取锁的概率大点，不一定是优先级低的抢不过优先级高的
 *
 * @author luolei
 * @date 2017-03-13 15:59
 */
public class Base07 {
    public static class HighPriority extends Thread {
        static int count = 0;
        @Override
        public void run() {
            while (true) {
                synchronized (Base07.class) {//类锁
                    count++;
                    if (count > 1000_0000) {
                        System.out.println("HightPriority is complete");
                        break;
                    }
                }
            }
        }
    }

    public static class LowPriority extends Thread {
        static int count = 0;
        @Override
        public void run() {
            while (true) {
                synchronized (Base07.class) {
                    count++;
                    if (count > 1000_0000) {
                        System.out.println("LowPriority is complete");
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread high = new HighPriority();
        Thread low = new LowPriority();
        high.setPriority(Thread.MAX_PRIORITY);
        low.setPriority(Thread.MIN_PRIORITY);
        low.start();
        high.start();
    }
}
