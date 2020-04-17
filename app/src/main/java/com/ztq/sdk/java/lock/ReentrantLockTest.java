package com.ztq.sdk.java.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
	private ReentrantLock lock = new ReentrantLock();

	public static void main(String[] args) {
		final ReentrantLockTest l1 = new ReentrantLockTest();

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

	private void printLog() {
		try {
			lock.lock();
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + " is printing " + i);
				Thread.sleep(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}