package com.luolei.base.thread.alive;

/**
 * 当调用Thread对象的start方法后线程才开始启动
 * Thread.currentThread() 获取的是当前只在执行的线程
 * thread.isAlive() 判断线程是否存活
 *
 * @author luolei
 * @date 2017-03-13 15:27
 */
public class CountOperate extends Thread {

    public static void main(String[] args) {
        CountOperate c = new CountOperate();
        System.out.println("main begin tl isAlive = " + c.isAlive());
        c.setName("A");
        c.start();
        System.out.println("main end tl isAlive = " + c.isAlive());
    }

    public CountOperate() {
        System.out.println("CountOperate---begin");
        System.out.println("Thread.currentThread().getName()=" + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());
        System.out.println("this.getName()=" + this.getName());
        System.out.println("this.isAlive()=" + this.isAlive());
        System.out.println("CountOperate---end");
    }

    @Override
    public void run() {
        System.out.println("run---begin");
        System.out.println("Thread.currentThread().getName()=" + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());
        System.out.println("this.getName()=" + this.getName());
        System.out.println("this.isAlive()=" + this.isAlive());
        System.out.println("run---end");
    }
}
