package com.ztq.sdk.data_structure_and_algorithm;

import com.ztq.sdk.log.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Author: ztq
 * Date: 2020/1/17 10:48
 * Description: 数据结构与算法中的工具类
 */
public class Utils {
    private static final String TAG = "noahedu.Utils";

    /**
     * 获取第index个丑数
     * 丑数概念：我们把只包含因子2、3和5的数称作丑数（Ugly Number）。例如6、8都是丑数，但14不是，因为它包含因子7。
     * 习惯上我们把1当做是第一个丑数。求按从小到大的顺序的第index个丑数。
     *
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
     *
     * @param str
     */
    public static String getFirstLetter2(String str) {
        HashMap<Character, Integer> hashMap = new HashMap<>();

        for (int i = 0; i < str.length(); i++) {
            if (hashMap.containsKey(str.charAt(i))) {
                int value = hashMap.get(str.charAt(i));
                hashMap.put(str.charAt(i), value + 1);
            } else {
                hashMap.put(str.charAt(i), 1);
            }
        }
        String c = "";
        for (int i = 0; i < str.length(); i++) {
            if (hashMap.get(str.charAt(i)) == 1) {
                c = String.valueOf(str.charAt(i));
                Log.v(TAG, c);
                break;
            }
        }
        return c;
    }

    /**
     * 获取数组中的逆序对数
     *
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
     * @param array 未归并数组
     * @param copy  用于存储归并后数据的数组
     * @param begin 起始位置
     * @param end   结束位置
     * @return 逆序数
     * @author ztq
     */
    private static int iPairs(int[] array, int[] copy, int begin, int end) {
        if (begin == end) {
            return 0;
        }
        int mid = (begin + end) >> 1;
        // 递归调用
        int left = iPairs(copy, array, begin, mid);
        int right = iPairs(copy, array, mid + 1, end);
        // 归并
        int i = mid, j = end, pos = end;
        int count = 0; // 记录相邻子数组间逆序数

        while (i >= begin && j >= mid + 1) {
            if (array[i] > array[j]) {
                copy[pos--] = array[i--];
                count += j - mid;
            } else {
                copy[pos--] = array[j--];
            }
        }
        Log.v(TAG, "i = " + i + "; begin = " + begin + "; j = " + j + "; mid + 1 = " + (mid + 1) + "; pos = " + pos);
        while (i >= begin) {
            copy[pos--] = array[i--];
        }
        while (j >= mid + 1) {
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
        if (a == null || a.length <= 1) {
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
            for (int j = 0; j < a.length; j++) {
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
        if (a == null || a.length <= 1) {
            return;
        }
        quickSort(a, 0, a.length - 1);
    }

    /**
     * 快速排序用到的方法
     *
     * @param a
     * @param start
     * @param end
     */
    public static void quickSort(int[] a, int start, int end) {
        if (a == null || a.length <= 1 || start < 0 || end > a.length - 1) {
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
            for (int k = 0; k < a.length; k++) {
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
        if (a == null || a.length <= 1) {
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
        for (int i = a.length - 1; i > 0; i--) {
            temp = a[0];
            a[0] = a[i];
            a[i] = temp;
            creatMaxHeap(a, 0, i - 1);
        }
    }

    /**
     * 创建最大堆
     *
     * @param a
     * @param low
     * @param high
     */
    public static void creatMaxHeap(int[] a, int low, int high) {
        if (a == null || a.length <= 1 || low < 0 || high > a.length - 1 || low >= high) {
            return;
        }
        int j = 0;
        int k = 0;
        int temp = 0;
        for (int i = high >> 1; i >= low; i--) {
            temp = a[i];
            k = i;
            j = 2 * k + 1;
            while (j <= high) {
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
        for (int n = 0; n < a.length; n++) {
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
        if (a == null || a.length  <= 1) {
            return;
        }
        int temp = 0;
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j] < a[j - 1]) {   // 可以改进下：使用二分查找，这样效率更快
                    temp = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = temp;
                } else {
                    break;
                }
            }
            String str = "";
            for (int j = 0; j < a.length; j++) {
                str += a[j] + " ";
            }
            str = "直接插入排序第" + i + "次子步骤后的：" + str;
            Log.v(TAG, str);
        }
    }

    /**
     * 直接插入排序的另外一种方式（更优化）
     * @param arr
     */
    public static void directInsertSort1(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            //挖出一个要用来插入的值,同时位置上留下一个可以存新的值的坑
            int x = arr[i];
            int j = i - 1;
            //在前面有一个或连续多个值比x大的时候,一直循环往前面找,将x插入到这串值前面
            while (j >= 0 && arr[j] > x) {
                //当arr[j]比x大的时候,将j向后移一位,正好填到坑中
                arr[j + 1] = arr[j];
                j--;
            }
            //将x插入到最前面
            arr[j + 1] = x;
        }
    }

    /**
     * 希尔排序（插入排序的另外一种）
     *
     * @param a
     */
    public static void shellSort(int[] a) {
        if (a == null || a.length <= 1) {
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
            for (int j = 0; j < a.length; j++) {
                str += a[j] + " ";
            }
            str = "希尔排序第" + "次子步骤后的：" + str;
            Log.v(TAG, str);
        }
    }

    /**
     * 归并排序
     *
     * @param arr
     */
    public static void mergeSort(int[] arr) {
        // 空数组 或 只有一个元素的数组，则什么都不做。
        if (arr == null || arr.length <= 1) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        // 计算出中间值，这种算法保证不会溢出。
        int mid = low + ((high - low) >> 1);
        // 先对左边排序
        mergeSort(arr, low, mid);
        // 先对右边排序
        mergeSort(arr, mid + 1, high);
        // 归并两个有序的子序列
        merge(arr, low, mid, high);
        // 把每一趟排序的结果也输出一下。
        print(arr);
    }

    // 打印数组
    public static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        String str = "";
        for (int i : arr) {
            str += i + " 0";
        }
        Log.v(TAG, str);
    }

    private static void merge(int[] arr, int low, int mid, int high) {
        // temp[]是临时数组，包左不包右，所以要额外 + 1。
        int[] temp = new int[high - low + 1];
        int left = low;         // 左侧指针从low开始。
        int right = mid + 1;    // 右侧指针从mid+1开始。
        int index = 0;          // 此索引用于temp[]
        // 当两个子序列还有元素时，从小到大放入temp[]中。
        while (left <= mid && right <= high) {
            if (arr[left] <= arr[right]) {
                temp[index++] = arr[left++];
            } else {
                temp[index++] = arr[right++];
            }
        }

        // 要么左边没有元素
        while (left <= mid) {
            temp[index++] = arr[left++];
        }

        // 要么右边没有元素
        while (right <= high) {
            temp[index++] = arr[right++];
        }

        // 重新赋值给arr对应的区间。
        for (int i = 0; i < temp.length; i++) {
            arr[low + i] = temp[i];
        }
    }

    public static void showHeap(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }
        String str = "";
        for (int i = 0; i < a.length; i++) {
            str += a[i] + " ";
            if (isPowerOfTwo(i + 2)) {
                Log.v(TAG, "堆排：" + str);
                str = "";
            }
        }
    }

    public static boolean isPowerOfTwo(int n) {    // 2的次幂在二进制中，肯定只有一位为1，其余为0
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
        // 或者 return (n & -n) == n;
    }

    /**
     * 判断一个整数是否为奇数
     *
     * @param i
     * @return
     */
    public static boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    /**
     * 将十进制数化成二进制字符串
     *
     * @param i
     * @return
     */
    public static String decimalNumToBinary(int i) {
        return Integer.toBinaryString(i);
    }

    /**
     * 统计数字k在排序数组中出现的次数
     *
     * @param a
     * @param k
     * @return
     */
    public static int getNumberOfK(int[] a, int k) {
        int count = 0;
        if (a != null && a.length != 0) {
            int firstIndex = getFirstIndexK(a, k, 0, a.length - 1);
            int lastIndex = getLastIndexK(a, k, 0, a.length - 1);
            if (firstIndex != -1) {
                count = lastIndex - firstIndex + 1;
            }
        }
        Log.v(TAG, "count = " + count);
        return count;
    }

    private static int getFirstIndexK(int[] a, int k, int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            return -1;
        }
        int middleIndex = (startIndex + endIndex) >> 1;
        int middleData = a[middleIndex];
        if (middleData == k) {
            if ((middleIndex > 0 && a[middleIndex - 1] != k) || middleIndex == 0) {
                return middleIndex;
            } else {
                endIndex = middleIndex - 1;
            }
        } else if (middleData > k) {
            endIndex = middleIndex - 1;
        } else {
            startIndex = middleIndex + 1;
        }
        return getFirstIndexK(a, k, startIndex, endIndex);
    }

    private static int getLastIndexK(int[] a, int k, int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            return -1;
        }
        int middleIndex = (startIndex + endIndex) >> 1;
        int middleData = a[middleIndex];
        if (middleData == k) {
            if ((middleIndex < a.length - 1 && a[middleIndex + 1] != k) || middleIndex == a.length - 1) {
                return middleIndex;
            } else {
                startIndex = middleIndex + 1;
            }
        } else if (middleData > k) {
            endIndex = middleIndex - 1;
        } else {
            startIndex = middleIndex + 1;
        }
        return getLastIndexK(a, k, startIndex, endIndex);
    }

    /**
     * 0~n-1中缺失的数字
     * 一个长度为n-1的递增排序数组中的所有数字都是唯一的，并且每个数字都在范围0~n-1之内。
     * 在范围0~n-1内的n个数字中有且只有一个数字不在该数组中，请找出这个数字
     *
     * @param a
     * @return
     */
    public static int getMissingNumber(int[] a) {
        if (a == null || a.length == 0) {
            return -1;
        }
        int leftIndex = 0;
        int rightIndex = a.length - 1;
        while (leftIndex <= rightIndex) {
            int middleIndex = (leftIndex + rightIndex) >> 1;
            if (a[middleIndex] != middleIndex) {
                if (middleIndex == 0 || a[middleIndex - 1] == middleIndex - 1) {
                    return middleIndex;
                }
                rightIndex = middleIndex - 1;
            } else {
                leftIndex = middleIndex + 1;
            }
        }
        return -1;
    }

    /**
     * 二叉树的前序遍历
     *
     * @param treeNode
     * @param list     (遍历的值依次放进list列表中)
     */
    public static void preOrderTraverse(BinaryTreeNode<Integer> treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(treeNode.getValue());
        Log.v(TAG, "前序遍历：" + treeNode.getValue());
        BinaryTreeNode leftTree = treeNode.getLeftNode();
        if (leftTree != null) {
            preOrderTraverse(leftTree, list);
        }
        BinaryTreeNode rightTree = treeNode.getRightNode();
        if (rightTree != null) {
            preOrderTraverse(rightTree, list);
        }
    }

    /**
     * 二叉树的中序遍历
     *
     * @param treeNode
     * @param list     (遍历的值依次放进list列表中)
     */
    public static void midOrderTraverse(BinaryTreeNode<Integer> treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        BinaryTreeNode leftTree = treeNode.getLeftNode();
        if (leftTree != null) {
            midOrderTraverse(leftTree, list);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(treeNode.getValue());
        Log.v(TAG, "中序遍历：" + treeNode.getValue());
        BinaryTreeNode rightTree = treeNode.getRightNode();
        if (rightTree != null) {
            midOrderTraverse(rightTree, list);
        }
    }

    /**
     * 二叉树的后序遍历
     *
     * @param treeNode
     * @param list     (遍历的值依次放进list列表中)
     */
    public static void postOrderTraverse(BinaryTreeNode<Integer> treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        BinaryTreeNode leftTree = treeNode.getLeftNode();
        if (leftTree != null) {
            postOrderTraverse(leftTree, list);
        }
        BinaryTreeNode rightTree = treeNode.getRightNode();
        if (rightTree != null) {
            postOrderTraverse(rightTree, list);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(treeNode.getValue());
        Log.v(TAG, "后序遍历：" + treeNode.getValue());
    }

    private static boolean mIsBalanceBinaryTree = true;
    /**
     * 重置mIsBalanceBinaryTree变量值
     */
    public static void resetBalanceBinaryTreeData() {
        mIsBalanceBinaryTree = true;
    }

    /**
     * 获取二叉树的深度
     *
     * @param rootNode
     * @return
     */
    public static int getTreeDepth(BinaryTreeNode<Integer> rootNode) {
        if (rootNode == null) {
            return 0;
        }
        int leftTreeDepth = getTreeDepth(rootNode.getLeftNode());
        int rightTreeDepth = getTreeDepth(rootNode.getRightNode());
        if (Math.abs(leftTreeDepth - rightTreeDepth) > 1) {
            mIsBalanceBinaryTree = false;
        }
        return (leftTreeDepth > rightTreeDepth) ? leftTreeDepth + 1 : rightTreeDepth + 1;
    }

    /**
     * 判断是否为平衡二叉树
     * 平衡二叉树的定义：如果某二叉树中的任意节点的左、右子树的深度相差不超过1，那么它就是一颗平衡二叉树
     *
     * @param rootNode
     * @return
     */
    public static boolean isBalancedBinaryTree(BinaryTreeNode<Integer> rootNode) {
        if (rootNode == null) {
            return true;
        }
        getTreeDepth(rootNode);
        return mIsBalanceBinaryTree;
    }

    /**
     * 寻找数组中只出现一次的两个数字
     * 一个整型数组里除两个数字之外，其它数字都出现了两次，请找出来这两个只出现一次的数字
     *
     * @param array
     * @param list  记录这两个只出现一次的数字
     */
    public static void findTwoNumsAppearOnce(int[] array, List<Integer> list) {
        if (array == null || array.length < 2) {
            return;
        }
        int resultXOR = 0;
        for (int i = 0; i < array.length; i++) {
            resultXOR ^= array[i];
        }
        int indexOf1 = 0;
        while (((resultXOR & 1) == 0) && (indexOf1 <= 4 * 8)) {
            resultXOR = resultXOR >> 1;  //只有n>>1不完整，要n=n>>1
            indexOf1++;
        }
        int num1 = 0;
        int num2 = 0;
        for (int i = 0; i < array.length; i++) {
            if (((array[i] >> indexOf1) & 1) == 1) {
                num1 ^= array[i];
            } else {
                num2 ^= array[i];
            }
        }
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        list.add(num1);
        list.add(num2);
        Log.v(TAG, "num1 = " + num1 + "; num2 = " + num2);
    }

    /**
     * 数组中唯一只出现一次的数字
     * 在一个数组中除一个数字只出现一次之外，其它数字都出现了三次，请找出那个只出现一次的数字
     *
     * @param arr
     * @return
     */
    public static int findNumberAppearingOnce(int[] arr) {
        if (arr == null || arr.length <= 0) {
            throw new RuntimeException();
        }
        int[] bitSum = new int[32];
        for (int i = 0; i < 32; i++) {
            bitSum[i] = 0;
        }
        for (int i = 0; i < arr.length; i++) {
            int bitMask = 1;
            for (int j = 31; j >= 0; j--) {
                int bit = arr[i] & bitMask; // 注意arr[i]&bitMask不一定等于1或者0，有可能等于00010000
                if (bit != 0) {
                    bitSum[j] += 1;
                }
                bitMask = bitMask << 1;
            }
        }
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result = result << 1;

            result += (bitSum[i] % 3);
            // result=result<<1; //不能放在后面，否则最前面一位就没了
        }
        return result;
    }

    /**
     * 和为s的两个数字
     * 输入一个递增排序的数组和一个数字s，在数组中查找两个数，使得它们的和正好是s。
     * 如果有多对数字的和等于s，输出任意一对即可。
     * @param array
     * @param sum
     * @return
     */
    public static ArrayList<Integer> findNumbersWithSum(int[] array, int sum) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (array == null || array.length <= 0) {
            return list;
        }
        int low = 0;
        int high = array.length - 1;
        while (low < high) {
            if (array[low] + array[high] == sum) {
                list.add(array[low]);
                list.add(array[high]);
                break;
            } else if (array[low] + array[high] < sum) {
                low++;
            } else {
                high--;
            }
        }
        return list;
    }

    /**
     * 和为sum的连续正数序列
     * 输入一个正数s，打印出所有和为s的连续正数序列（至少含有两个数）。
     * 例如输入15，由于1+2+3+4+5=4+5+6=7+8=15，所以结果打印出3个连续序列1～5、4～6和7～8
     * @param sum
     * @return
     */
    public static ArrayList<ArrayList<Integer>> findContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> sequenceList = new ArrayList<ArrayList<Integer>>();
        if (sum <= 0) {
            return sequenceList;
        }
        int small = 1;
        int big = 2;
        int curSum = small + big;
        while (small <= sum >> 1) {
            if (curSum == sum) {
                ArrayList<Integer> sequence = new ArrayList<Integer>();
                for (int i = small; i <= big; i++) {
                    sequence.add(i);
                }
                sequenceList.add(sequence);
                curSum -= small;
                small++; // 这两行位置先后要注意
            }
            if (curSum < sum) {
                big++;
                curSum += big;
            }
            if (curSum > sum) {
                curSum -= small;
                small++;
            }
        }
//        for(int i = 0; i < sequenceList.size(); i++) {
//            List list = sequenceList.get(i);
//            for(int j = 0; j < list.size(); j++) {
//                System.out.print(list.get(j) + " ");
//            }
//            System.out.println();
////        }
        return sequenceList;
    }

    /**
     * 获取圆圈中最后剩下的数字
     * 题目：0,1,2,...,n-1这n个数字排成一个圆圈，从数字0开始，每次从这个圆圈里删除第m个数字
     * 求出这个圆圈里剩下的最后一个数字
     *
     * @param n
     * @param m
     * @return
     */
    public static int getLastRemaining(int n, int m) {
        if (n < 1 || m < 1) {
            return -1;
        }
        int last = 0;
        for (int i = 2; i <= n; i++) {
            last = (last + m) % i;
        }
        Log.v(TAG, "last = " + last);
        return last;
    }

    /**
     * 题目：在一个长度为n的数组里的所有数字都在0到n-1的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，
     * 也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。例如，如果输入长度为7的数组{2, 3, 1, 0, 2, 5, 3}，
     * 那么对应的输出是重复的数字2和3。
     */
    public static List<Integer> getDuplicate(int[] arr) {
        if (arr == null || arr.length <= 0) {
            Log.v(TAG, "数组输入无效！");
            return null;
        }
        for (int a : arr) {
            if (a < 0 || a > arr.length - 1) {
                Log.v(TAG, "数字大小超出范围！");
                return null;
            }
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            int temp;
            while (arr[i] != i) {
                if (arr[arr[i]] == arr[i]) {
                    list.add(arr[i]);
                    break;
                }
                // 交换arr[arr[i]]和arr[i]
                temp = arr[i];
                arr[i] = arr[temp];
                arr[temp] = temp;
            }
        }
        if (list.size() != 0) {
            String str = "";
            for (int i = 0; i < list.size(); i++) {
                str += list.get(i) + " ";
            }
            Log.v(TAG, "重複的數字為：" + str);
        }
        return list;
    }

    /**
     * @Description 二维数组中的查找
     * 题目：在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按
     * 照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个
     * 整数，判断数组中是否含有该整数。
     * @author ztq
     * @date 2018年7月16日 下午2:20:59
     */
    public static int[] find(int[][] matrix, int a) {
        int[] index = {-1, -1};

        // 判断数组是否正确
        if (matrix == null || matrix.length <= 0) {
            Log.v(TAG, "数组无效！");
            return index;
        }
        // 判断数组数字的大小是否符合大小规则
        int columns = matrix[0].length;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length != columns) {
                Log.v(TAG, "数组列数不一致！");
                return index;
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == 0 && j == 0)
                    // matrix[0][0]不比较
                    break;
                if (i == 0) { // 第一行的数，仅和前一列的数比较
                    if (matrix[i][j] < matrix[i][j - 1]) {
                        Log.v(TAG, "数组中数字大小不符合要求！");
                        return index;
                    }
                } else if (j == 0) { // 第一列的数，仅和前一行的数比较
                    if (matrix[i][j] < matrix[i - 1][j]) {
                        Log.v(TAG, "数组中数字大小不符合要求！");
                        return index;
                    }
                } else if (matrix[i][j] < matrix[i - 1][j] || matrix[i][j] < matrix[i][j - 1]) {
                    // 其余位置的数字，和前一行或前一列的比较
                    Log.v(TAG, "数组中数字大小不符合要求！");
                    return index;
                }
            }
        }

        // 正式查找
        int row = 0; // 行数
        int column = matrix[0].length - 1; // 列数
        while (row <= matrix.length - 1 && column >= 0) {
            if (a == matrix[row][column]) {
                index[0] = row;
                index[1] = column;
                Log.v(TAG, "数字" + a + "在二维数组中的下标为：" + index[0] + "," + index[1]); // 注意下标是从0开始的
                return index;
            } else if (a < matrix[row][column]) {
                column--;
            } else {
                row++;
            }
        }
        Log.v(TAG, "数组中不含数字：" + a);
        return index;
    }

    /**
     * 实现空格的替换
     * 题目：请实现一个函数，把字符串中的每个空格替换成"%20"。例如输入“We are happy.”，
     * 则输出“We%20are%20happy.”。
     */
    public static String replaceSpace(StringBuffer str) {
        if (str == null) {
            System.out.println("输入错误！");
            return null;
        }
        int length = str.length();
        int indexOfOriginal = length - 1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ')
                length += 2;
        }
        str.setLength(length);
        int indexOfNew = length - 1;
        while (indexOfNew > indexOfOriginal) {
            if (str.charAt(indexOfOriginal) != ' ') {
                str.setCharAt(indexOfNew--, str.charAt(indexOfOriginal));
            } else {
                str.setCharAt(indexOfNew--, '0');
                str.setCharAt(indexOfNew--, '2');
                str.setCharAt(indexOfNew--, '%');
            }
            indexOfOriginal--;
        }
        return str.toString();
    }

    public static BinaryTreeNode<Integer> reConstructBinaryTree(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length <= 0 || in.length <= 0 || pre.length != in.length) {
            throw new RuntimeException("数组不符合规范！");
        }
        return construct(pre, in, 0, pre.length - 1, 0, in.length - 1);
    }

    /**
     * @Description 由前序遍历序列和中序遍历序列得到根结点
     * pre、in：始终用最初的前序遍历和中序遍历数组代入
     * pStart、pEnd：当前树的前序数组开始和结束位置
     * iStart、iEnd：中序数组开始和结束位置
     */
    public static BinaryTreeNode<Integer> construct(int[] pre, int[] in, int preStart, int preEnd, int inStart, int inEnd) {
        BinaryTreeNode<Integer> root = new BinaryTreeNode<Integer>(pre[preStart]);
        if (preStart == preEnd && inStart == inEnd) {
            if (pre[preStart] != in[inStart])
                throw new RuntimeException("数组不符合规范！");
            return root;
        }
        int index = inStart; // 用于记录中序遍历序列中根结点的位置
        while (root.getValue() != in[index] && index <= inEnd) {
            index++;
        }
        if (index > inEnd)
            throw new RuntimeException("数组不符合规范！");
        int leftLength = index - inStart;
        if (leftLength > 0) {
            root.setLeftNode(construct(pre, in, preStart + 1, preStart + leftLength, inStart, index - 1));
        }
        if (leftLength < inEnd - inStart) {
            root.setRightNode(construct(pre, in, preStart + leftLength + 1, preEnd, index + 1, inEnd));
        }
        return root;
    }

    /**
     * 题目：给定一棵二叉树和其中的一个结点，如何找出中序遍历顺序的下一个结点？
     * 树中的结点除了有两个分别指向左右子结点的指针以外，还有一个指向父结点的指针。
     *
     * @param pNode
     * @return
     */
    public static BinaryTreeNodeWithParentPointer<Integer> getNextNode(BinaryTreeNodeWithParentPointer<Integer> pNode) {
        if (pNode == null) {
            Log.v(TAG, "结点为null ");
            return null;
        }
        if (pNode.getRightNode() != null) {
            pNode = pNode.getRightNode();
            while (pNode.getLeftNode() != null) {
                pNode = pNode.getLeftNode();
            }
            return pNode;
        }
        while (pNode.getParentNode() != null) {
            if (pNode == pNode.getParentNode().getLeftNode()) {
                return pNode.getParentNode();
            }
            pNode = pNode.getParentNode();
        }
        return null;
    }

    /**
     * 题目：用两个栈实现一个队列类。队列的声明如下，请实现它的两个函数push
     * 和pop，分别完成在队列尾部插入结点和在队列头部删除结点的功能。
     */
    static class QueueByTwoStacks<E> {
        Stack<E> stack1 = new Stack<E>();
        Stack<E> stack2 = new Stack<E>();

        /**
         * 插入结点
         */
        public void push(E node) {
            stack1.push(node);
        }

        /**
         * 删除结点
         */
        public E pop() {
            if (stack2.empty()) {
                if (stack1.empty()) {
                    throw new RuntimeException("队列为空！");
                } else {
                    while (!stack1.empty()) {
                        stack2.push(stack1.pop());
                    }
                }
            }
            return stack2.pop();
        }
    }

    /**
     * 两个队列实现一个栈
     * 一个队列加入元素，弹出元素时，需要把队列中的元素放到另外一个队列中，删除最后一个元素
     * 两个队列始终保持只有一个队列是有数据的
     */
    static class StackByTwoQueues<T> {
        private Queue<T> queue1 = new LinkedList<T>();
        private Queue<T> queue2 = new LinkedList<T>();

        /**
         * 压栈
         * <p>
         * 入栈非空的队列
         */
        public boolean push(T t) {
            if (!queue1.isEmpty()) {
                return queue1.offer(t);
            } else {
                return queue2.offer(t);
            }
        }

        /**
         * 弹出并删除元素
         */
        public T pop() {
            if (queue1.isEmpty() && queue2.isEmpty()) {
                throw new RuntimeException("queue is empty");
            }
            if (!queue1.isEmpty() && queue2.isEmpty()) {
                while (queue1.size() > 1) {
                    queue2.offer(queue1.poll());
                }
                return queue1.poll();
            }
            if (queue1.isEmpty() && !queue2.isEmpty()) {
                while (queue2.size() > 1) {
                    queue1.offer(queue2.poll());
                }
                return queue2.poll();
            }
            return null;
        }

        @Override
        public String toString() {
            return this.queue1.toString() + ", " + this.queue2.toString();
        }
    }

    /**
     * 对公司所有员工的年龄排序，公司总共有几万名员工
     */
    public static void sortAge(int[] ages) {
        if (ages == null || ages.length < 1) {
            return;
        }
        int oldAge = 80;
        int youngAge = 20;

        // 初始化一个odlAge+1的数组
        int[] timeOfAge = new int[oldAge + 1];
        // 将数组元素都置为0
        for (int i = 0; i < timeOfAge.length; i++) {
            timeOfAge[i] = 0;
        }
        // 某个年龄出现了多少次，就在timeOfAge数组对应年龄的位置设置多少次
        for (int j = 0; j < ages.length; j++) {
            int a = ages[j];
            timeOfAge[a]++;
        }

        int index = 0;
        for (int i = youngAge; i <= oldAge; i++) {     // 按照年龄从小到大依次遍历timeOfAge
            for (int j = 0; j < timeOfAge[i]; j++) {   // 在timeOfAge中取得各个年龄位置记录的出现次数
                ages[index] = i;                       // 将新数组从头设置出现的年龄，已经排好序
                index++;
            }
        }
    }

    /**
     * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。
     * 例如数组{3, 4, 5, 1, 2}为{1, 2, 3, 4, 5}的一个旋转，该数组的最小值为1
     */
    public static int minNumberInRotateArray(int[] array) {
        if (array == null || array.length <= 0) { // 空数组或null时返回0
            return 0;
        }
        int low = 0;
        int high = array.length - 1;
        int mid = (low + high) >> 1;
        //升序数组
        if (array[low] < array[high]) {
            return array[low];
        }
        //中间数字与首尾数字相等
        if (array[mid] == array[high] && array[mid] == array[low]) {
            for (int i = 1; i <= high; i++) {
                if (array[i] < array[i - 1]) {
                    return array[i];
                }
            }
            return array[low];
        }
        //正常情况
        while (low < high) {
            if (high - low == 1) {
                break;
            }
            mid = (low + high) >> 1;
            if (array[mid] <= array[high]) {
                high = mid;
            }
            if (array[mid] > array[high]) {
                low = mid;
            }
        }
        return array[high];    // 别错写成了return high; !!
    }

    /**
     * @Description 矩阵中的路径
     * @author ztq
     * @date 2018年9月16日 上午11:13:48
     * // 题目：请设计一个函数，用来判断在一个矩阵中是否存在一条包含某字符串所有
     * // 字符的路径。路径可以从矩阵中任意一格开始，每一步可以在矩阵中向左、右、
     * // 上、下移动一格。如果一条路径经过了矩阵的某一格，那么该路径不能再次进入
     * // 该格子。例如在下面的3×4的矩阵中包含一条字符串“bfce”的路径（路径中的字
     * // 母用下划线标出）。但矩阵中不包含字符串“abfb”的路径，因为字符串的第一个
     * // 字符b占据了矩阵中的第一行第二个格子之后，路径不能再次进入这个格子。
     * // A B T G
     * // C F C S
     * // J D E H
     */
    public static boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        char[][] map = new char[rows][cols];
        boolean[][] tag = new boolean[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                map[i][j] = matrix[index++];
                tag[i][j] = false;
            }
        }

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j] == str[0]) {
                    tag[i][j] = true;
                    if (hasPathCore(map, i, j, str, 1, tag)) {
                        return true;
                    }
                    tag[i][j] = false;
                }
            }
        }
        return false;
    }

    private static boolean hasPathCore(char[][] map, int x, int y, char[] str, int index, boolean[][] tag) {
        if (str.length == index) {
            return true;
        }
        int[] dx = {1, -1, 0, 0};    // 向右、向左、向下、向上方向
        int[] dy = {0, 0, 1, -1};
        for (int i = 0; i < 4; ++i) {
            int targetX = x + dx[i];
            int targetY = y + dy[i];
            if (targetX < 0 || targetY < 0 || targetX >= map.length || targetY >= map[x].length) {
                continue;
            }
            if (tag[targetX][targetY] == false && map[targetX][targetY] == str[index]) {
                tag[targetX][targetY] = true;
                if (hasPathCore(map, targetX, targetY, str, index + 1, tag)) {
                    return true;
                }
                tag[targetX][targetY] = false;
            }
        }
        return false;
    }

    /**
     * @Description 机器人的运动范围
     * @author ztq
     * // 题目：地上有一个m行n列的方格。一个机器人从坐标(0, 0)的格子开始移动，它
     * // 每一次可以向左、右、上、下移动一格，不能进入已经经过的格子，但不能进入行坐标和列坐标的数位之和
     * // 大于k的格子。例如，当k为18时，机器人能够进入方格(35, 37)，因为3+5+3+7=18。
     * // 但它不能进入方格(35, 38)，因为3+5+3+8=19。请问该机器人能够到达多少个格子？
     */
    public static int movingCount(int threshold, int rows, int cols) {
        if (rows <= 0 || cols <= 0 || threshold < 0) {
            return 0;
        }
        boolean[] isVisited = new boolean[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            isVisited[i] = false;
        }
        int count = movingCountCore(threshold, rows, cols, 0, 0, isVisited);// 用两种方法试一下
        return count;
    }

    private static int movingCountCore(int threshold, int rows, int cols, int row, int col, boolean[] isVisited) {
        if (row < 0 || col < 0 || row >= rows || col >= cols || isVisited[row * cols + col] || cal(row) + cal(col) > threshold) {
            return 0;
        }
        isVisited[row * cols + col] = true;
        return 1 + movingCountCore(threshold, rows, cols, row - 1, col, isVisited)
                + movingCountCore(threshold, rows, cols, row + 1, col, isVisited)
                + movingCountCore(threshold, rows, cols, row, col - 1, isVisited)
                + movingCountCore(threshold, rows, cols, row, col + 1, isVisited);
    }

    private static int cal(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    /**
     * @Description 剪绳子
     * @author ztq
     * @date 2018年9月17日 上午9:37:41
     */
// 题目：给你一根长度为n绳子，请把绳子剪成m段（m、n都是整数，n>1并且m≥1）。
// 每段的绳子的长度记为k[0]、k[1]、……、k[m]。k[0]*k[1]*…*k[m]可能的最大乘
// 积是多少？例如当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此
// 时得到最大的乘积18。
    // ======动态规划======
    public static int maxProductAfterCutting_solution1(int length) {
        if (length <= 1) {
            return 0;
        }
        if (length == 2) {
            return 1;
        }
        if (length == 3) {
            return 2;
        }
        int[] product = new int[length + 1]; // 用于存放最大乘积值
        // 下面几个不是乘积，因为其本身长度比乘积大
        product[0] = 0;
        product[1] = 1;
        product[2] = 2;
        product[3] = 3;

        // 开始从下到上计算长度为i绳子的最大乘积值product[i]
        for (int i = 4; i <= length; i++) {
            int max = 0;
            // 算不同子长度的乘积，找出最大的乘积
            for (int j = 1; j <= i >> 1; j++) {
                if (max < product[j] * product[i - j]) {
                    max = product[j] * product[i - j];
                }
            }
            product[i] = max;
        }
        return product[length];
    }

    // =======贪婪算法========
    public static int maxProductAfterCutting_solution2(int length) {
        if (length <= 1) {
            return 0;
        }
        if (length == 2) {
            return 1;
        }
        if (length == 3) {
            return 2;
        }
        int timesOf3 = length / 3;
        int timesOf2 = 0;
        if (length - timesOf3 * 3 == 1) {
            timesOf3--;
        }
        timesOf2 = (length - timesOf3 * 3) >> 1;
        return (int) (Math.pow(3, timesOf3) * Math.pow(2, timesOf2));
    }

    /**
     * @Description 二进制中1的个数
     * @author ztq
     * // 题目：请实现一个函数，输入一个整数，输出该数二进制表示中1的个数。例如
     * // 把9表示成二进制是1001，有2位是1。因此如果输入9，该函数输出2。
     */
    public static int numberOf1_Solution1(int n) {
        int count = 0;
        int flag = 1;
        while (flag != 0) {
            if ((flag & n) != 0)
                count++;
            flag = flag << 1;
        }
        return count;
    }

    public static int numberOf1_Solution2(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n = (n - 1) & n;
        }
        return count;
    }

    private static boolean mIsInvalid = false;//用全局变量标记是否出错

    /**
     * 数值的整数次方
     * 题目：实现函数double Power(double base, int exponent)，求base的exponent
     * 次方。不得使用库函数，同时不需要考虑大数问题。
     *
     * @param base
     * @param exponent
     * @return
     */
    public static double power(double base, int exponent) {
        mIsInvalid = false;
        double result; // double类型
        if (exponent > 0) {
            result = powerCore(base, exponent);
        } else if (exponent < 0) {
            if (base == 0) {
                mIsInvalid = true; //0的负数次方不存在
                return 0;
            }
            result = 1 / powerCore(base, -exponent);
        } else {
            return 1; //这里0的0次方输出为1
        }
        return result;
    }

    private static double powerCore(double base, int exponent) {
        if (exponent == 1) {
            return base;
        }
        if (exponent == 0) {
            return 1;
        }
        double result = powerCore(base, exponent >> 1);
        result *= result;
        if ((exponent & 0x1) == 1) {
            result *= base;
        }
        return result;
    }

    //=========方法二============

    /**
     * 打印从1到最大的n位数
     * 采用递归的方法
     */
    public static void print1ToMaxOfNDigits2(int n) {
        if (n <= 0) {
            return;
        }
        char[] number = new char[n];
        for (int k = 0; k < number.length; k++) {
            number[k] = '0';
        }
        System.out.println(number);
        for (int i = 0; i <= 9; i++) {
            makeNumber(n, number, i, 0);
        }
    }

    /**
     * 生成数字
     */
    private static void makeNumber(int n, char[] number, int nNumber, int index) {
        if (index == number.length - 1) {
            number[index] = (char) (nNumber + '0');
            printCharNumber2(number); // 打印数字代码与第一个方法一样
            return;
        } else {
            number[index] = (char) (nNumber + '0');
            for (int i = 0; i <= 9; i++) {
                makeNumber(n, number, i, index + 1);
            }
        }
    }

    /**
     * 打印字符数组形成的数字
     * 自己的方法：找出第一个非零字符位置，往后进行打印
     */
    private static void printCharNumber2(char[] number) {
        int beginner = number.length; // 不写成number.length-1，以防写出0
        for (int i = 0; i <= number.length - 1; i++) {
            if ((number[i] - '0') != 0) {
                beginner = i;
                break;
            }
        }
        for (int i = beginner; i <= number.length - 1; i++) {
            System.out.print(number[i]);
        }
        if (beginner != number.length) { // 数字为0时，换行符不输出
            System.out.println();
        }
    }

    /**
     * 在O(1)时间删除链表结点
     * 本题存在缺陷，要求O(1)时间，则无法确定待删除结点的确在表中
     *
     * @param head
     * @param pToBeDeleted
     */
    public static ListNode deleteNode(ListNode head, ListNode pToBeDeleted) {
        if (head == null || pToBeDeleted == null) {
            return head;
        }
        //待删除结点不是尾结点
        if (pToBeDeleted.next != null) {
            ListNode nextNode = pToBeDeleted.next;
            pToBeDeleted.value = nextNode.value;
            pToBeDeleted.next = nextNode.next;
            nextNode = null;
            //只有一个结点（即是尾结点，又是头结点）
        } else if (head == pToBeDeleted) {
            pToBeDeleted = null;
            head = null;
            //链表含多个结点，删除尾结点
        } else {
            ListNode preNode = head;
            while (preNode.next != pToBeDeleted && preNode != null) {
                preNode = preNode.next;
            }
            if (preNode == null) {
                System.out.println("无法找到待删除结点！");
                return head;
            }
            preNode.next = null;
            pToBeDeleted = null;
        }
        return head;
    }

    /**
     * 调整数组顺序使奇数位于偶数前面
     * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有
     * 奇数位于数组的前半部分，所有偶数位于数组的后半部分。
     *
     * @param array
     */
    public static void reOrderArray(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        int length = array.length;
        int low = 0;
        int high = length - 1;
        int temp;
        while (low < high) {
            //向后移动low指针，直到它指向偶数
            while (low < length && (array[low] & 1) != 0) {
                low++;
            }
            //向前移动high指针，直到它指向奇数
            while (high >= 0 && (array[high] & 1) == 0) {
                high--;
            }
            if (low < high) {
                temp = array[low];
                array[low] = array[high];
                array[high] = temp;
            }
        }
    }

    /**
     * 寻找链表中倒数第k个结点
     *
     * @param head
     * @param k
     * @return
     */
    public static ListNode findKthToTail(ListNode head, int k) {
        if (head == null || k <= 0) {
            return null;
        }
        ListNode pAhead = head;
        for (int i = 1; i < k; i++) {
            pAhead = pAhead.next;
            if (pAhead == null) {
                return null;
            }
        }
        ListNode pBehind = head;
        while (pAhead.next != null) {
            pAhead = pAhead.next;
            pBehind = pBehind.next;
        }
        return pBehind;
    }

    /**
     * 链表中环的入口结点
     * 一个链表中包含环，如何找出环的入口结点
     * 计算环中入口结点
     */
    public static ListNode entryNodeOfLoop(ListNode head) {
        ListNode meetingNode = meetingNode(head);
        if (meetingNode == null) {
            return null;
        }

        //计算环中结点的数目
        int count = 1;  //环中结点的数目
        ListNode pNode1 = meetingNode.next;
        while (pNode1 != meetingNode) {
            count++;
            pNode1 = pNode1.next;
        }

        //先移动pNode1，次数为count
        pNode1 = head;
        for (int i = 1; i <= count; i++) {
            pNode1 = pNode1.next;
        }
        ListNode pNode2 = head;
        while (pNode1 != pNode2) {
            pNode1 = pNode1.next;
            pNode2 = pNode2.next;
        }
        return pNode1;
    }

    /**
     * 确定链表是否有环，采用快慢指针确定
     * 返回值代表快慢指针相遇时的结点，返回null代表链表无环
     */
    private static ListNode meetingNode(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode pSlow = head;
        ListNode pFast = head;
        while (pFast != null) {
            pSlow = pSlow.next;
            pFast = pFast.next;
            if (pFast != null) {
                pFast = pFast.next;
            }
            if (pSlow != null && pSlow == pFast) {
                return pSlow;
            }
        }
        return null;
    }

    /**
     * 输入一个链表的头结点，反转该链表并输出反转后链表的头结点。
     *
     * @param node
     * @return
     */
    public static ListNode reverseList(ListNode node) {
        return reverseList(node, null);
    }

    public static ListNode reverseList(ListNode node, ListNode targetNode) {
        ListNode pre = null;
        ListNode next = null;
        while (node != targetNode) {
            next = node.next;
            node.next = pre;
            pre = node;
            node = next;
        }
        return pre;
    }

    /**
     * 输入两个递增排序的链表，合并这两个链表并使新链表中的结点仍然是按照递增排序的。
     * 递归版本
     * @param list1
     * @param list2
     */
    public static ListNode<Integer> merge(ListNode<Integer> list1, ListNode<Integer> list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        if (list1.value < list2.value) {
            list1.next = merge(list1.next, list2);
            return list1;
        } else {
            list2.next = merge(list1, list2.next);
            return list2;
        }
    }

    /**
     * 输入两棵二叉树A和B，判断B是不是A的子结构。
     * @param root1
     * @param root2
     * @return
     */
    public boolean hasSubtree(BinaryTreeNode<Double> root1, BinaryTreeNode<Double> root2) {
        if (root1 == null || root2 == null) {
            return false;
        }
//        boolean result=false;
//        if(equal(root1.val, root2.val)) {
//          result = doesTree1HasTree2(root1, root2);
//          if(!result)
//              result=hasSubtree(root1.left, root2)
//              ||hasSubtree(root1.right, root2);
//        }
//        return result;
        //上面几行可以直接写成：
        return doesTree1HasTree2(root1, root2) || hasSubtree(root1.getLeftNode(), root2) || hasSubtree(root1.getRightNode(), root2);
    }

    /**
     * 判断root结点开始的子树中各个结点是否相同
     */
    private boolean doesTree1HasTree2(BinaryTreeNode<Double> root1, BinaryTreeNode<Double> root2) {
        if (root2 == null) {
            return true;
        }
        if (root1 == null) {
            return false;
        }
        return equal(root1.getValue(), root2.getValue()) && doesTree1HasTree2(root1.getLeftNode(), root2.getLeftNode()) && doesTree1HasTree2(root1.getRightNode(), root2.getRightNode());
    }

    /**
     * 判断两个浮点数是否相等
     */
    private boolean equal(double num1, double num2) {
        if (Math.abs(num1 - num2) < 0.0000001) {
            return true;
        }
        return false;
    }

    /**
     * 输入一个二叉树，该函数输出它的镜像。
     * @param root
     */
    public static void mirror(BinaryTreeNode root) {
        if (root == null) {
            return;
        }
        //左右子结点交换
        BinaryTreeNode tempNode = root.getLeftNode();
        root.setLeftNode(root.getRightNode());
        root.setRightNode(tempNode);

        mirror(root.getLeftNode());
        mirror(root.getRightNode());
    }

    /**
     * 请实现一个函数，用来判断一棵二叉树是不是对称的。如果一棵二叉树和
     * 它的镜像一样，那么它是对称的。
     * @param pRoot
     * @return
     */
    public static boolean isBinaryTreeSymmetrical(BinaryTreeNode pRoot) {
        if (pRoot == null) {
            return true;      //根结点为null时，认为是对称二叉树
        }
        return isEqual(pRoot.getLeftNode(), pRoot.getRightNode());
    }

    private static boolean isEqual(BinaryTreeNode pRoot1, BinaryTreeNode pRoot2) {
        if (pRoot1 == null && pRoot2 == null) {
            return true;
        }
        if (pRoot1 == null || pRoot2 == null) {
            return false;
        }
        return pRoot1.getValue() == pRoot2.getValue() && isEqual(pRoot1.getLeftNode(), pRoot2.getRightNode()) && isEqual(pRoot1.getRightNode(), pRoot2.getLeftNode());
    }

    /**
     * 输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字。
     * @param matrix
     * @param list，记录打印的值
     */
    public static void printMatrix(int[][] matrix, List<Integer> list) {
        if (matrix == null || matrix.length <= 0) {
            return;
        }
        printMatrixInCircle(matrix, 0, list);
    }

    private static void printMatrixInCircle(int[][] matrix, int start, List<Integer> list) {
        int row = matrix.length;
        int col = matrix[0].length;
        int endX = col - 1 - start;
        int endY = row - 1 - start;
        if (endX < start || endY < start) {
            return;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        //仅一行
        if (endY == start) {
            for (int i = start; i <= endX; i++) {
                list.add(matrix[start][i]);
                System.out.print(matrix[start][i] + " ");
            }
            return;  //记得结束
        }
        //仅一列
        if (endX == start) {
            for (int i = start; i <= endY; i++) {
                list.add(matrix[i][start]);
                System.out.print(matrix[i][start] + " ");
            }
            return;  //记得结束
        }

        //打印边界
        for (int i = start; i <= endX; i++) {
            list.add(matrix[start][i]);
            System.out.print(matrix[start][i] + " ");
        }
        for (int i = start + 1; i <= endY; i++) {
            list.add(matrix[i][endX]);
            System.out.print(matrix[i][endX] + " ");
        }
        for (int i = endX - 1; i >= start; i--) {
            list.add(matrix[endY][i]);
            System.out.print(matrix[endY][i] + " ");
        }
        for (int i = endY - 1; i >= start + 1; i--) {
            list.add(matrix[i][start]);
            System.out.print(matrix[i][start] + " ");
        }

        //继续打印更内部的矩阵，令start+1
        printMatrixInCircle(matrix, start + 1, list);
    }


    //题目：定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的min
    //函数。在该栈中，调用min、push及pop的时间复杂度都是O(1)。
    private static Stack<Integer> stack_data = new Stack<Integer>();
    private static Stack<Integer> stack_min = new Stack<Integer>();
    public static void push(int node) {
        stack_data.push(node);
        if (stack_min.empty() || stack_min.peek() > node) {
            stack_min.push(node);
        } else {
            stack_min.push(stack_min.peek());
        }
    }

    public static void pop() {
        if (!stack_data.empty()) {
            stack_data.pop();
            stack_min.pop();
        }
    }

    public static int min() {
        return stack_min.peek();
    }

    /**
     * 是否为正确的弹出序列
     * //题目：输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是
     * //否为该栈的弹出顺序。假设压入栈的所有数字均不相等。例如序列1、2、3、4、
     * //5是某栈的压栈序列，序列4、5、3、2、1是该压栈序列对应的一个弹出序列，但
     * //4、3、5、1、2就不可能是该压栈序列的弹出序列。
     * @param pushArr
     * @param popArr
     * @return
     */
    public boolean isPopOrder(int[] pushArr, int[] popArr) {
        if (pushArr == null || popArr == null) {
            return false;
        }
        Stack<Integer> stack = new Stack<Integer>();
        //必须提前判断长度是否相等
        if (popArr.length != pushArr.length || pushArr.length == 0) {
            return false;
        }
        int popIndex = 0;
        for (int pushIndex = 0; pushIndex < pushArr.length; pushIndex++) {
            stack.push(pushArr[pushIndex]);
            while (!stack.empty() && stack.peek() == popArr[popIndex]) {
                stack.pop();
                popIndex++;
            }
        }
        return stack.empty();
    }

    /**
     *  不分行从上往下打印二叉树
     *  题目：从上往下打印出二叉树的每个结点，同一层的结点按照从左到右的顺序打印。
     * @param root
     */
    public static void printBinaryTree1(BinaryTreeNode root) {
        if (root == null) {
            return;
        }
        LinkedList<BinaryTreeNode> queue = new LinkedList<BinaryTreeNode>();
        queue.offer(root);
        BinaryTreeNode node = null;
        while (queue.size() != 0) {
            node = queue.poll();
            System.out.print(node.getValue() + " ");
            if (node.getLeftNode() != null) {
                queue.offer(node.getLeftNode());
            }
            if (node.getRightNode() != null) {
                queue.offer(node.getRightNode());
            }
        }
        System.out.println();
    }

    /**
     * 分行从上到下打印二叉树
     * 题目：从上到下按层打印二叉树，同一层的结点按从左到右的顺序打印，每一层
     * 打印到一行。
     * @param root
     */
    public static void printBinaryTree2(BinaryTreeNode root) {
        if (root == null) {
            return;
        }
        LinkedList<BinaryTreeNode> queue = new LinkedList<BinaryTreeNode>();
        queue.offer(root);
        BinaryTreeNode node = null;
        int pCount = 0;      //当前层结点数目
        int nextCount = 1;   //下一层结点数目
        while (!queue.isEmpty()) {
            pCount = nextCount;
            nextCount = 0;
            //打印当前层数字，并计算下一层结点数目
            for (int i = 1; i <= pCount; i++) {
                node = queue.poll();
                System.out.print(node.getValue() + " ");
                if (node.getLeftNode() != null) {
                    queue.offer(node.getLeftNode());
                    nextCount++;
                }
                if (node.getRightNode() != null) {
                    queue.offer(node.getRightNode());
                    nextCount++;
                }
            }
            System.out.println();
        }
    }

    /**
     * 之字形打印二叉树
     * 题目：请实现一个函数按照之字形顺序打印二叉树，即第一行按照从左到右的顺
     * 序打印，第二层按照从右到左的顺序打印，第三行再按照从左到右的顺序打印，
     * 其他行以此类推。
     * @param root
     */
    public static void printBinaryTree3(BinaryTreeNode root) {
        if (root == null) {
            return;
        }
        Stack<BinaryTreeNode> stack1 = new Stack<BinaryTreeNode>();
        Stack<BinaryTreeNode> stack2 = new Stack<BinaryTreeNode>();
        BinaryTreeNode node = null;
        stack1.push(root);
        while (!stack1.empty() || !stack2.empty()) {
            while (!stack1.empty()) {
                node = stack1.pop();
                System.out.print(node.getValue() + " ");
                if (node.getLeftNode() != null) {
                    stack2.push(node.getLeftNode());
                }
                if (node.getRightNode() != null) {
                    stack2.push(node.getRightNode());
                }
            }
            System.out.println();
            while (!stack2.empty()) {
                node = stack2.pop();
                System.out.print(node.getValue() + " ");
                if (node.getRightNode() != null) {
                    stack1.push(node.getRightNode());
                }
                if (node.getLeftNode() != null) {
                    stack1.push(node.getLeftNode());
                }
            }
            System.out.println();
        }
    }

    /**
     * 题目：输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。
     * 如果是则返回true，否则返回false。假设输入的数组的任意两个数字都互不相同。
     * 二叉树后序遍历数组的最后一个数为根结点，剩余数字中，小于根结点的数字（即左子树部分）都排在前面，
     * 大于根结点的数字（即右子树部分）都排在后面
     * @param sequence
     * @return
     */
    public static boolean verifySquenceOfBST(int[] sequence) {
        if (sequence == null || sequence.length <= 0) {
            return false;
        }
        return verifyCore(sequence, 0, sequence.length - 1);
    }

    private static boolean verifyCore(int[] sequence, int start, int end) {
        if (start >= end) {
            return true;
        }
        //判断左子树
        int mid = start;
        while (sequence[mid] < sequence[end]) {
            mid++;
        }
        //判断右子树
        for (int i = mid; i < end; i++) {
            if (sequence[i] < sequence[end]) {
                return false;
            }
        }
        return verifyCore(sequence, start, mid - 1) && verifyCore(sequence, mid, end - 1);
    }

    /**
     * 输入一棵二叉树和一个整数，打印出二叉树中结点值的和为输入整数的所有路径。
     * 从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。
     * @param root
     * @param target
     */
    public static void findPath(BinaryTreeNode<Integer> root, int target) {
        if (root == null) {
            return;
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        printPath(root, target, list);
    }

    private static void printPath(BinaryTreeNode<Integer> node, int target, ArrayList<Integer> list) {
        if (node == null) {
            return;
        }
        list.add(node.getValue());
        target -= node.getValue();       // 每个结点的target不会受到方法的影响而改变
        if (target == 0 && node.getLeftNode() == null && node.getRightNode() == null) { // 叶子结点
            for (Integer integer : list) {
                System.out.print(integer + " ");
            }
            System.out.println();
        } else {                         // 中间结点
            printPath(node.getLeftNode(), target, list);
            printPath(node.getRightNode(), target, list);
        }
        list.remove(list.size() - 1);
    }

    /**
     * 序列化二叉树。
     * @param node
     * @return
     */
    public static String serialize(BinaryTreeNode node) {
        StringBuilder sb = new StringBuilder();
        if (node == null) {
            sb.append("$,");
        } else {
            sb.append(node.getValue() + ",");
            sb.append(serialize(node.getLeftNode()));
            sb.append(serialize(node.getRightNode()));
        }
        return sb.toString();
    }

    private static int index = 0;
    /**
     * 反序列化二叉树
     * @param str
     * @return
     */
    public static BinaryTreeNode deserialize(String str) {
        BinaryTreeNode node = null;
        if (str == null || str.length() == 0) {
            return node;
        }
        int start = index;
        while (str.charAt(index) != ',') {
            index++;
        }
        if (!str.substring(start, index).equals("$")) {
            node = new BinaryTreeNode(Integer.parseInt(str.substring(start, index)));
            index++;           // 这条语句位置别放错了
            node.setLeftNode(deserialize(str));
            node.setRightNode(deserialize(str));
        } else {
            index++;
        }
        return node;
    }

    /**
     * 输入一个字符串，打印出该字符串中字符的所有排列。例如输入字符串abc，则打印出由字符a、b、c所能排列出来的所有字符串abc、acb、bac、bca、cab和cba。
     * （本文代码采用ArrayList<String>接收返回的字符串，并要求不出现重复字符串）
     * @param str
     * @return
     */
    public static ArrayList<String> permutation(String str) {
        ArrayList<String> list = new ArrayList<String>();
        if (str == null || str.length() == 0) {
            return list;
        }
        permutationCore(str.toCharArray(), 0, list);
        Collections.sort(list); // 将list中的字符串排序
        return list;
    }

    private static void permutationCore(char[] strArray, int index, ArrayList<String> list) {
        if (index == strArray.length - 1) {
            if (!list.contains(String.valueOf(strArray))) { // 判断是否有重复字符串
                list.add(String.valueOf(strArray));
            }
        } else {
            for (int i = index; i < strArray.length; i++) {
                char temp = strArray[index];
                strArray[index] = strArray[i];
                strArray[i] = temp;
                System.out.println("strArray = " + String.valueOf(strArray) + "; i = " + i);
                permutationCore(strArray, index + 1, list);
                strArray[i] = strArray[index];
                strArray[index] = temp;
            }
        }
    }

    private static boolean mIsInputInvalid = true;
    /**
     * 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。
     * 例如输入一个长度为9的数组{1, 2, 3, 2, 2, 2, 5, 4, 2}。由于数字2在数组中出现了5次，超过数组长度的一半，因此输出2。
     * @param array
     * @return
     */
    public static int moreThanHalfNum_Solution2(int[] array) {
        if (array == null || array.length <= 0) {
            return 0;
        }
        int num = array[0];
        int count = 1;
        for (int i = 1; i < array.length; i++) {
            if (count == 0) {
                num = array[i];
                count++;
            } else if (array[i] == num) {
                count++;
            } else {
                count--;
            }
        }
        if (count > 0) {
            int times = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] == num) {
                    times++;
                }
            }
            if (times * 2 > array.length) {
                mIsInputInvalid = false;
                return num;
            }
        }
        return 0;
    }

    /**
     *  输入n个整数，找出其中最小的k个数。
     *  例如输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。
     * @param input
     * @param k
     * @return
     */
    public static ArrayList<Integer> getLeastNumbers_Solution2(int[] input, int k) {
        ArrayList<Integer> leastNumbers = new ArrayList<Integer>();
        while (input == null || k <= 0 || k > input.length) {
            return leastNumbers;
        }
        int[] numbers = new int[k];  //用于放最小的k个数
        for (int i = 0; i < k; i++) {
            numbers[i] = input[i];//先放入前k个数
        }
        creatMaxHeap(numbers, 0, k - 1);//将数组构造成最大堆形式
        for (int i = k; i < input.length; i++) {
            if (input[i] < numbers[0]) { //存在更小的数字时
                numbers[0] = input[i];
                creatMaxHeap(numbers, 0, k - 1);//重新调整最大堆
            }
        }
        for (int n : numbers) {
            leastNumbers.add(n);
        }
        return leastNumbers;
    }

    private static boolean mInvalidInput = false;
    /**
     * 题目：输入一个整型数组，数组里有正数也有负数。数组中一个或连续的多个整
     * 数组成一个子数组。求所有子数组的和的最大值。要求时间复杂度为O(n)。
     * @param array
     * @return
     */
    public static int findGreatestSumOfSubArray(int[] array) {
        if (array == null || array.length <= 0) {
            mInvalidInput = true;
            return 0;
        }
        mInvalidInput = false;
        int sum = array[0];
        int maxSum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (sum < 0) {
                sum = array[i];
            } else {
                sum += array[i];
            }
            if (sum > maxSum) {
                maxSum = sum;
            }
        }
        return maxSum;
    }

    /**
     * 题目：输入一个整数n，求从1到n这n个整数的十进制表示中1出现的次数。例如
     * 输入12，从1到12这些整数中包含1 的数字有1，10，11和12，1一共出现了5次。
     * @param n
     * @return
     */
    public static int numberOf1Between1AndN_Solution(int n) {
        int count = 0;
        for (int i = 1; i <= n; i *= 10) { // i代表位数
            int high = n / (i * 10); // 更高位数字
            int low = (n % i); // 更低位数字
            int cur = (n / i) % 10; // 当前位数字
            System.out.println("n = " + n + "; high = " + high + "; low = " + low + "; cur = " + cur + "; i = " + i);
            if (cur == 0) {
                count += high * i;
            } else if (cur == 1) {
                count += high * i + (low + 1);
            } else {
                count += (high + 1) * i;
            }
        }
        System.out.println("count = " + count);
        return count;
    }

    /**
     * 数字序列中某一位的数字
     * 数字以0123456789101112131415…的格式序列化到一个字符序列中。
     * 在这个序列中，第5位（从0开始计数）是5，第13位是1，第19位是4，等等。
     * 请写一个函数求任意位对应的数字。
     * @param index
     * @return
     */
    public static int digitAtIndex(int index) {
        if (index < 0) {
            return -1;
        }
        int m = 1;  //m位数
        while (true) {
            int numbers = numbersOfIntegers(m);  //m位数的个数
            if (index < numbers * m) {
                return getDigit(index, m);
            }
            index -= numbers * m;
            m++;
        }
    }

    /**
     * 返回m位数的总个数
     * 例如，两位数一共有90个：10~99；三位数有900个：100~999
     */
    private static int numbersOfIntegers(int m) {
        if (m == 1) {
            return 10;
        }
        return (int) (9 * Math.pow(10, m - 1));
    }

    /**
     * 获取数字
     */
    private static int getDigit(int index, int m) {
        int number = getFirstNumber(m) + index / m;  //对应的m位数
        int indexFromRight = m - index % m;  //在数字中的位置
        for (int i = 1; i < indexFromRight; i++) {
            number /= 10;
        }
        return number % 10;
    }

    /**
     * 第一个m位数
     * 例如第一个两位数是10，第一个三位数是100
     */
    private static int getFirstNumber(int m) {
        if (m == 1) {
            return 0;
        }
        return (int) Math.pow(10, m - 1);
    }

    /**
     * 把数组排成最小的数
     * 输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
     * 例如输入数组{3, 32, 321}，则打印出这3个数字能排成的最小数字321323。
     * @param numbers
     * @return
     */
    public static String printMinNumber(int[] numbers) {
        if (numbers == null || numbers.length <= 0) {
            return "";
        }
        ArrayList<String> list = new ArrayList<String>();
        for (int number : numbers) {
            list.add(String.valueOf(number));
        }
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String a = s1 + s2;
                String b = s2 + s1;
                return a.compareTo(b);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 把数字翻译成字符串
     * 题目：给定一个数字，我们按照如下规则把它翻译为字符串：0翻译成"a"，1翻
     * 译成"b"，……，11翻译成"l"，……，25翻译成"z"。一个数字可能有多个翻译。例
     * 如12258有5种不同的翻译，它们分别是"bccfi"、"bwfi"、"bczi"、"mcfi"和
     * "mzi"。请编程实现一个函数用来计算一个数字有多少种不同的翻译方法。
     * @param number
     * @return
     */
    public static int getTranslationCount(int number) {
        if (number < 0) {
            return 0;
        }
        String sNumber = String.valueOf(number);
        int len = sNumber.length();
        int[] counts = new int[len];
        for (int i = len - 1; i >= 0; i--) {
            if (i == len - 1) {
                counts[i] = 1;
            } else {
                counts[i] = counts[i + 1];
                if (canBeTrans(sNumber, i)) {
                    if (i == len - 2) {
                        counts[i] += 1;
                    } else {
                        counts[i] += counts[i + 2];
                    }
                }
            }
        }
        return counts[0];
    }

    private static boolean canBeTrans(String sNumber, int i) {
        int a = sNumber.charAt(i) - '0';
        int b = sNumber.charAt(i + 1) - '0';
        int convert = a * 10 + b;
        if (convert >= 10 && convert <= 25) {
            return true;
        }
        return false;
    }

    /**
     * 礼物的最大价值
     * 在一个m×n的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于0）。
     * 你可以从棋盘的左上角开始拿格子里的礼物，并每次向右或者向下移动一格直到到达棋盘的右下角。
     * 给定一个棋盘及其上面的礼物，请计算你最多能拿到多少价值的礼物
     * @param values
     * @return
     */
    public static int maxValueOfGifts(int[][] values) {
        if (values == null || values.length <= 0 || values[0].length <= 0) {
            return 0;
        }
        int rows = values.length;
        int cols = values[0].length;
        int[] maxValue = new int[cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int left = 0;
                int up = 0;
                if (i > 0) {
                    up = maxValue[j];
                }
                if (j > 0) {
                    left = maxValue[j - 1];
                }
                maxValue[j] = Math.max(up, left) + values[i][j];
                Log.v(TAG, "i = " + i + "; j = " + j + "; maxValue = " + maxValue[j] + "; up = " + up + "; left = " + left);
            }
        }
        return maxValue[cols - 1];
    }

    /**
     * 最长不含重复字符的子字符串
     * 请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度。
     * 假设字符串中只包含从'a'到'z'的字符
     * @param str
     * @return
     */
    public static int lengthOfLongestSubstring(String str) {
        if (str.length() == 0) {
            return 0;
        }
        int maxLength = 1;
        List<Character> list = new ArrayList<Character>();
        list.add(str.charAt(0));
        for (int i = 1; i < str.length(); i++) {
            if (list.contains(str.charAt(i))) {
                // 返回与当前字符相同字符的索引
                int index = list.indexOf(str.charAt(i));
                list = list.subList(index + 1, list.size());
                list.add(str.charAt(i));
                maxLength = Math.max(maxLength, list.size());
            } else {
                list.add(str.charAt(i));
                maxLength = Math.max(maxLength, list.size());
            }
        }
        return maxLength;
    }

    private static int[] positionRecords; //字符读出位置记录
    private static int position;  //字符的读出位置
    /**
     * 字符流中第一个只出现一次的字符
     * 请实现一个函数用来找出字符流中第一个只出现一次的字符。
     * 例如，当从字符流中只读出前两个字符"go"时，第一个只出现一次的字符是'g'。
     * 当从该字符流中读出前六个字符"google"时，第一个只出现一次的字符是'l'。
     * @param str
     */
    private static void findFirstCharInCharacterStream(String str) {
        positionRecords = new int[256];
        for (int i = 0; i < positionRecords.length; i++) {
            positionRecords[i] = -1;
        }
        position = 0;
        for (int i = 0; i < str.length(); i++) {
            insert(str.charAt(i));
        }
        System.out.println(firstAppearingOnceChar());
    }

    // 从字符流中读出字符
    private static void insert(char ch) {
        if (positionRecords[ch] == -1) {
            positionRecords[ch] = position; // 记录读出位置
        } else if (positionRecords[ch] >= 0) {
            positionRecords[ch] = -2; // 该字符出现多次
        }
        position++;
    }

    // 字符流中第一个只出现一次的字符
    private static char firstAppearingOnceChar() {
        char result = '#';
        int minIndex = Integer.MAX_VALUE; // 非重复字符最早出现的位置
        for (int i = 0; i < positionRecords.length; i++) {
            System.out.println("i = " + i + "; value = " + positionRecords[i]);
            if (positionRecords[i] >= 0 && positionRecords[i] < minIndex) {
                result = (char) i;
                minIndex = positionRecords[i];
            }
        }
        return result;
    }

    /**
     * 两个链表的第一个公共结点
     * 输入两个链表，找出它们的第一个公共结点
     * @param pHead1
     * @param pHead2
     * @return
     */
    public static ListNode findFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        int length1 = getLength(pHead1);
        int length2 = getLength(pHead2);
        int lengthDif = length1 - length2;
        ListNode longList = pHead1;
        ListNode shortList = pHead2;
        if (lengthDif < 0) {
            longList = pHead2;
            shortList = pHead1;
            lengthDif = -lengthDif;
        }
        for (int i = 0; i < lengthDif; i++) {
            longList = longList.next;
        }
        while (longList != null && longList != shortList) {
            longList = longList.next;
            shortList = shortList.next;
        }
        return longList;  //没有公共结点刚好是null
    }

    /**
     * 获取链表的长度
     * @param head
     * @return
     */
    public static int getLength(ListNode head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }

    /**
     * 二叉搜索树的第k个结点
     * 给定一棵二叉搜索树，请找出其中的第k小的结点。
     * @param pRoot
     * @param k
     * @return
     */
    public static BinaryTreeNode kthNode(BinaryTreeNode pRoot, int k) {
        if (pRoot == null || k <= 0) {
            return null;
        }
        List<BinaryTreeNode> cache = new ArrayList<>();
        kthNodeHelper(pRoot, k, cache);
        return k <= cache.size() ? cache.get(k - 1) : null;
    }

    private static void kthNodeHelper(BinaryTreeNode node, int k, List<BinaryTreeNode> cache) {
        if (node == null) {
            return;
        }
        kthNodeHelper(node.getLeftNode(), k, cache);
        cache.add(node);
        if (cache.size() == k) {
            return;
        }
        kthNodeHelper(node.getRightNode(), k, cache);
    }

    /**
     * 翻转单词顺序
     * 输入一个英文句子，翻转句子中单词的顺序，但单词内字符的顺序不变。
     * 为简单起见，标点符号和普通字母一样处理。例如输入字符串"I am a student. "，则输出"student. a am I"。
     * @param chars
     * @return
     */
    public static String reverseSentence(char[] chars) {
        if (chars == null || chars.length <= 0) {
            return String.valueOf(chars);
        }
        //翻转整个句子
        reverseSb(chars, 0, chars.length - 1);
        //翻转单词（指针指向单词的第一个和最后一个）
        int start = 0;
        int end = 0;
        while (start < chars.length) {
            while (end < chars.length && chars[end] != ' ') {
                end++;
            }
            reverseSb(chars, start, end - 1);
            start = ++end;
        }
        return String.valueOf(chars);
    }

    /**
     * 另外一种逻辑
     * @param s
     * @return
     */
    public static String revertWords(String s) {
        Stack stack = new Stack();
        String temp = "";
        for(int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                temp += s.charAt(i);
            } else if (!temp.equals("")) {
                stack.push(temp);
                temp = "";
            } else {
                continue;
            }
        }
        if (!temp.equals("")) {
            stack.push(temp);
        }
        String result = "";
        while(!stack.empty()) {
            result += stack.pop() + " ";
        }
        return result.substring(0, result.length() - 1);
    }

    private static void reverseSb(char[] chars, int start, int end) {
        while (start < end) {
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * 左旋转字符串
     * 字符串的左旋转操作是把字符串前面的若干个字符转移到字符串的尾部。
     * 请定义一个函数实现字符串左旋转操作的功能。
     * 比如输入字符串"abcdefg"和数字2，该函数将返回左旋转2位得到的结果"cdefgab"。
     * @param chars
     * @param n
     * @return
     */
    public static String leftRotateString(char[] chars, int n) {
        if (chars == null || chars.length <= 0) {
            return String.valueOf(chars);
        }
        if (n <= 0 || n > chars.length) {
            return String.valueOf(chars);
        }
        reverseSb(chars, 0, n - 1);
        reverseSb(chars, n, chars.length - 1);
        reverseSb(chars, 0, chars.length - 1);
        return String.valueOf(chars);
    }

    /**
     * 滑动窗口的最大值
     * 给定一个数组和滑动窗口的大小，请找出所有滑动窗口里的最大值。
     * 例如，如果输入数组{2, 3, 4, 2, 6, 2, 5, 1}及滑动窗口的大小3，那么一共存在6个滑动窗口，它们的最大值分别为{4, 4, 6, 6, 6, 5}
     * @param num
     * @param size
     * @return
     */
    public static List<Integer> maxInWindows(int[] num, int size) {
        List<Integer> max = new ArrayList<Integer>();
        if (num == null || num.length <= 0 || size <= 0 || size > num.length) {
            return max;
        }
        ArrayDeque<Integer> indexDeque = new ArrayDeque<Integer>();

        for (int i = 0; i < size - 1; i++) {
            while (!indexDeque.isEmpty() && num[i] > num[indexDeque.getLast()]) {
                indexDeque.removeLast();
            }
            indexDeque.addLast(i);
        }
        System.out.println(indexDeque.size());
        for (int i = size - 1; i < num.length; i++) {
            while (!indexDeque.isEmpty() && num[i] > num[indexDeque.getLast()]) {
                indexDeque.removeLast();
            }

            if (!indexDeque.isEmpty() && (i - indexDeque.getFirst()) >= size) {
                indexDeque.removeFirst();
            }
            indexDeque.addLast(i);
            max.add(num[indexDeque.getFirst()]);
        }
        return max;
    }

    /**
     * 股票的最大利润
     * 假设把某股票的价格按照时间先后顺序存储在数组中，请问买卖交易该股票可能获得的利润是多少？
     * 例如一只股票在某些时间节点的价格为{9, 11, 8, 5,7, 12, 16, 14}。
     * 如果我们能在价格为5的时候买入并在价格为16时卖出，则能收获最大的利润11。
     * @param arr
     * @return
     */
    public static int maxDiff(int[] arr) {
        if (arr == null || arr.length < 2) {
            return -1;      //error
        }
        int min = arr[0];

        //最大利润可以是负数，只要亏损最小就行
        int maxDiff = arr[1] - min;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] < min) {     //保存“之前”最小数字
                min = arr[i - 1];
            }
            if (arr[i] - min > maxDiff) {
                maxDiff = arr[i] - min;
            }
        }
        return maxDiff;
    }

    /**
     * 求1+2+…+n
     * 求1+2+…+n，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。
     * @param n
     * @return
     */
    public static int getSum(int n) {
        int sum = n;
        boolean flag = (n > 1) && ((sum += getSum(n - 1)) > 0);
        //上面这句话相当于：
        //if(n>1)
        //   sum+=getSum(n-1);

        //也可以使用||来实现
        //boolean flag = (n==1) || ((sum+=getSum(n-1))>0);
        return sum;
    }

    /**
     * 不用加减乘除做加法
     * 写一个函数，求两个整数之和，要求在函数体内不得使用＋、－、×、÷四则运算符号。
     * @param num1
     * @param num2
     * @return
     */
    public static int add(int num1, int num2) {
        while (num2 != 0) {
            int sum = num1 ^ num2;            // 没进位的和
            int carry = (num1 & num2) << 1;   // 进位
            // 如果可以用加法这样写 return (sum + carry);
            num1 = sum;
            num2 = carry;
        }
        return num1;
    }

    /**
     * 构建乘积数组
     * 给定一个数组A[0, 1, …, n-1]，请构建一个数组B[0, 1, …, n-1]，其中B中的元素B[i] =A[0]×A[1]×… ×A[i-1]×A[i+1]×…×A[n-1]。
     * 不能使用除法。
     * @param A
     * @return
     */
    public static int[] multiply(int[] A) {
        if (A == null || A.length < 2) {
            return null;
        }
        int[] B = new int[A.length];
        B[0] = 1;
        for (int i = 1; i < A.length; i++) {
            B[i] = B[i - 1] * A[i - 1];
        }
        int temp = 1;
        for (int i = A.length - 2; i >= 0; i--) {
            temp *= A[i + 1];
            B[i] *= temp;
        }
        return B;
    }

    /**
     * 寻找两个有序数组的中位数
     * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
     * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
     * 你可以假设 nums1 和 nums2 不会同时为空。
     * @param nums1
     * @param nums2
     * @return
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int size = len1 + len2;
        if (size % 2 == 1) {
            return findKth(nums1, 0, len1, nums2, 0, len2, size / 2 + 1);
        } else {
            return (findKth(nums1, 0, len1, nums2, 0, len2, size / 2) + findKth(nums1, 0, len1, nums2, 0, len2, size / 2 + 1)) / 2;
        }
    }

    /**
     * @param nums1
     * @param start1
     * @param len1
     * @param nums2
     * @param start2
     * @param len2
     * @param k k在方法中的真正含义为“在这两个数组中找第k小的数”
     * @return
     */
    private static double findKth(int[] nums1, int start1, int len1, int[] nums2, int start2, int len2, int k) {
        if (len1 - start1 > len2 - start2) {        // 传进来的时候统一让短的数组为nums1
            return findKth(nums2, start2, len2, nums1, start1, len1, k);
        }
        System.out.println("len1 = " + len1 + "; start1 = " + start1 + "; len2 = " + len2 + "; start2 = " + start2 + "; k = " + k);
        if (len1 - start1 == 0) {           // 表示nums1已经全部加入前K个了，第k个为nums2[k - 1];
            return nums2[k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[start1], nums2[start2]);
            // k==1表示已经找到第k-1小的数，下一个数为两个数组start开始的最小值
        }
        int p1 = start1 + Math.min(len1 - start1, k / 2); // p1和p2记录当前需要比较的那个位
        int p2 = start2 + k - (p1 - start1);

        System.out.println("nums1[" + (p1 - 1) + "] = " + nums1[p1 - 1] + "; nums2[" + (p2 - 1) + "] = " + nums2[p2 - 1] + "; (p1 + p2) = " + (p1 + p2));
        if (nums1[p1 - 1] < nums2[p2 - 1]) {
            return findKth(nums1, p1, len1, nums2, start2, len2, k - p1 + start1);
        } else if (nums1[p1 - 1] > nums2[p2 - 1]) {
            return findKth(nums1, start1, len1, nums2, p2, len2, k - p2 + start2);
        } else {
            return nums1[p1 - 1];
        }
    }

    /**
     * 最长回文子串(动态规划法)
     * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
     * @param str
     * @return
     */
    public static String getLongestPalindrome(String str) {
        if (com.ztq.sdk.utils.Utils.isNullOrNil(str)) {
            return "";
        }
        int len = str.length();
        if (len < 2) {
            return str;
        }
        boolean[][] dp = new boolean[len][len];
        for(int i = 0; i < len; i++) {
            dp[i][i] = true;
        }
        int maxLen = 1;
        int start = 0;
        for(int j = 1; j < len; j++) {
            for(int i = 0; i < j; i++) {
                if(str.charAt(i) == str.charAt(j)) {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                } else {
                    dp[i][j] = false;
                }
                if (dp[i][j]) {
                    int curLen = j - i + 1;
                    if (curLen > maxLen) {
                        maxLen = curLen;
                        start = i;
                    }
                }
            }
        }
        return str.substring(start, start + maxLen);
    }

    /**
     * Z字型变换
     * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
     * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
     * L   C   I   R
     * E T O E S I I G
     * E   D   H   N
     * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
     * 请你实现这个将字符串进行指定行数变换的函数
     * @param s
     * @param numRows
     * @return
     */
    public static String convert(String s, int numRows) {
        if (com.ztq.sdk.utils.Utils.isNullOrNil(s) || numRows <= 0) {
            return "";
        }
        if (numRows == 1) {
            return s;
        }
        List<StringBuilder> rows = new ArrayList<>();
        for (int i = 0; i < Math.min(numRows, s.length()); i++) {
            rows.add(new StringBuilder());
        }
        int curRow = 0;
        boolean goingDown = false;
        for (char c : s.toCharArray()) {
            rows.get(curRow).append(c);  // 按行存值
            if (curRow == 0 || curRow == numRows - 1) {
                goingDown = !goingDown;   // 转向,很独特的方法,学到了
            }
            curRow += goingDown ? 1 : -1;  // 递增或递减
        }

        StringBuilder ret = new StringBuilder();
        for (StringBuilder row : rows) {
            ret.append(row);      // 拼接字符串
        }
        return ret.toString();
    }

    /**
     * 反转整数（reverse integer）
     * 给定一个 32 位有符号整数，将整数中的数字进行反转。
     * @param x
     * @return
     */
    public static int reverse(int x) {
        int res = 0;
        while (x != 0) {
            int temp = x % 10 + res * 10;
            System.out.println("temp = " + temp + "; x = " + x + "; x%10 = " + (x %10) + "; res = " + res);
            if ((temp - x % 10) / 10 != res) {
                return 0;
            }
            res = temp;
            x /= 10;
        }
        return res;
    }

    /**
     * 反转整数（reverse integer）方法二
     * @param x
     * @return
     */
    public static int reverse1(int x) {
        long res = 0;
        int flag = -1;
        if (x < 0) {
            x = -x;
        } else {
            flag = 1;
        }
        while (x != 0) {
            res = res * 10 + x % 10;
            x /= 10;
        }
        res *= flag;
        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            return 0;
        } else {
            return (int) res;
        }
    }

    /**
     * 字符串转换整数 (atoi)
     * 请你来实现一个 atoi 函数，使其能将字符串转换成整数。
     * 首先，该函数会根据需要丢弃无用的开头空格字符，直到寻找到第一个非空格的字符为止。
     * 当我们寻找到的第一个非空字符为正或者负号时，则将该符号与之后面尽可能多的连续数字组合起来，作为该整数的正负号；假如第一个非空字符是数字，则直接将其与之后连续的数字字符组合起来，形成整数。
     * 该字符串除了有效的整数部分之后也可能会存在多余的字符，这些字符可以被忽略，它们对于函数不应该造成影响。
     * 注意：假如该字符串中的第一个非空格字符不是一个有效整数字符、字符串为空或字符串仅包含空白字符时，则你的函数不需要进行转换。
     * 在任何情况下，若函数不能进行有效的转换时，请返回 0。
     * @param str
     * @return
     */
    public static int myAtoi(String str) {
        if (com.ztq.sdk.utils.Utils.isNullOrNil(str)) {
            return 0;
        }
        str = str.trim(); // 删除字符串头尾空格
        if (str.length() == 0) {
            return 0;
        }
        int flag = 1; // 符号位标识
        int rev = 0; // 数值（无符号）
        int edge = Integer.MAX_VALUE / 10;               // 判断数值是否超过范围的边界线，这样写可以节省时间
        if (str.charAt(0) == '-') {
            flag = -1;
            str = str.substring(1, str.length());                  // 跳过符号位，可不写第二参数
        } else if (str.charAt(0) == '+') {
            str = str.substring(1, str.length());                 // 跳过符号位，可不写第二参数
        } else if (!(str.charAt(0) >= '0' && str.charAt(0) <= '9')) { // 如果开始非空字符不为符号或数字，则直接返回0
            return 0;
        }
        for (char s : str.toCharArray()) {
            if (s >= '0' && s <= '9') {
                int n = s - '0'; // 计算字符代表值
                if (rev >= edge) { // 超过边界情况较少，故该判断写于外侧
                    if (flag == 1) {
                        if (rev > edge || n > 7) {             // Integer.MAX_VALUE=2147483647,最后一位为7
                            return Integer.MAX_VALUE;
                        }
                    } else {
                        if (rev > edge || n > 8) {           // Integer.MIN_VALUE=-2147483648,最后一位为8
                            return Integer.MIN_VALUE;
                        }
                    }
                }
                rev = rev * 10 + n;
            } else {
                break;
            }
        }
        return rev * flag;
    }

    /**
     * 盛最多水的容器
     * 给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。
     * 在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。
     * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     * 你不能倾斜容器，且 n 的值至少为 2。
     * @param height
     * @return
     */
    public static int maxArea(int[] height) {
        if (height == null) {
            return 0;
        }
        int i = 0;
        int j = height.length - 1;
        int res = 0;
        while (i < j) {
            int area = (j - i) * Math.min(height[i], height[j]);  //面积为底乘以高
            res = Math.max(res, area);  //选出大的面积
            if (height[i] <= height[j]) {
                i++;
            } else {
                j--;
            }
        }
        return res;
    }

    /**
     * 最长公共前缀
     * 编写一个函数来查找字符串数组中的最长公共前缀
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null) {
            return "";
        }
        int count = strs.length;
        String prefix = "";
        if (count != 0) {
            prefix = strs[0];
        }
        for (int i = 0; i < count; i++) {
            //关键代码，不断的从后往前截取字符串，然后与之相比，直到startsWith()返回true
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }
        return prefix;
    }

    // 求解两个字符号的最长公共子串
    public static String maxSubstring(String strOne, String strTwo) {
        // 参数检查
        if (strOne == null || strTwo == null) {
            return null;
        }
        if (strOne.equals("") || strTwo.equals("")) {
            return null;
        }
        // 二者中较长的字符串
        String max = "";
        // 二者中较短的字符串
        String min = "";
        if (strOne.length() < strTwo.length()) {
            max = strTwo;
            min = strOne;
        } else {
            max = strOne;
            min = strTwo;
        }
        String current = "";
        // 遍历较短的字符串，并依次减少短字符串的字符数量，判断长字符是否包含该子串
        for (int i = 0; i < min.length(); i++) {
            for (int begin = 0, end = min.length() - i; end <= min.length(); begin++, end++) {
                current = min.substring(begin, end);
                System.out.println("current = " + current + "; max = " + max + "; min = " + min + "; i = " + i);
                if (max.contains(current)) {
                    return current;
                }
            }
        }
        return null;
    }

    /**
     * 三个数之和等于0
     * 给定一个包含n个整数的数组，在数组中是否存在a、b、c元素使得a + b + c = 0?找出数组中所有唯一的三个数组合，它们的和为零。
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSumEqualsZero(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            // 当第i个数开始大于零时，跳出循环
            if (nums[i] > 0) {
                break;
            }
            // 当相邻数相等时，跳过该数，避免重复结果
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            // 使用两个指针，向中间夹
            int left = i + 1, right = nums.length - 1, sum = -nums[i];
            while (left < right) {
                int count = nums[left] + nums[right];
                if (count == sum) {
                    list.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (count < sum) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return list;
    }

    /**
     * 有效的括号
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
     * 有效字符串需满足：
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * 注意空字符串可被认为是有效字符串。
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        if (s.equals("")) {
            return true;
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char topChar = stack.pop();
                if (ch == ')' && topChar != '(') {
                    return false;
                } else if (ch == ']' && topChar != '[') {
                    return false;
                } else if (ch == '}' && topChar != '{') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 两两交换链表中的节点
     * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode firstNode = head;
        ListNode secondNode = head.next;
        firstNode.next = swapPairs(secondNode.next);
        secondNode.next = firstNode;
        return secondNode;
    }

    /**
     * K个一组翻转链表
     * 给定一个链表，每k个节点一组进行翻转，请返回翻转后的链表
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null || k <= 1) {
            return head;
        }
        ListNode tail = head;
        for(int i = 0; i < k; i++) {
            if (tail == null) {
                return head;
            }
            tail = head.next;
        }
        // 反转前k个元素
        ListNode newHead = reverseList(head, tail);
        // 下一轮开始的地方就是tail
        head.next = reverseKGroup(tail, k);
        return newHead;
    }

    /**
     * 删除排序数组中的重复项
     * 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
     * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
     * @param nums
     * @return
     */
    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int back = 0;
        for (int front = 1; front < nums.length; front++) {
            if (nums[back] != nums[front]) {
                back++;
                nums[back] = nums[front];
            }
        }
        return back + 1;
    }

    /**
     * 移除元素
     * 给定一个数组和一个值val，你需要原地移除所有值等于val的元素，并返回移除后的数组长度
     * @param nums
     * @param val
     * @return
     */
    public static int removeElements(int[] nums, int val) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int i = 0;
        for(int j = 0; j < nums.length; j++) {
            if (nums[j] == val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }

    /**
     * 两数相除
     * 给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
     * 返回被除数 dividend 除以除数 divisor 得到的商。
     * @param dividend
     * @param divisor
     * @return
     */
    public static int divide(int dividend, int divisor) {
        if (dividend == 0) {
            return 0;
        }
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        boolean negative;
        negative = (dividend ^ divisor) < 0;
        Log.v(TAG, "negative = " + negative);
        long t = Math.abs((long) dividend);
        long d = Math.abs((long) divisor);
        int result = 0;
        for (int i = 31; i >= 0; i--) {
            Log.v(TAG, "t = " + t + "; i = " + i + "; t>>i = " + (t>>i) + "; d = " + d);
            if ((t >> i) >= d) {
                result += 1 << i;
                t -= d << i;
            }
        }
        return negative ? -result : result;
    }

    public static void main(String[] args) {
        //TODO 动态规划求解最短路径问题
        int[][] m = {{ 0, 5, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 1, 3, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 8, 7, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 6, 8, 0, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 3, 5, 0, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 8, 4, 0, 0, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 2, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 0 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
                     { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3 }};
        Log.v(TAG, "minumim distance is " + minPath(m));
    }

    public static int minPath(int[][] matrix) {
        return process(matrix, matrix[0].length - 1);
    }

    public static int process(int[][] matrix, int i) {
        //到达A退出递归
        if (i == 0) {
            return 0;
        }
        // 状态转移
        int distance = 999;
        for(int j = 0; j < i; j++) {
            System.out.println("j = " + j + "; i = " + i + "; value = " + matrix[j][i]);
            if (matrix[j][i] != 0) {
                int d_tmp = matrix[j][i] + process(matrix, j);
                if (d_tmp < distance) {
                    distance = d_tmp;
                }
            }
        }
        return distance;
    }

    /**
     * 在一个流式数据中，查找中位数，如果是偶数个，则返回偏左边的那个元素
     */
    private static int count = 0;
    private static PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    private static PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg1.compareTo(arg0);
        }
    });
    public static void insert(Integer num) {
        if (count % 2 == 0) {
            minHeap.offer(num);
            maxHeap.offer(minHeap.poll());
        } else {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());
        }
        count++;
        System.out.println("median num is " + getMedian());
    }

    public static int getMedian() {
        return maxHeap.peek();
    }


    /**
     * 给定一个放歌棋盘，从左上角出发到右下角有多少种方。
     * 每次只能向右或向下，移动相邻的格子，同时，棋盘中有若干格子是陷阱，
     * 不可经过，必须绕开走
     */
    private static int[][] matrix ={{1, 1, 1, 1, 1, 1},
                                    {1, 1, -1, -1, 1, 1},
                                    {1, 1, -1, 1, -1, 1}};
    public static int getPath(int[][] m, int i, int j) {
        if (m == null) {
            return 0;
        }
        if(m[i][j] == -1) {
            return 0;
        }
        if (i > 0 && j > 0) {
            return getPath(m, i - 1, j) + getPath(m, i, j - 1);
        } else if (i == 0 && j > 0) {
            return getPath(m, i, j - 1);
        } else if (i > 0 && j == 0) {
            return getPath(m, i - 1, j);
        } else {
            return 1;
        }
    }

    /**
     * 根据每日气温列表，请重新生成一个列表，对应位置的输出是需要再等待多久温度才会升高超过该日的天数。
     * 如果之后都不会升高，请在该位置用 0 来代替。
     * @param T
     * @return
     */
    public static int[] dailyTemperatures(int[] T) {
        int n = T.length;
        if (n == 0) {
            return new int[n];
        }
        if (n == 1) {
            return new int[0];
        }
        Stack<Integer> stack = new Stack<>();
        stack.add(0);
        int[] ans = new int[n];
        for (int i = 1; i < T.length; i++) {
            while (stack.size() > 0 && T[i] > T[stack.peek()]) {
                ans[stack.peek()] = i - stack.peek();
                stack.pop();
            }
            stack.push(i);
        }
        while (stack.size() > 0) {
            ans[stack.pop()] = 0;
        }
        return ans;
    }

    /**
     * 给定一个整型数组，假如数组有两个数的和等于target，返回数组的下标组成的数组
     * @param nums
     * @param target
     * @return
     */
    public static int[] findTwoNumIsTargetValueIndex(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; ++i) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }
}