package com.ztq.sdk.proxy_demo;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyMethodInterceptor implements MethodInterceptor {

    private Object target;//需要代理的目标对象

    /**
     * @param obj    代理对象
     * @param method 代理方法
     * @param arr    参数列表
     * @param proxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] arr, MethodProxy proxy) throws Throwable {
        System.out.println("------经纪人谈判------");
        Object invoke = method.invoke(target, arr);
        System.out.println("------经纪人收费------");
        return invoke;
    }

    /**
     * @param objectTarget 目标对象
     * @return
     */
    public Object getCglibProxy(Object objectTarget) {
        //为目标对象target赋值
        this.target = objectTarget;
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(objectTarget.getClass());
        enhancer.setCallback(this);// 设置回调
        Object result = enhancer.create();//创建并返回代理对象
        return result;
    }

    public static void main(String[] args) {
        Subject star = new RealStar();
        CglibProxyMethodInterceptor cglibProxyMethodInterceptor = new CglibProxyMethodInterceptor();
        Subject proxy = (Subject) cglibProxyMethodInterceptor.getCglibProxy(star);
        proxy.advertise();
    }
}