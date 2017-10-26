package com.luolei.base.thread.join_sleep2;

public class ThreadC extends Thread{
	
	private ThreadB threadB;
	
	public ThreadC(ThreadB threadB) {
		super();
		this.threadB = threadB;
	}
	
	@Override
	public void run() {
		threadB.bService();
	}

}
