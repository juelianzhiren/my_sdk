package com.ztq.sdk.gc;

/**
 * VM args: -Xms200m -Xmx200m
 * @author pc
 *
 */
public class GCRootLocalVariable {
	private int _10MB = 10 * 1024 * 1024;
	private byte[] memory = new byte[8 * _10MB];
	
	public static void main(String[] args) {
		System.out.println("开始时");
		printMemory();
		method();
		System.gc();
		System.out.println("第二次GC完成");
		printMemory();
	}
	
	private static void method() {
		GCRootLocalVariable g = new GCRootLocalVariable();
		System.gc();
		System.out.println("第一次GC完成");
		printMemory();
	}
	
	private static void printMemory() {
		System.out.println("free is " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M,");
		System.out.println("total is " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M,");
	}
}