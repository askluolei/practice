package com.luolei.base.thread.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 值得注意的是，调度程序实际上并不保证任务会无限期的持续调用，如果任务本身抛出了异常，那么后续所有执行都会被中断，因此，如果你想让你的任务持续稳定的执行，做好异常处理非常重要
 * @author 罗雷
 * @date 2016年9月11日
 * @time 下午10:31:50
 */
public class ScheduledExecutorServiceDemo {

	public static void main(String[] args) {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
		//后续的第n个任务将在initialDelay+period * n 执行    
		ses.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					System.out.println(System.currentTimeMillis()/1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
		
		//任务执行完成后，下一个任务将在period时间后执行
		ses.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					System.out.println(System.currentTimeMillis()/1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
	}
}
