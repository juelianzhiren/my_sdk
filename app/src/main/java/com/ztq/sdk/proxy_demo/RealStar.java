package com.ztq.sdk.proxy_demo;

public class RealStar implements Subject{
    @Override
    public void advertise() {
        System.out.println("------明星拍广告------");
    }
}