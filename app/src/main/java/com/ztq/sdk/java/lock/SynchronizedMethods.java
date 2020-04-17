package com.ztq.sdk.java.lock;

public class SynchronizedMethods {
	private Object obj = new Object();
	
	public static void main(String[] args) {
		final SynchronizedMethods l1 = new SynchronizedMethods();
		final SynchronizedMethods l2 = new SynchronizedMethods();
		
		Thread t1 = new Thread() {
			@Override
			public void run() {
				l1.printLog();
			}
		};
		Thread t2 = new Thread() {
			@Override
			public void run() {
				l1.printLog();
			}
		};
		t1.start();
		t2.start();
	}
	
//	private synchronized void printLog() {
//		try {
//			for(int i = 0; i < 5; i++) {
//				System.out.println(Thread.currentThread().getName() + " is printing " + i);
//				Thread.sleep(300);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private  void printLog() {
		try {
			synchronized(obj) {
				for(int i = 0; i < 5; i++) {
					System.out.println(Thread.currentThread().getName() + " is printing " + i);
					Thread.sleep(300);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	private static synchronized void printLog() {
//		try {
//			for(int i = 0; i < 5; i++) {
//				System.out.println(Thread.currentThread().getName() + " is printing " + i);
//				Thread.sleep(300);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
}