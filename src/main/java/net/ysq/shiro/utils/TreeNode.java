package net.ysq.shiro.utils;

import java.util.List;

/**
 * 树节点
 * @param <T>   parentId的类型
 */
public class TreeNode<T> {

    private T id;
    private T parentId;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T id) {
        this.id = id;
    }

    public TreeNode(T id, T parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public T getParentId() {
        return parentId;
    }

    public void setParentId(T parentId) {
        this.parentId = parentId;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }
}
