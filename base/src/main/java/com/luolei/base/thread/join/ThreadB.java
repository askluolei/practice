package com.luolei.base.thread.join;

public class ThreadB extends Thread{

	@Override
	synchronized public void run() {
		try {
			System.out.println("begin B ThreadName=" + Thread.currentThread().getName());
			Thread.sleep(5000);
			System.out.println("  end B ThreadName=" + Thread.currentThread().getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
