package com.ztq.sdk.java.gc;

import java.lang.ref.SoftReference;

/**
 * VM args: -Xms200m -Xmx200m
 * @author pc
 *
 */
public class SoftReferenceNormal {
	static class SoftObject{
		byte[] data = new byte[120 * 1024 * 1024];
	}

	public static void main(String[] args) throws InterruptedException{
		SoftReference<SoftObject> cacheRef = new SoftReference<SoftObject>(new SoftObject());
		System.out.println("第一次GC前软引用：" + cacheRef.get());
		System.gc();
		System.out.println("第一次GC后软引用：" + cacheRef.get());
		
		SoftObject newSoft = new SoftObject();
		System.out.println("再次分配120M强引用对象之后，软引用：" + cacheRef.get());
	}
}