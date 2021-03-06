package com.ztq.sdk.java.gc;

/**
 * VM args: -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * @author pc
 *
 */
public class MinorGCTest {
	private static final int _1MB = 1024 * 1024;
	
	public static void testAllocation() {
		byte[] a1, a2, a3, a4;
		a1 = new byte[2 * _1MB];
		a2 = new byte[2 * _1MB];
		a3 = new byte[2 * _1MB];
		a4 = new byte[2 * _1MB];
	}
	
	public static void main(String[] args) {
		testAllocation();
	}
}