package com.ztq.sdk.data_structure_and_algorithm;

/**
 * 二叉树的节点类
 */
public class BinaryTreeNode<T> {
    private T value;
    private BinaryTreeNode<T> leftNode;
    private BinaryTreeNode<T> rightNode;

    public BinaryTreeNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public BinaryTreeNode<T> getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(BinaryTreeNode<T> leftNode) {
        this.leftNode = leftNode;
    }

    public BinaryTreeNode<T> getRightNode() {
        return rightNode;
    }

    public void setRightNode(BinaryTreeNode<T> rightNode) {
        this.rightNode = rightNode;
    }
}