package com.luolei.base.thread.locktools;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环栅栏，
 * 构造方法如下：
 * public CyclicBarrier(int parties, Runnable barrierAction) //第一个参数指定等待的线程数，第二个参数是等待的线程数到达指定数量后出发的动作
 *
 * 这个类可以循环使用，下面的例子是每满10个线程等待就会触发一次指定的线程任务
 *
 * @author luolei
 * @date 2017-03-13 16:48
 */
public class CyclicBarrierDemo {
    public static class Soldier implements Runnable {
        private String soldier;
        private final CyclicBarrier cyclic;

        public Soldier(CyclicBarrier cyclic, String soldierName) {
            this.cyclic = cyclic;
            this.soldier = soldierName;
        }

        public void run() {
            try {
                cyclic.await();
                doWork();
                cyclic.await();
            } catch (InterruptedException | BrokenBarrierException e) {//第二个异常，代表着CyclicBarrier已经破损了，可能系统已经没有办法等待所有线程到齐了
                e.printStackTrace();
            }
        }

        void doWork() {
            try {
                Thread.sleep(Math.abs(new Random().nextInt()%10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(soldier + "：任务完成");
        }
    }

    public static class BarrierRun implements Runnable {
        boolean flag;
        int N;

        public BarrierRun(boolean flag, int N) {
            this.flag = flag;
            this.N = N;
        }

        public void run() {
            if (flag) {
                System.out.println("司令：【士兵" + N + "个，任务完成！】");
            } else {
                System.out.println("司令：【士兵" + N + "个，集合完毕！】");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] allSoldier = new Thread[10];
        boolean flag = false;
        CyclicBarrier cyclic = new CyclicBarrier(N, new BarrierRun(flag, N));
        System.out.println("集合队伍");
        for (int i = 0; i < N; i++) {
            System.out.println("士兵" + i + "报道！");
            allSoldier[i] = new Thread(new Soldier(cyclic, "士兵" + i));
            allSoldier[i].start();
        }
    }
}
