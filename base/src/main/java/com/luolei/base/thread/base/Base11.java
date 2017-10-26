package com.luolei.base.thread.base;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁 响应中断
 * 下面的例子 T1, T2 实现死锁，互相等待锁资源
 * 然后 设置t2 中断， t2响应中断，抛出异常，释放资源
 * t1 获取锁资源，正常结束
 *
 * @author luolei
 * @date 2017-03-13 16:05
 */
public class Base11 {
    public static class IntLock implements Runnable {

        public static ReentrantLock lock1 = new ReentrantLock();
        public static ReentrantLock lock2 = new ReentrantLock();

        int lock;

        public IntLock(int lock) {
            this.lock = lock;
        }

        public void run() {
            try {
                if (lock == 1) {
                    lock1.lockInterruptibly();
                    try {
                        Thread.sleep(500);//等待t2获取锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock2.lockInterruptibly();
                } else {
                    lock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);//等待t1获取锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock1.lockInterruptibly();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
                System.out.println(Thread.currentThread().getId() + " :退出线程");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        IntLock r1 = new IntLock(1);
        IntLock r2 = new IntLock(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        t2.interrupt();
    }
}
