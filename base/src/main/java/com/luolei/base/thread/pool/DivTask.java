package com.luolei.base.thread.pool;

public class DivTask implements Runnable{

	private int a,b;
	
	public DivTask(int a, int b) {
		this.a = a;
		this.b = b;
	}
	public void run() {
		double re = a/b;
		System.out.println(re);
	};
}
