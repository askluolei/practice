package com.luolei.base.thread.pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程非常容易引起幽灵般的错误，如下面的实例，两个数相除，最后发现结果只有4个，从这个for循环看，我们应该得到5个结果，
 * 但是程序算漏了一组，但是更不幸的是，程序没有任何日志，没有任何错误提示信息，
 * 这个案例很容易分析出来，有一组除0了，但是会异常，然而异常被吃掉了
 * 没有异常堆栈，排查问题的时候，也就只能像大海捞针，慢慢琢磨了
 * 
 * 那么怎么向线程池讨回异常堆栈：
 * 1.放弃submit，改用execute
 * 2.使用Future接受返回，get的时候获取堆栈信息
 * 
 * 这里，我们从异常堆栈中可以知道异常是在哪里抛的，但是我们还可能会希望得到任务到底是在哪里提交的，而任务提交的具体位置已经被线程池完全淹没了，顺着堆栈，最多只能找到线程池中的调用
 * @author 罗雷
 * @date 2016年9月13日
 * @time 上午7:19:59
 */
public class ThreadPoolProblem {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ThreadPoolExecutor pools = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
		for (int i = 0; i < 5; i++) {
			pools.submit(new DivTask(100, i));
			//pools.execute(new DivTask(100, i));
			//Future re = pools.submit(new DivTask(100, i));
			//re.get();
		}
	}
}
