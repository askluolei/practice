package com.luolei.base.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Start {

	public static class MyTask implements Runnable {

		public String name;
		
		public MyTask(String name) {
			this.name = name;
		}
		
		@Override
		public void run() {
			System.out.println("正在执行" + this.name + "  " + System.currentTimeMillis());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		ExecutorService es = new ThreadPoolExecutor(5, 5, //第一个参数是活跃线程数，线程池中将维持的线程，不管是否空闲。第二个参数是最大线程数
				0L, //多余空闲线程存活时间，当前池中的线程 - 一个个参数就是多余线程，这些线程如果空闲，会在指定时间内被回收
				TimeUnit.MILLISECONDS, //上面参数的时间单位
				new LinkedBlockingQueue<>(10),//等待队列的类型
				Executors.defaultThreadFactory(),//ThreadFacoty  线程工厂，用来创建线程，也可以自己实现
				new RejectedExecutionHandler() {//拒绝策略，当提交的线程es.execute(runnable)时，线程池已经到最大线程，等待队列也已经满了，这个时候的执行拒绝策略，默认的拒绝策略为 AbortPllicy，即直接拒绝并抛异常，可以自己实现接口，自定义拒绝策略
					
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						System.out.println(r.toString() + " is discard");
					}
				}) {
			
			//将任务提交到ExecutorService，任务开始执行之前
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				System.out.println("准备执行:" + ((MyTask)r).name + " " + System.currentTimeMillis());
			}
			
			//任务执行完成后
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				System.out.println("执行完成:" + ((MyTask)r).name + " " + System.currentTimeMillis());
			}

			//任务退出后
			@Override
			protected void terminated() {
				System.out.println("线程池退出");
			}
		};
		
		for (int i = 0; i < 5; i++) {
			MyTask task = new MyTask("TASK-" + 1);
			es.execute(task);
			Thread.sleep(10);
		}
		es.shutdown();
	}
}

