package com.ztq.sdk.data_structure_and_algorithm;

public class ViolenceStrMatch {

    public static void main(String[] args) {
        String str1 = "addgadfhggsfhfgs";
        String str2 = "fhf";
        int index = violenceStrMatch(str1, str2);
        System.out.println("index = " + index);
    }

    //暴力匹配算法实现
    public static int violenceStrMatch(String str1, String str2) {
        if (str1.length() < str2.length()) {
            return -1;
        }
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();

        int s1len = s1.length;
        int s2len = s2.length;
        //两个索引
        int i = 0;//指向字符串str1
        int j = 0;//指向字符串str2
        while (i < s1len && j < s2len) {//保证匹配不越界
            if (s1[i] == s2[j]) {
                i++;
                j++;
            } else {
                i = i - j + 1;
                j = 0;
            }
        }
        //判断是否匹配成功
        if (j == s2len) {
            return i - j;
        } else {
            return -1;
        }
    }
}