package com.luolei.base.thread.base;

/**
 * synchronized
 * 同步关键字，相当于排它锁
 * 可以在类方法上，对象方法上，同步块
 *
 * 不要对基本数据类型的封装类加锁，会出现问题，因为有基本类型有自动拆包，打包，对象会变化
 *
 * @author luolei
 * @date 2017-03-13 16:01
 */
public class Base08 {
    public static volatile int i = 0;

    public static class Add implements Runnable {

        public void run() {
            for (int j = 0; j < 1000_0000; j++) {
//				i++;
                add();
            }
        }

        public synchronized static void add() {
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Add add = new Add();
        Thread t1 = new Thread(add);
        Thread t2 = new Thread(add);
        t1.start();
        t1.join();
        t2.start();
        t2.join();
        System.out.println(i);
    }
}
