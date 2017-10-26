package com.luolei.base.thread.locktools;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition对象 和Object.wait  和Object.notify，notifyAll功能相同
 * Condition对象跟重入锁配合使用，主要使用以下方法
 *
 * void await() throws InterruptedException;	//使当前线程等待，同时释放锁	跟wait方法类似，注意， 方法名 await，很容易使用成了wait
 * void awaitUninterruptibly();		//跟await方法基本相同，但是他并不会在等待过程中响应中断
 * long awaitNanos(long nanosTimeout) throws InterruptedException;	//跟await方法一样，不过只等待指定时间
 * boolean await(long time, TimeUnit unit) throws InterruptedException;	//跟上面方法一样
 * boolean awaitUntil(Date deadline) throws InterruptedException;	//等到指定的时间就不等了
 * void signal();//激活一个在condition对象上的一个线程
 * void signalAll();//唤醒所有线程
 *
 * 在JDK内部，重入锁和Condition对象被广泛使用
 *
 * @author luolei
 * @date 2017-03-13 16:53
 */
public class ReenterLockCondition implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();//先要获取锁
            condition.await();//在condition对象上等待
            System.out.println("Thread is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//释放锁
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLockCondition tl = new ReenterLockCondition();
        Thread t1 = new Thread(tl, "Condition");
        t1.start();
        Thread.sleep(2000);
        lock.lock();
        condition.signal();//激活condition上的一个等待线程
        lock.unlock();
    }
}
