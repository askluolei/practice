package com.luolei.base.thread.locktools;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程阻塞工具类
 * 它可以在线程内任意位置让线程阻塞，不需要先获得一个对象的锁，方法是静态调用
 *
 * public static void park()	//线程阻塞
 * public static void park(Object blocker)	//作用跟上一个方法相同，设置一个阻塞对象，这个对象会出现在线程DUMP中
 * public static void parkNanos(Object blocker, long nanos)	//指定一个阻塞时间
 * public static void unpark(Thread thread)	//取消阻塞
 *
 * park()方法支持中断影响，但是不会抛出异常，默默的返回，代码后面可以通过Thread.interrupted()等方法获取中断标记
 *
 * @author luolei
 * @date 2017-03-13 16:50
 */
public class LockSupportDemo {
    public static Object u = new Object();
    static ChangeObjectThread t1 = new ChangeObjectThread("t1");
    static ChangeObjectThread t2 = new ChangeObjectThread("t2");

    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(1000);
        t2.start();
        LockSupport.unpark(t1);
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }
}
