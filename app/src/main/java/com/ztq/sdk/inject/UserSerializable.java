package com.ztq.sdk.inject;


public class UserSerializable implements java.io.Serializable {
    String name;

    public UserSerializable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserSerializable{" +
                "name='" + name + '\'' +
                '}';
    }
}
