package com.ztq.sdk.data_structure_and_algorithm;

import java.util.Random;

/**
 * 跳表，redis中使用的缓存结构
 * 实质是一种可以进行二分查找的链表
 * 在原有的有序链表上面增加了多级索引，通
 * 过索引来实现快速查找，以空间换时间
 */
public class SkipList {
    final int HEAD_VALUE = -1;    // 链表头结点的值
    final Node HEAD = new Node(HEAD_VALUE);
    Node head;                   // 最左上角的头结点，所有操作的起始位置
    int levels;                  // 当前层级，即head节点所在的最高层数

    public SkipList() {
        head = HEAD;
        levels = 1;
    }

    /**
     * 查找数据为target的节点
     *
     * @param target
     * @return
     */
    public boolean search(int target) {
        Node n = head;
        while (n != null) {
            // 1.在同一层级上向右查找，直到链表结尾，或者找到
            while (n.right != null && n.right.val < target) {
                n = n.right;
            }
            // 2.若找到，返回true
            Node right = n.right;
            if (right != null && right.val == target) {
                return true;
            }
            // 3.若右侧没有数据或者数据较大，向下一层
            n = n.down;
        }
        return false;
    }

    /**
     * 删除数据为num的节点（若有重复的值，删除第一个即可）
     *
     * @param num
     * @return
     */
    public boolean erase(int num) {
        boolean exist = false;
        Node n = head;
        while (n != null) {
            // 1.获取该指定数据节点的前一个节点
            while (n.right != null && n.right.val < num) {
                n = n.right;
            }
            Node right = n.right;
            // 2.与当前层链表断开
            if (right != null && right.val == num) {
                n.right = right.right;
                right.right = null;
                exist = true;
            }
            // 3.删除下一层
            n = n.down;
        }
        return exist;
    }

    public void add(int num) {
        // 1.定位插入位置，原链表中>=num的最小节点前
        Node node = head;    // 从head开始查找
        // 节点向下，可能是生成索引的位置，使用数组记录这些节点
        Node[] nodes = new Node[levels];
        int i = 0; // 操作上述数组
        while (node != null) {   // 当node==null时，到达原链表
            // 在同一层级上向右查找，直到链表结尾，或者找到
            while (node.right != null && node.right.val < num) {
                node = node.right;
            }
            // 右侧为结尾 or 右侧值大 or 右侧值相同
            nodes[i++] = node;
            node = node.down;
        }
        // 2.插入新节点
        node = nodes[--i];          // nodes中最后一个元素
        Node newNode = new Node(num, node.right, null);
        node.right = newNode;

        // 3.根据扔硬币决定是否生成索引
        addIndicesByCoinFlip(newNode, nodes, i);
    }

    private void addIndicesByCoinFlip(Node target, Node[] nodes, int indices) {
        Node downNode = target;
        Random random = new Random();
        int coins = random.nextInt(2); // 0 or 1, 50% 概率
        // 1.抛硬币，在现有跳表层数范围内建立索引
        while (coins == 1 && indices > 0) {
            Node prev = nodes[--indices]; // 数组的倒数第二个元素，level 2
            Node newIndex = new Node(target.val, prev.right, downNode);
            prev.right = newIndex;
            downNode = newIndex;
            coins = random.nextInt(2);
        }
        // 2.抛硬币，决定是否建立一层超出跳表层数的索引层
        if (coins == 1) { // 新建一个索引层级
            // 新建索引节点和 head 节点
            Node newIndex = new Node(target.val, null, downNode);
            Node newHead = new Node(HEAD_VALUE, newIndex, head);
            head = newHead; // head指针上移
            levels++; // 跳表层数加 1
        }
    }

    class Node {
        int val;
        Node right;
        Node down;

        public Node(int val) {
            this(val, null, null);
        }

        public Node(int val, Node right, Node down) {
            this.val = val;
            this.right = right;
            this.down = down;
        }
    }
}