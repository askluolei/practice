package com.luolei.base.thread.base;

/**
 * 中断的3个方法
 * interrupt
 * isInterrupted
 * Thread.interrupted
 *
 * @author luolei
 * @date 2017-03-13 15:36
 */
public class Base02 {
    static class MyThread extends Thread {

        @Override
        public void run() {
            int i = 0;
            System.out.println(i);
            while (true) {
                i++;
            }
        }
    }

    public static void main(String[] args) {
        try {
            MyThread thread = new MyThread();
            thread.start();
            Thread.sleep(1000);
            thread.interrupt();//中断线程	就是设置中断标志位	sleep方法会相应中断，抛出InterruptedException（就是要强制处理的异常）,并清除中断标志位
//			thread.isInterrupted();//判断线程是否被中断，通过检查中断标志位
//			Thread.interrupted();//判断是否被中断，并清除当前中断状态
            System.out.println("是否停止1? = " + Thread.interrupted());
            System.out.println("是否停止2? = " + Thread.interrupted());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
