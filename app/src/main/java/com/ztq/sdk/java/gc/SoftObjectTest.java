package com.ztq.sdk.java.gc;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

/**
 * VM args:-Xms4M -Xmx4M -Xmn2M
 * @author pc
 *
 */
public class SoftObjectTest {
	public static class SoftObject{
		byte[] data = new byte[1024];
	}
	
	private static int CACHE_INITIAL_CAPACITY = 100 * 1024;
	private static Set<SoftReference<SoftObject>> cache = new HashSet<SoftReference<SoftObject>>(CACHE_INITIAL_CAPACITY);
	private static ReferenceQueue<SoftObject> referenceQueue = new ReferenceQueue<SoftObject>();
	
	public static void main(String[] args) {
		for(int i = 0; i < CACHE_INITIAL_CAPACITY; i++) {
			SoftObject obj = new SoftObject();
			cache.add(new SoftReference<SoftObject>(obj, referenceQueue));
			if (i % 10000 == 0) {
				System.out.println("size of cache:" + cache.size());
			}
		}
		System.out.println("End!");
	}
}