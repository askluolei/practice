package com.luolei.base.thread.pool;

import java.util.concurrent.Executors;

/**
 * Executors 创建出来的线程池，其内部实现大多都是ThreadPoolExecutor实现的：
 * 
 *  public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
 * 
 * public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
    
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
    
    来看下ThreadPoolExecutor的构造方法，参数最多的，也是最重要的：
    public ThreadPoolExecutor(int corePoolSize,		//指定线程池中线程数量
                              int maximumPoolSize,	//指定线程池中最大线程数量
                              long keepAliveTime,	//当线程池线程数量超过了corePoolSize时候，多余的空闲线程存活的时间
                              TimeUnit unit,		//keepAliveTime的单位
                              BlockingQueue<Runnable> workQueue,	//任务队列，被提交但尚未被执行的任务
                              ThreadFactory threadFactory,			//线程工厂，用于创建线程，一般使用默认的即可
                              RejectedExecutionHandler handler)		//拒绝策略，当任务太多，来不及处理，如果拒绝任务
                              
   以上参数大部分都很简单，只有workQueue和handler需要详细说明下：
   
   workQueue指背提交但未被执行的任务队列，它是一个BlockingQueue接口的对象，仅用于存放Runnable对象，根据队列功能的分类，在ThreadPoolExecutor的构造函数中看使用以下几种BlockingQueue：
   1.直接提交的对象：该功能由SynchronousQueue对象提供,该对象没有容量，每一个插入操作都要等待一个相应的删除操作，反之，每一个删除操作都要等待对应的插入操作，如果使用这个对象，提交的任务不会被真实的保存，而总是将新任务提交给线程执行，
   如果没有空闲线程，则尝试创建新的线程，如果线程数量已经到达最大值，则执行拒绝策略，因此，使用SynchronousQueue，通常要设置很大的maximumPoolSize，否则很容易执行拒绝策略	newCachedThreadPool就是这个队列
   2.有界的任务队列：ArrayBlockingQueue，这个队列的构造函数需要带一个容量参数，代表该队列的最大容量，当使用有界队列时，若有新的任务需要执行，如果线程池中实际线程数小于corePoolSize，则会优先创建新的线程，若大于corePoolSize
 则会将新任务加入等待队列中，若等待队列已满，无法加入，则在总线程数不大于maximumPoolSize的前提下创建新的线程执行任务，若大于maximumPoolSize，则执行拒绝策略，可见，只有当任务队列已满时，才可能将线程数提升到corePoolSize以上。
   3.无界的任务队列：LinkedBlockingQueue：与有界队列相比，除非系统资源耗尽，否则无界的任务队列不存在任务如队失败的情况
   4.优先任务队列：带有执行优先级的队列，PriorityBlockingQueue，可以控制任务执行的先后顺序，它是一个特殊的无界队列。无论是ArrayBlockingQueue还是LinkedBlockingQueue 都是先进先出算法处理任务，而PriorityBlockingQueue
 则可以根据任务自身的优先级顺序先后执行，在确保系统性能的同时，也能有很好的质量保证（总是确保高优先级的任务先执行）
 
 使用自定义线程池时，要根据 应用的具体情况，选择合适的并发队列作为任务的缓冲，当线程资源紧张时，不同的并发队列对系统行为和性能影响均不同。
 
 ThreadPoolExecutor线程池的核心调度逻辑，工作逻辑（execute方法）：
 if(活跃线程 < coreSize) {
 	分配线程执行任务
 } else {
 	提交到等待队列
 	if (提交成功) {
 		等待执行
 	} else {
 		提交线程池
 		if (达到最大线程数) {
 			执行拒绝策略
 		} else {
 			分配线程执行
 		}
 	}
 }
 * @author 罗雷
 * @date 2016年9月11日
 * @time 下午10:41:40
 */
public class ThreadPoolExecutorDemo {

	public static void main(String[] args) {
		Executors.newFixedThreadPool(2);
	}
}
