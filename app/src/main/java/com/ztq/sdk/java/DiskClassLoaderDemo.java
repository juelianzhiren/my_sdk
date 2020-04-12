package com.ztq.sdk.java;

import java.lang.reflect.Method;

public class DiskClassLoaderDemo {
	public static void main(String[] args) {
		DiskClassLoader loader = new DiskClassLoader("F:/java_class");
		try {
			Class c = loader.findClass("Secret");
			if (c != null) {
				Object obj = c.newInstance();
				Method method = c.getDeclaredMethod("printSecret", null);
				method.invoke(obj, null);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}