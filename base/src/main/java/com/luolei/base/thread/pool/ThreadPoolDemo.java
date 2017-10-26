package com.luolei.base.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程的软件设计确实可以最大限度的发挥现代多核处理器的计算能力，提高生产系统的吞吐量，但是，若不加控制和管理的随意使用线程，对系统的性能反而会产生不利影响
 * 为了避免系统频繁的创建和销毁线程，我们可以让创建的线程进行复用，线程池中总有那么几个活跃线程，当你需要使用线程时，可以从池子里随便拿一个空闲线程，当完成工作时，并不着急着关闭线程，而是将这个线程放回池中。
 * 
 * 为了更好的控制多线程，JDK提供了一套Executor框架，帮助开发人员有效的进行线程控制，其本质是一个线程池，Executors类是一个线程池的工厂类，通过Executors可以取得一个拥有特定功能的线程池
 * public static ExecutorService newFixedThreadPool(int nThreads)	//返回一个固定线程数量的线程池，该线程池中线程数量始终不变，当有一个新任务提交时，线程池中有空闲线程，则立即执行，否则，新的任务会被暂存在一个任务队列中，待线程池中有空闲线程再执行
 * public static ExecutorService newSingleThreadExecutor()	//返回一个只有一个线程的线程池，任务提交后，会被保存到任务队列中，待线程空闲，按先入先出顺序执行队列中的任务
 * public static ExecutorService newCachedThreadPool()	//返回一个可以根据实际情况调整线程数量的线程池，线程池的数量不确定，但若有空闲线程可以复用，则会优先使用可复用的线程，若所有线程均在工作，又有新任务提交，则会创建新的线程处理任务，所有线程在当前任务执行完毕后，将返回给线程池进行复用
 * public static ScheduledExecutorService newSingleThreadScheduledExecutor()//返回一个ScheduleExecutorService对象，线程池大小为1， 是ExecutorServcie接口的扩展，在给定时间执行某任务
 * public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)//可以知道线程数量
 * 
 * @author 罗雷
 * @date 2016年9月11日
 * @time 上午8:51:37
 */
public class ThreadPoolDemo {
	
	public static class MyTask implements Runnable {
		@Override
		public void run() {
			System.out.println(System.currentTimeMillis() + "Thread ID:" + Thread.currentThread().getId());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MyTask task = new MyTask();
		ExecutorService es = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 10; i++) {
			es.submit(task);
		}
		es.shutdown();
	}
}
