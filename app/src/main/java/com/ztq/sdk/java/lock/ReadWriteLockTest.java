package com.ztq.sdk.java.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	private static String number = "0";

	public static void main(String[] args) {
		Thread t1 = new Thread(new Reader(), "读线程 1 ");
		Thread t2 = new Thread(new Reader(), "读线程 2 ");
		Thread t3 = new Thread(new Writer(), "写线程");
		t1.start();
		t2.start();
		t3.start();
	}

	static class Reader implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i <= 10; i++) {
				lock.readLock().lock();
				System.out.println(Thread.currentThread().getName() + "--->" + number);
				lock.readLock().unlock();
			}
		}
	}

	static class Writer implements Runnable {
		@Override
		public void run() {
			for (int i = 1; i <= 7; i+=2) {
				try {
					lock.writeLock().lock();
					System.out.println(Thread.currentThread().getName() + " 正在写入 " + i);
					number = number.concat("" + i);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.writeLock().unlock();
				}
			}
		}
	}
}