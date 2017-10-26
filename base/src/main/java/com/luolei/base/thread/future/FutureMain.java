package com.luolei.base.thread.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author luolei
 * @date 2017-03-13 16:30
 */
public class FutureMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> future = new FutureTask<>(new RealData("a"));
        ExecutorService executor = Executors.newFixedThreadPool(1);

        executor.submit(future);
        System.out.println("请求完毕");
        Thread.sleep(2000);

        System.out.println("数据 = " + future.get());
    }
}
