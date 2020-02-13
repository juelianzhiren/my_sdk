package com.ztq.sdk.data_structure_and_algorithm;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
}