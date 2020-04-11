package com.ztq.sdk.java;

public class InitOrder {
	public static void main(String[] args) {
		Parent p = new Child();
		System.out.println("....................................");
		p = new Child();
		System.out.println("....................................");
		Child child = new Child();
	}
}

class Child extends Parent{
	static {
		System.out.println("child static block!");
	}
	
	{
		System.out.println("child non-static block!");
	}
	
	public Child() {
		System.out.println("child constructor!");
	}
}

class Parent {
	static {
		System.out.println("parent static block!");
	}
	
	{
		System.out.println("parent non-static block!");
	}
	
	public Parent() {
		System.out.println("parent constructor!");
	}
}