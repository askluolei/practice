package com.luolei.base.thread.base;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁 和 非公平锁
 * 一般很少用公平锁
 *
 * @author luolei
 * @date 2017-03-13 16:21
 */
public class Base13 {
    public static class FairLock implements Runnable {
        public static ReentrantLock lock = new ReentrantLock(true);//构造方法可以有个参数，默认是false
        public void run() {
            while (true) {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " 获得锁");
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        FairLock rl = new FairLock();
        Thread t1 = new Thread(rl, "Thread_1");
        Thread t2 = new Thread(rl, "Thread_2");
        t1.start();
        t2.start();
    }
}
