package com.luolei.base.thread.base;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁	限时等待  锁释放的时候要注意当前线程是否持有锁
 *
 * @author luolei
 * @date 2017-03-13 16:11
 */
public class Base12 {
    public static class TimeLock implements Runnable {
        public static ReentrantLock lock = new ReentrantLock();
        public void run() {
            try {
                if (lock.tryLock(5, TimeUnit.SECONDS)) {
                    Thread.sleep(6000);
                } else {
                    System.out.println("get lock failed");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        TimeLock tl = new TimeLock();
        Thread t1 = new Thread(tl);
        Thread t2 = new Thread(tl);
        t1.start();
        t2.start();
    }
}
