package com.luolei.base.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor的最后一个参数指定了拒绝策略，也就是当任务数量超过系统实际承载能力时，就会使用拒绝策略
 * JDK内置的拒绝策略如下：
 * 1.AbortPolicy策略：该策略会直接抛出异常，阻止系统正常工作
 * 2.CallerRunsPolicy策略：只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务，显然这样做不会真的丢弃任务，但是，任务提交线程的性能极有可能急剧下降
 * 3.DiscardOledestPolicy策略：该策略将丢弃最老的一个请求，也就是即将被执行的以一个任务，并尝试再次提交当前任务
 * 4.DiscardPolicy策略：该策略默默的丢弃无法处理的任务，不予任何处理。
 * 
 * 以上内置策略均实现了RejectedExecutionHandler接口，若以上策略仍无法满足实际应用需要，完全可以自己扩展RejectedExecutionHanlder接口，接口中只有一个方法，定义如下：
 * void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
 * executor为当前线程池，r为提交的任务
 * @author 罗雷
 * @date 2016年9月12日
 * @time 下午10:06:19
 */
public class RejectThreadPoolDemo {

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
		MyTask task = new MyTask();
		ExecutorService es = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<>(10), 
				(r, executor) -> {
					System.out.println(r.toString() + "is discard");
				});
		
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			es.submit(task);
			Thread.sleep(10);
		}
	}
}
