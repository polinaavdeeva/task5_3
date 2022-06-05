package ru.vsu.cs.avdeeva_p_a;

import java.util.Iterator;
import java.util.Stack;

public class BinaryTreeAlgorithms {

    @FunctionalInterface
    public interface Visitor<T> {
        /**
         * "Посещение" значения
         *
         * @param value Значение, которое "посещаем"
         * @param level Уровень дерева/поддерева, на котором находится данное значение
         */
        void visit(T value, int level);
    }


    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в прямом/NLR порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor Посетитель
     */
    public static <T> void preOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            void preOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                if (node == null) {
                    return;
                }
                visitor.visit(node.getValue(), level);
                preOrderVisit(node.getLeft(), visitor, level + 1);
                preOrderVisit(node.getRight(), visitor, level + 1);
            }
        }
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().preOrderVisit(treeNode, visitor, 0);
    }

    /**
     * Обход поддерева с вершиной в данном узле
     * в виде итератора в прямом/NLR порядке
     * (предполагается, что в процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> preOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            stack.push(treeNode);

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return stack.size() > 0;
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop();
                    if (node.getRight() != null) {
                        stack.push(node.getRight());
                    }
                    if (node.getLeft() != null) {
                        stack.push(node.getLeft());
                    }
                    return node.getValue();
                }

            };
        };
    }

    /**
     * Обход поддерева с вершиной в данном узле в виде итератора в
     * симметричном/поперечном/центрированном/LNR порядке (предполагается, что в
     * процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> inOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            BinaryTree.TreeNode<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop();
                    T result = node.getValue();
                    if (node.getRight() != null) {
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node);
                            node = node.getLeft();
                        }
                    }
                    return result;
                }
            };
        };
    }

    /**
     * Представление дерева в виде строки в скобочной нотации
     *
     * @param treeNode Узел поддерева, которое требуется представить в виже скобочной нотации
     * @return дерево в виде строки
     */
    public static <T> String toBracketStr(BinaryTree.TreeNode<T> treeNode) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 2-мя параметрами)
        class Inner {
            void printTo(BinaryTree.TreeNode<T> node, StringBuilder sb) {
                if (node == null) {
                    return;
                }
                sb.append(node.getValue());
                if (node.getLeft() != null || node.getRight() != null) {
                    sb.append(" (");
                    printTo(node.getLeft(), sb);
                    if (node.getRight() != null) {
                        sb.append(", ");
                        printTo(node.getRight(), sb);
                    }
                    sb.append(")");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().printTo(treeNode, sb);

        return sb.toString();
    }
}