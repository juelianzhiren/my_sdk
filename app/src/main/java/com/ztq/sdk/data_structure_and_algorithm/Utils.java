package com.ztq.sdk.data_structure_and_algorithm;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    /**
     * 统计数字k在排序数组中出现的次数
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
        int middleIndex = (startIndex + endIndex) / 2;
        int middleData = a[middleIndex];
        if (middleData == k) {
            if ((middleIndex > 0 && a[middleIndex - 1] != k) || middleIndex == 0) {
                return  middleIndex;
            } else {
                endIndex = middleIndex - 1;
            }
        } else if (middleData > k) {
            endIndex = middleIndex - 1;
        } else {
            startIndex= middleIndex + 1;
        }
        return getFirstIndexK(a, k, startIndex, endIndex);
    }

    private static int getLastIndexK(int[] a, int k, int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            return -1;
        }
        int middleIndex = (startIndex + endIndex) / 2;
        int middleData = a[middleIndex];
        if (middleData == k) {
            if ((middleIndex < a.length - 1 && a[middleIndex + 1] != k) || middleIndex == a.length - 1) {
                return  middleIndex;
            } else {
                startIndex = middleIndex + 1;
            }
        } else if (middleData > k) {
            endIndex = middleIndex - 1;
        } else {
            startIndex= middleIndex + 1;
        }
        return getLastIndexK(a, k, startIndex, endIndex);
    }

    /**
     * 0~n-1中缺失的数字
     * 一个长度为n-1的递增排序数组中的所有数字都是唯一的，并且每个数字都在范围0~n-1之内。
     * 在范围0~n-1内的n个数字中有且只有一个数字不在该数组中，请找出这个数字
     * @param a
     * @return
     */
    public static int getMissingNumber(int[] a) {
        if (a == null || a.length == 0) {
            return -1;
        }
        int leftIndex = 0;
        int rightIndex = a.length - 1;
        while(leftIndex <= rightIndex) {
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
        if (leftIndex == a.length) {
            return a.length;
        }
        return -1;
    }

    /**
     * 二叉树的前序遍历
     * @param treeNode
     * @param list (遍历的值依次放进list列表中)
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
        if(leftTree != null) {
            preOrderTraverse(leftTree, list);
        }
        BinaryTreeNode rightTree = treeNode.getRightNode();
        if(rightTree != null) {
            preOrderTraverse(rightTree, list);
        }
    }

    /**
     * 二叉树的中序遍历
     * @param treeNode
     * @param list (遍历的值依次放进list列表中)
     */
    public static void midOrderTraverse(BinaryTreeNode<Integer> treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        BinaryTreeNode leftTree = treeNode.getLeftNode();
        if(leftTree != null) {
            midOrderTraverse(leftTree, list);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(treeNode.getValue());
        Log.v(TAG, "中序遍历：" + treeNode.getValue());
        BinaryTreeNode rightTree = treeNode.getRightNode();
        if(rightTree != null) {
            midOrderTraverse(rightTree, list);
        }
    }

    /**
     * 二叉树的后序遍历
     * @param treeNode
     * @param list (遍历的值依次放进list列表中)
     */
    public static void postOrderTraverse(BinaryTreeNode<Integer> treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        BinaryTreeNode leftTree = treeNode.getLeftNode();
        if(leftTree != null) {
            postOrderTraverse(leftTree, list);
        }
        BinaryTreeNode rightTree = treeNode.getRightNode();
        if(rightTree != null) {
            postOrderTraverse(rightTree, list);
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(treeNode.getValue());
        Log.v(TAG, "后序遍历：" + treeNode.getValue());
    }

    private  static boolean mIsBalanceBinaryTree = true;
    /**
     * 重置mIsBalanceBinaryTree变量值
     */
    public static void resetBalanceBinaryTreeData() {
        mIsBalanceBinaryTree = true;
    }

    /**
     * 获取二叉树的深度
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
     * @param array
     * @param list 记录这两个只出现一次的数字
     */
    public static void findNumsAppearOnce(int[] array, List<Integer> list) {
        if(array==null || array.length < 2)
            return;
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
        for(int i = 0; i < array.length; i++) {
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
     * 和为sum的连续正数序列
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
        while (small <= sum / 2) {
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
     *       求出这个圆圈里剩下的最后一个数字
     * @param n
     * @param m
     * @return
     */
    public static int getLastRemaining(int n, int m) {
        if(n < 1 || m < 1) {
            return -1;
        }
        int last = 0;
        for(int i = 2; i <= n; i++) {
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
            for(int i = 0; i < list.size(); i++) {
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
     *
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
    static class QueueByTwoStacks<E>{
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
     *
     */
    static class StackByTwoQueues<T> {
        private Queue<T> queue1 = new LinkedList<T>();
        private Queue<T> queue2 = new LinkedList<T>();

        /**
         * 压栈
         *
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
            return this.queue1.toString() + ", " +this.queue2.toString();
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
        int mid = (low + high) / 2;
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
            mid = (low + high) / 2;
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
     *
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
        int[] dx = { 1, -1, 0, 0 };    // 向右、向左、向下、向上方向
        int[] dy = { 0, 0, 1, -1 };
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
     *
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
        for(int i = 0; i < rows * cols; i++) {
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
     *
     * @Description 剪绳子
     *
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
            for (int j = 1; j <= i / 2; j++) {
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
        timesOf2 = (length - timesOf3 * 3) / 2;
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
}