package com.luolei.base.thread.join_sleep2;

public class ThreadA extends Thread{
	
	private ThreadB b;
	
	public ThreadA(ThreadB b) {
		super();
		this.b = b;
	}
	
	@Override
	public void run() {
		try {
			synchronized (b) {
				b.start();
				b.join();
				System.out.println("终于到A执行了");
//				for (int i = 0; i < Integer.MAX_VALUE; i++) {
//					String newString = new String();
//					Math.random();
//				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
