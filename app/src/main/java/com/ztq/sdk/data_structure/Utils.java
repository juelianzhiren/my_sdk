package com.ztq.sdk.data_structure;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: ztq
 * Date: 2020/1/17 10:48
 * Description: 数据结构中的工具类
 */
public class Utils {
    private static final String TAG = "noahedu.Utils";

    /**
     * 获取第index个丑数
     * 丑数概念：我们把只包含因子2、3和5的数称作丑数（Ugly Number）。例如6、8都是丑数，但14不是，因为它包含因子7。
     * 习惯上我们把1当做是第一个丑数。求按从小到大的顺序的第index个丑数。
     * @param index
     * @return
     */
    public static int getUglyNumber(int index) {
        if (index <= 0) {
            return -1;
        }
        int twoCount = 0;
        int threeCount = 0;
        int fiveCount = 0;
        List<Integer> uglyList = new ArrayList<>();
        uglyList.add(1);
        for (int i = 0; i < index - 1; i++) {
            while (true) {
                if (uglyList.get(twoCount) * 2 > uglyList.get(uglyList.size() - 1)) {
                    break;
                }
                twoCount++;
            }
            while (true) {
                if (uglyList.get(threeCount) * 3 > uglyList.get(uglyList.size() - 1)) {
                    break;
                }
                threeCount++;
            }
            while (true) {
                if (uglyList.get(fiveCount) * 5 > uglyList.get(uglyList.size() - 1)) {
                    break;
                }
                fiveCount++;
            }

            int a = Math.min(uglyList.get(twoCount) * 2, uglyList.get(threeCount) * 3);
            int b = Math.min(a, uglyList.get(fiveCount) * 5);
            uglyList.add(b);
        }
        Log.v(TAG, "第" + index + "个丑数为：" + uglyList.get(index - 1));
        return uglyList.get(index - 1);
    }

    /**
     * 第一次只出现一次的字符
     * @param str
     */
    public static String getFirstLetter2(String str){
        HashMap<Character, Integer> hashMap = new HashMap<>();

        for (int i = 0; i < str.length(); i++) {
            if (hashMap.containsKey(str.charAt(i))){
                int value = hashMap.get(str.charAt(i));
                hashMap.put(str.charAt(i), value + 1);
            }else {
                hashMap.put(str.charAt(i), 1);
            }
        }
        String c = "";
        for (int i = 0; i < str.length(); i++) {
            if (hashMap.get(str.charAt(i)) == 1){
                c = String.valueOf(str.charAt(i));
                Log.v(TAG, c);
                break;
            }
        }
        return c;
    }

    /**
     * 获取数组中的逆序对数
     * @param array
     * @return
     */
    public static int getInversePairsNumber(int[] array) {
        if (array == null) {
            return -1;
        }
        // 创建辅助数组
        int length = array.length;
        int[] copy = new int[length];
        System.arraycopy(array, 0, copy, 0, length);
        int numberOfInversePairs = iPairs(array, copy, 0, length - 1);
        return numberOfInversePairs;
    }

    /**
     * @author ztq
     * @param array 未归并数组
     * @param copy 用于存储归并后数据的数组
     * @param begin 起始位置
     * @param end 结束位置
     * @return 逆序数
     */
    private static int iPairs(int[] array, int[] copy, int begin, int end) {
        if(begin == end) {
            return 0;
        }
        int mid = (begin + end) / 2;
        // 递归调用
        int left = iPairs(copy, array, begin, mid);
        int right = iPairs(copy, array, mid + 1, end);
        // 归并
        int i = mid, j = end, pos = end;
        int count = 0; // 记录相邻子数组间逆序数

        while(i >= begin && j >= mid + 1) {
            if(array[i] > array[j]) {
                copy[pos--] = array[i--];
                count += j - mid;
            } else {
                copy[pos--] = array[j--];
            }
        }
        Log.v(TAG, "i = " + i + "; begin = " + begin + "; j = " + j + "; mid + 1 = " + (mid + 1) + "; pos = " + pos);
        while(i >= begin) {
            copy[pos--] = array[i--];
        }
        while(j >= mid + 1) {
            copy[pos--] = array[j--];
        }
        return left + right + count;
    }
}