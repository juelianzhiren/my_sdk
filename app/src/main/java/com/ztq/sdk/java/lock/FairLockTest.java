package com.ztq.sdk.java.lock;

import java.util.concurrent.locks.ReentrantLock;

public class FairLockTest implements Runnable{
	private int shareNumber = 0;
	private static ReentrantLock lock = new ReentrantLock(true);
	
	@Override
	public void run() {
		while(shareNumber < 20) {
			lock.lock();
			try {
				shareNumber++;
				System.out.println(Thread.currentThread().getName() + " 获取锁，shareNumber is " + shareNumber);
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		FairLockTest test = new FairLockTest();
		Thread t1 = new Thread(test);
		Thread t2 = new Thread(test);
		Thread t3 = new Thread(test);
		t1.start();
		t2.start();
		t3.start();
	}
}