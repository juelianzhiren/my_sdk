package com.ztq.sdk.data_structure_and_algorithm;

/**
 * Author: ztq
 * Date: 2020/2/19 12:14
 * Description: 链表节点类
 */
public class ListNode<T> {
    T value;
    ListNode next;

    public ListNode(T value, ListNode nextNode) {
        this.value = value;
        next = nextNode;
    }
}