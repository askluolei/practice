package com.luolei.base.thread.locktools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 * 信号量是对锁的扩展，无论是内部锁synchronized还是重入锁ReetrantLock，一次都只运行一个线程访问一个资源，而信号量可以指定多个线程访问某个资源
 *
 * 构造方法如下：
 * public Semaphore(int permits)	//参数指定准入数，也就是多少个线程访问，每个线程每次只能申请一个许可
 * public Semaphore(int permits, boolean fair)	//第二个参数为是否公平
 *
 * 主要逻辑方法有：
 * void acquire() throws InterruptedException	//尝试获取一个许可，若无法获得，线程会等待，直到有现车释放一个许可，或者当前线程被中断
 * void acquireUninterruptibly()	//跟上面一样，但是不响应中断
 * boolean tryAcquire()	//尝试获取一个许可，成功返回true，失败返回false，不会进行等待，立即返回
 * boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException	//尝试获取一个许可，如果获取不到，只等待指定时长，
 * void release()	//释放一个许可
 *
 * 申请了许可，使用后一定要释放掉，否则会导致信号量泄露，会导致能够进入临界区的线程越来越少，直到所有线程都不能访问
 *
 * @author luolei
 * @date 2017-03-13 16:53
 */
public class SemapDemo implements Runnable{

    final Semaphore semp = new Semaphore(5);

    @Override
    public void run() {
        try {
            semp.acquire();
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + ":Done!");
            semp.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(20);
        final SemapDemo demo = new SemapDemo();
        for (int i = 0; i < 20; i++) {
            exec.submit(demo);
        }
        exec.shutdown();
    }
}
