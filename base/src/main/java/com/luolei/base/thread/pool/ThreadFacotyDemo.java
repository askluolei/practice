package com.luolei.base.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池中的线程是怎么来的？
 * 线程池的主要作用是为了线程复用，也就是避免了线程的频繁创建，但是最开始的线程从何而来？答案是ThreadFacoty，ThreadFacoty是一个接口，它只有一个方法用来创建线程
 * Thread newThread(Runnable r);
 * 
 * 自定义线程可以帮助我们做不少事，比如，我们可以跟踪线程池究竟在何时创建了多少线程，也可以自定义线程的名称，组合优先级等信息，甚至可以将所有线程设置为守护线程，总之，自定义线程池可以让我们更加自由的设置池子中所有线程的状态
 * @author 罗雷
 * @date 2016年9月12日
 * @time 下午11:02:23
 */
public class ThreadFacotyDemo {

	public static class MyTask implements Runnable {
		public void run() {
			System.out.println(System.currentTimeMillis() + ":Thread ID:" + Thread.currentThread().getId());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		MyTask myTask = new MyTask();
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (r) -> {
			Thread t = new Thread(r);
			t.setDaemon(true);
			System.out.println("create " + t);
			return t;
		});
		
		for (int i = 0; i < 5; i++) {
			es.submit(myTask);
		}
		Thread.sleep(2000);
		es.shutdown();
	}
}
