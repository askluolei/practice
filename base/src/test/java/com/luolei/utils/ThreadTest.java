package com.luolei.utils;

import java.util.concurrent.Semaphore;

/**
 * @author 罗雷
 * @date 2018/2/5 0005
 * @time 9:41
 */
public class ThreadTest {

    private static Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("try to lock");
                try {
                    semaphore.acquire();
                    System.out.println("locked ,to sleep");
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        });
        thread.setName("my test");
        thread.start();
        System.out.println("try to lock");
        try {
            semaphore.acquire();
            System.out.println("locked ,to sleep");
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
