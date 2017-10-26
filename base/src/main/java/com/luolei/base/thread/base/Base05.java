package com.luolei.base.thread.base;

/**
 * ThreadGroup
 * 建议，无论是线程组 还是线程，最好起个名字
 * @author luolei
 * @date 2017-03-13 15:55
 */
public class Base05 implements Runnable {

    public static void main(String[] args) {
        ThreadGroup tg = new ThreadGroup("PrintGroup");
        Thread t1 = new Thread(tg, new Base05(), "T1");
        Thread t2 = new Thread(tg, new Base05(), "T2");
        t1.start();
        t2.start();
        System.out.println(tg.activeCount());
        tg.list();
    }

    @Override
    public void run() {
        String groupAndName = Thread.currentThread().getThreadGroup().getName() + "-" + Thread.currentThread().getName();
        while(true) {
            System.out.println("I am " + groupAndName);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
