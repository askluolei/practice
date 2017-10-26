package com.luolei.base.thread.locktools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 倒计时器
 * 作用跟join类似，不过它能够控制等待多少线程
 * 下面的示例，main在等待10个线程完成任务后再开始继续执行
 *
 * @author luolei
 * @date 2017-03-13 16:47
 */
public class CountDownLatchDemo implements Runnable{

    static final CountDownLatch end = new CountDownLatch(10);
    static final CountDownLatchDemo demo = new CountDownLatchDemo();

    public void run() {
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println("check complete");
            end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            exec.submit(demo);
        }

        end.await();

        System.out.println("fire!");
        exec.shutdown();
    }
}
