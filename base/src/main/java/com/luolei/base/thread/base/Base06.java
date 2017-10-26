package com.luolei.base.thread.base;

/**
 * 后台线程  deamon
 * 调用Thread对象的setDeamon(true) 就可以将线程设置为后台线程
 * 当没有除了后台线程以外的其他线程正在运行时，JVM进程终止
 *
 * @author luolei
 * @date 2017-03-13 15:57
 */
public class Base06 {
    public static class DeamonT extends Thread {

        @Override
        public void run() {
            while (true) {
                System.out.println("I am alive");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new DeamonT();
        t.setDaemon(true);
        t.start();
        Thread.sleep(2000);
    }
}
