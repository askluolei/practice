package com.luolei.base.thread.mytry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 罗雷
 * @date 2017/11/16 0016
 * @time 14:54
 */
public class MyThread implements Runnable {

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Future<?> future;

    @Override
    public void run() {
        try {
            System.out.println("开始 sleep");
            Thread.sleep(5000);
            System.out.println("结束 sleep");
        } catch (InterruptedException e) {
            System.out.println("中断");
        } finally {
            System.out.println("finally ");
        }
    }

    public void start() {
        System.out.println("start");
        future = executorService.submit(this);
    }

    public void stop() {
        System.out.println("stop");
        future.cancel(true);
        executorService.shutdown();
    }

    public static void main(String[] args) throws Exception {
        MyThread thread = new MyThread();
        thread.start();
        Thread.sleep(1000);
        thread.stop();
    }
}
