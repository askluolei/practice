package com.luolei.base.thread.base;

/**
 * Thread  Runnable
 * Thread 代表线程对象， 调用对象的start方法，会启动一个线程，这个线程将要执行的任务是对象的run方法   如果直接调用thread对象的run方法，那将只是一次方法调用，而不是开启线程
 * Runnable 接口，实现这个接口的类，代表一个任务，可以将这个任务交给一个线程去处理
 *
 * @author luolei
 * @date 2017-03-13 15:34
 */
public class Base01 {
    public static void main(String[] args) throws Exception {
        MyThread thread = new MyThread("extends Thread");
        MyRunnable runnable = new MyRunnable("impl Runnable");
        new Thread(runnable).start();
        thread.start();
    }

    static class MyThread extends Thread {

        public MyThread() {};

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);//sleep方法  不会释放锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis());
        }
    }

    static class MyRunnable implements Runnable {

        private String name;

        public MyRunnable(){};

        public MyRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name);
            System.out.println(Thread.currentThread().getName() + " " + System.currentTimeMillis());
        }

    }
}
