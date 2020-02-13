package com.ztq.sdk.data_structure_and_algorithm;

/**
 * 二叉树节点，包含指向父节点的节点
 * @param <T>
 */
public class BinaryTreeNodeWithParentPointer<T> {
    private T value;
    private BinaryTreeNodeWithParentPointer<T> leftNode;
    private BinaryTreeNodeWithParentPointer<T> rightNode;
    private BinaryTreeNodeWithParentPointer<T> parentNode;

    public BinaryTreeNodeWithParentPointer(T value) {
        this.value = value;
    }

    public BinaryTreeNodeWithParentPointer<T> getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(BinaryTreeNodeWithParentPointer<T> leftNode) {
        this.leftNode = leftNode;
    }

    public BinaryTreeNodeWithParentPointer<T> getRightNode() {
        return rightNode;
    }

    public void setRightNode(BinaryTreeNodeWithParentPointer<T> rightNode) {
        this.rightNode = rightNode;
    }

    public BinaryTreeNodeWithParentPointer<T> getParentNode() {
        return parentNode;
    }

    public void setParentNode(BinaryTreeNodeWithParentPointer<T> parentNode) {
        this.parentNode = parentNode;
    }
}
