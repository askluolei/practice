package com.luolei.base.thread.base;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁,初体验
 *
 * @author luolei
 * @date 2017-03-13 16:04
 */
public class Base10 {
    public static class ReenterLock implements Runnable {
        public static ReentrantLock lock = new ReentrantLock();
        public static int count = 0;

        public void run() {
            for (int i = 0; i < 100_0000; i++) {
                lock.lock();
                try {
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLock tl = new ReenterLock();
        Thread t1 = new Thread(tl);
        Thread t2 = new Thread(tl);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(ReenterLock.count);
    }
}
