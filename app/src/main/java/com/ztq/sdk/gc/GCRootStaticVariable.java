package com.ztq.sdk.gc;

/**
 * VM args: -Xms200m -Xmx200m
 * @author pc
 *
 */
public class GCRootStaticVariable {
	private static int _10MB = 10 * 1024 * 1024;
	private byte[] memory;
	private static GCRootStaticVariable staticVariable;
	
	public GCRootStaticVariable(int size) {
		memory = new byte[size];
	}
	
	public static void main(String[] args) {
		System.out.println("程序开始：");
		printMemory();
		GCRootStaticVariable g = new GCRootStaticVariable(4 * _10MB);
		g.staticVariable = new GCRootStaticVariable(8 * _10MB);
		g = null;
		System.gc();
		System.out.println("GC完成");
		printMemory();
	}
	
	private static void printMemory() {
		System.out.println("free is " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M,");
		System.out.println("total is " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M,");
	}
}