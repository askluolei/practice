package com.luolei.base.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 扩展线程池
 * 虽然JDK已经帮我们实现了这个稳定的高性能线程池，但如果我们需要对这个线程池做一些扩展，比如，我们想监控每个任务执行的开始和结束时间，或者其他一些自定义的增强功能，可以扩展线程池。
 * ThreadPoolExecutor提供了beforeExecute  afterExecute  terminated()三个方法对线程池进行控制
 * @author 罗雷
 * @date 2016年9月12日
 * @time 下午11:20:21
 */
public class ExtThreadPool {

	public static class MyTask implements Runnable {
		private String name;
		
		public MyTask(String name) {
			this.name = name;
		}
		
		public void run() {
			System.out.println("正在执行" + ":Thread ID:" + Thread.currentThread().getId() + ".Task Nmae=" + name);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()) {
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				System.out.println("准备执行： " + ((MyTask)r).name);
			}
			
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				System.out.println("完成执行： " + ((MyTask)r).name);
			}
			
			@Override
			protected void terminated() {
				System.out.println("线程池退出");
			}
		};
		
		for (int i = 0; i < 5; i++) {
			MyTask task = new MyTask("TASK-GEYM-" + i);
			es.execute(task);
			Thread.sleep(10);
		}
		es.shutdown();
	}
}
