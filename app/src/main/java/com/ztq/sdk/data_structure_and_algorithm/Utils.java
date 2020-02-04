package com.ztq.sdk.data_structure_and_algorithm;

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

    /**
     * 冒泡排序(交换排序的一种)
     *
     * @param a
     */
    public static void bubbleSort(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        int temp = 0;
        for (int i = 0; i < a.length - 1; i++) {
            boolean flag = false;
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    flag = true;
                }
            }
            String str = "";
            for(int j = 0; j < a.length; j++) {
                str += a[j] + " ";
            }
            str = "冒泡排序第" + (i + 1) + "次子步骤后的：" + str;
            Log.v(TAG, str);
            if (!flag) {
                break;
            }
        }
    }

    /**
     * 快速排序(交换排序的另外一种)
     *
     * @param a
     */
    public static void quickSort(int[] a) {
        if (a == null) {
            return;
        }
        quickSort(a, 0, a.length - 1);
    }

    /**
     * 快速排序用到的方法
     * @param a
     * @param start
     * @param end
     */
    public static void quickSort(int[] a, int start, int end) {
        if (a == null || a.length == 0 || start < 0 || end > a.length - 1) {
            return;
        }
        int pivot = a[start];
        int temp = 0;
        int i = start + 1;
        int j = end;
        while (i < j) {
            while (i < j && a[j] >= pivot) {
                j--;
            }
            while (i < j && a[i] <= pivot) {
                i++;
            }
            Log.v(TAG, "quickSort before, i = " + i + "; j = " + j + "; pivot = " + pivot);
            if (i < j) {
                temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
            String str = "";
            for(int k = 0; k < a.length; k++) {
                str += a[k] + " ";
            }
            Log.v(TAG, "quickSort, i = " + i + "; j = " + j + "; 数组为：" + str);
        }
        if (a[j] < a[start]) {
            temp = a[j];
            a[j] = a[start];
            a[start] = temp;
        }
        if (i - 1 > start) {
            quickSort(a, start, i - 1);
        }
        if (end > j + 1) {
            quickSort(a, j + 1, end);
        }
    }

    /**
     * 简单选择排序(选择排序的一种)
     *
     * @param a
     */
    public static void simpleChooseSort(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        int k = 0;
        int temp = 0;
        for (int i = 0; i < a.length - 1; i++) {
            k = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[k]) {
                    k = j;
                }
            }

            if (k != i) {
                temp = a[i];
                a[i] = a[k];
                a[k] = temp;
            }
        }
    }

    /**
     * 堆排序，这里是最大堆排序(这是选择排序的另外一种)
     */
    public static void heapSort(int[] a) {
        int temp = 0;
        creatMaxHeap(a, 0, a.length - 1);
        for(int i = a.length - 1; i > 0; i--) {
            temp = a[0];
            a[0] = a[i];
            a[i] = temp;
            creatMaxHeap(a, 0, i - 1);
        }
    }

    /**
     * 创建最大堆
     * @param a
     * @param low
     * @param high
     */
    public static void creatMaxHeap(int[] a, int low, int high) {
        if (a == null || a.length == 0 || low < 0 || high > a.length - 1 || low >= high) {
            return;
        }
        int j = 0;
        int k = 0;
        int temp = 0;
        for(int i = high / 2; i >= low; i--) {
            temp = a[i];
            k = i;
            j = 2 * k + 1;
            while(j <= high) {
                if (((j + 1) <= high) && (a[j] < a[j + 1])) {
                    j++;
                }
                if (temp < a[j]) {
                    a[k] = a[j];
                    k = j;
                    j = 2 * k + 1;
                } else {
                    break;
                }
            }
            a[k] = temp;
            Log.v(TAG, "堆排序：");
            showHeap(a);
        }
        String str = "";
        for(int n = 0; n < a.length; n++) {
            str += a[n] + " ";
        }
        Log.v(TAG, "堆排序; 数组为：" + str);
    }

    /**
     * 直接插入排序（插入排序的一种）
     *
     * @param a
     */
    public static void directInsertSort(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        int temp = 0;
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j] < a[j - 1]) {
                    temp = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = temp;
                } else {
                    break;
                }
            }
            String str = "";
            for(int j = 0; j < a.length; j++) {
                str += a[j] + " ";
            }
            str = "直接插入排序第" + i + "次子步骤后的：" + str;
            Log.v(TAG, str);
        }
    }

    /**
     * 希尔排序（插入排序的另外一种）
     *
     * @param a
     */
    public static void shellSort(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        int increment = a.length / 3;
        int temp = 0;
        while (increment >= 1) {
            for (int i = 0; i < increment; i++) {
                for (int j = i + increment; j < a.length; j += increment) {
                    for (int k = j; k >= increment; k -= increment) {
                        if (a[k] < a[k - increment]) {
                            temp = a[k];
                            a[k] = a[k - increment];
                            a[k - increment] = temp;
                        } else {
                            break;
                        }
                    }
                }
            }
            increment /= 2; // 这增量有待于改进，最好的方法是所有的增量都是质数，最后一个增量一定是1
            String str = "";
            for(int j = 0; j < a.length; j++) {
                str += a[j] + " ";
            }
            str = "希尔排序第" + "次子步骤后的：" + str;
            Log.v(TAG, str);
        }
    }

    public static void showHeap(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        String str = "";
        for(int i = 0; i < a.length; i++) {
            str += a[i] + " ";
            if (isPowerOfTwo(i + 2)) {
                Log.v(TAG, "堆排：" + str);
                str = "";
            }
        }
    }

    public static boolean isPowerOfTwo(int n) {
        if (n <= 0) {
            return false;
        }
        int count = 0;
        while (n != 0) {
            if ((n & 1) == 1) {
                count++;
            }
            if (count > 1) {
                return false;
            }
            n = n >> 1;
        }
        return true;
    }

    /**
     * 判断一个整数是否为奇数
     * @param i
     * @return
     */
    public static boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    /**
     * 将十进制数化成二进制字符串
     * @param i
     * @return
     */
    public static String decimalNumToBinary(int i) {
        return Integer.toBinaryString(i);
    }
}