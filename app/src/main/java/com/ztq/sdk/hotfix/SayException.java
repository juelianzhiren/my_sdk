package com.ztq.sdk.hotfix;

public class SayException implements ISay {
    @Override
    public String saySomething() {
        return "something wrong here!";
    }
}