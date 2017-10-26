package com.luolei.base.thread.base;

/**
 * 等待，通知 的3个方法
 * 这3个方法都是Object对象上的
 * 都需要先获取锁，才能使用这三个方法，否则会抛异常 IllegalMonitorStateException
 * wait 放弃锁，等待被通知唤醒
 * notify 唤醒一个在锁对象上等待的线程，  被唤醒的这个线程不一定会拿到锁，只是有获取锁的资格了，调用notify方法的线程，还不会释放锁，当它是否锁后，被唤醒的线程可以加入到锁的竞争中
 * notifyAll 唤醒所有在锁对象上等待的线程  同样的，只是有获取这个锁的资格了，具体是否获取锁，看调用notifyAll方法的线程是否释放锁，以及是否有其他线程竞争锁
 *
 * @author luolei
 * @date 2017-03-13 15:44
 */
public class Base03 {
    final static Object object = new Object();

    public static class T1 extends Thread {
        @Override
        public void run() {
//            try {
//                object.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + " :T1 start! ");
                try {
                    System.out.println(System.currentTimeMillis() + " T1 wait for object");
                    object.wait();//wait 方法会释放目标对象的锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis() + " T1 end!");
        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);//让T1先获取对象锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + " T2 start! notify one thread");
                object.notify();
                System.out.println(System.currentTimeMillis() + " T2 end!");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new T1();
        Thread t2 = new T2();
        t1.start();
        t2.start();
    }
}
