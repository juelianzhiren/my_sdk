package com.ztq.sdk.proxy_demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理类
 */
public class DynamicProxy {
	interface Car {
		void running();
	}

	static class Bus implements Car {
		@Override
		public void running() {
			System.out.println("The bus is running.");
		}
	}

	static class Taxi implements Car {
		@Override
		public void running() {
			System.out.println("The taxi is running.");
		}
	}

	static class JDKProxy implements InvocationHandler {
		private Object target; // 代理对象

		public Object getInstance(Object target) {
			this.target = target;
			// 取得代理对象
			return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println("动态代理之前的业务处理。");
			Object result = method.invoke(target, args);
			return result;
		}
	}

	public static void main(String[] args) {
		JDKProxy jdkProxy = new JDKProxy();
		Car car = (Car) jdkProxy.getInstance(new Taxi());
		car.running();
	}
}