package com.luolei.base.thread.pool;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 分而治之：Fork/Join框架
 * fork后系统多了一个执行分支（线程），所以需要等待这个执行分支执行完毕，才有可能得到最终结果，因此join就表示等待
 * 实际上如果毫无顾忌的使用fork开启线程进行处理，那么很可能导致系统开启过多线程而严重影响性能，所有JDK中给出了一个ForkJoinPool线程池，对于fork并不着急开启线程，而是提交给ForkJoinPool线程池处理，以节省系统资源
 * 由于线程池的优化，提交的任务和线程数量并不是一对一的关系，在绝大多数情况下，一个物理线程时间上需要处理多个逻辑任务。因此每个线程必然需要一个任务队列。当A线程处理完了自己的任务，B线程还有很多任务等着处理，A线程就会从B线程的任务队列中取出底部拿任务执行，这样避免数据竞争
 * @author 罗雷
 * @date 2016年9月13日
 * @time 下午9:45:45
 */
public class CountTask extends RecursiveTask<Long>{

	private static final long serialVersionUID = 1L;
	private static final int THRESHOLD = 1_0000;
	private long start;
	private long end;
	
	public CountTask(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected Long compute() {
		long sum = 0;
		boolean canCompute = (end - start) < THRESHOLD;
		if (canCompute) {
			for (long i = start; i < end; i++) {
				sum += i;
			}
		} else {
			long step = (start + end) / 100;
			ArrayList<CountTask> subTasks = new ArrayList<>();
			long pos = start;
			for (int i = 0;i < 100; i++) {
				long lastOne = pos + step;
				if (lastOne > end)
					lastOne = end;
				CountTask subTask = new CountTask(pos, lastOne);
				pos += step + 1;
				subTasks.add(subTask);
				subTask.fork();
			}
			for (CountTask t: subTasks) {
				sum += t.join();
			}
		}
		return sum;
	}
	
	public static void main(String[] args) {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CountTask task = new CountTask(0, 20_0000L);
		ForkJoinTask<Long> result = forkJoinPool.submit(task);
		try {
			long res = result.get();
			System.out.println("sum=" + res);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}
