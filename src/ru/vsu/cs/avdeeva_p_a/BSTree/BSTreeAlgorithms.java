package ru.vsu.cs.avdeeva_p_a.BSTree;

import ru.vsu.cs.avdeeva_p_a.BinaryTree;

public class BSTreeAlgorithms {
    /**
     * Поиск TreeNode по значению в поддереве node
     *
     * @param node Узел дерева
     * @param value Значение для поиска
     * @return Узел, содержащий искомый элемент
     */
    public static <T extends Comparable<? super T>> BinaryTree.TreeNode<T> getNode(BinaryTree.TreeNode<T> node, T value) {
        if (node == null) {
            return null;
        }
        int cmp = node.getValue().compareTo(value);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return getNode(node.getLeft(), value);
        } else {
            return getNode(node.getRight(), value);
        }
    }

    /**
     * Поиск минимального TreeNode в поддереве node
     *
     * @param node Поддерево в котором надо искать минимальный элемент
     * @return Узел, содержащий минимальный элемент
     */
    public static <T extends Comparable<? super T>> BinaryTree.TreeNode<T> getMinNode(BinaryTree.TreeNode<T> node) {
        return (node == null || node.getLeft() == null) ? node : getMinNode(node.getLeft());
    }

    /**
     * Поиск максимального TreeNode в поддереве node
     *
     * @param node Узел дерева
     * @return Узел, содержащий максимальный элемент
     */
    public static <T extends Comparable<? super T>> BinaryTree.TreeNode<T> getMaxNode(BinaryTree.TreeNode<T> node) {
        return (node == null || node.getRight() == null) ? node : getMaxNode(node.getRight());
    }

    /**
     * Поиск TreeNode с наибольшим значением, меньшим или равным value, в
     * поддереве node
     *
     * @param node Узел дерева
     * @param value Параметр
     * @return Узел, содержащий искомый элемент
     */
    public static <T extends Comparable<? super T>> BinaryTree.TreeNode<T> getFloorNode(BinaryTree.TreeNode<T> node, T value) {
        if (node == null) {
            return null;
        }
        int cmp = value.compareTo(node.getValue());
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return getFloorNode(node.getLeft(), value);
        } else {
            BinaryTree.TreeNode<T> res = getFloorNode(node.getRight(), value);
            return (res != null) ? res : node;
        }
    }

    /**
     * Поиск TreeNode с наименьшим значением, большим или равным value, в
     * поддереве node
     *
     * @param node Узел дерева
     * @param value Параметр
     * @return Узел, содержащий искомый элемент
     */
    public static <T extends Comparable<? super T>> BinaryTree.TreeNode<T> getCeilingNode(BinaryTree.TreeNode<T> node, T value) {
        if (node == null) {
            return null;
        }
        int cmp = value.compareTo(node.getValue());
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return getCeilingNode(node.getRight(), value);
        } else {
            BinaryTree.TreeNode<T> res = getCeilingNode(node.getLeft(), value);
            return (res != null) ? res : node;
        }
    }
}