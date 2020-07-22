package com.ztq.sdk.proxy_demo;

/**
 * 静态代理类
 */
public class StaticProxy {
	interface HelloInterface {
		void sayHello();
	}

	static class Hello implements HelloInterface {
		@Override
		public void sayHello() {
			System.out.println("Hello zhanghao!");
		}
	}

	static class HelloProxy implements HelloInterface {
		private HelloInterface helloInterface = new Hello();

		@Override
		public void sayHello() {
			System.out.println("Before invoke sayHello");
			helloInterface.sayHello();
			System.out.println("After invoke sayHello");
		}
	}

	public static void main(String[] args) {
		HelloProxy helloProxy = new HelloProxy();
		helloProxy.sayHello();
	}
}