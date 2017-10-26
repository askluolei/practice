package com.luolei.base.thread.join;

public class ThreadA extends Thread{
	
	private ThreadB b;
	
	public ThreadA(ThreadB b) {
		super();
		this.b = b;
	}
	
	@Override
	public void run() {
		synchronized (b) {
			try {
				System.out.println("begin A ThreadName=" + Thread.currentThread().getName());
				Thread.sleep(5000);
				System.out.println("  end A ThreadName=" + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
