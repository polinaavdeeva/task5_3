package ru.vsu.cs.avdeeva_p_a.BSTree;

import java.util.function.Function;
import ru.vsu.cs.avdeeva_p_a.BinaryTree;
import ru.vsu.cs.avdeeva_p_a.BinaryTreeAlgorithms;
import ru.vsu.cs.avdeeva_p_a.SimpleBinaryTree;

/**
 * Класс, реализующий простое (наивное) дерево поиска
 *
 * @param <T>
 */

public class SimpleBSTree<T extends Comparable<? super T>> extends SimpleBinaryTree<T> implements BSTree<T> {

    private static class CheckBSTResult<T> {
        public boolean result;
        public int size;
        public T min;
        public T max;

        public CheckBSTResult(boolean result, int size, T min, T max) {
            this.result = result;
            this.size = size;
            this.min = min;
            this.max = max;
        }
    }

    int size = 0;

    public SimpleBSTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        super(fromStrFunc, toStrFunc);
    }

    public SimpleBSTree(Function<String, T> fromStrFunc) {
        super(fromStrFunc);
    }

    public SimpleBSTree() {
        super();
    }

    private static <T extends Comparable<? super T>> CheckBSTResult<T> isBSTInner(BinaryTree.TreeNode<T> node) {
        if (node == null) {
            return null;
        }

        CheckBSTResult<T> leftResult = isBSTInner(node.getLeft());
        CheckBSTResult<T> rightResult = isBSTInner(node.getRight());
        CheckBSTResult<T> result = new CheckBSTResult<>(true, 1, node.getValue(), node.getValue());
        if (leftResult != null) {
            result.result &= leftResult.result;
            result.result &= leftResult.max.compareTo(node.getValue()) < 0;
            result.size += leftResult.size;
            result.min = leftResult.min;
        }
        if (rightResult != null) {
            result.result &= rightResult.result;
            result.size += rightResult.size;
            result.result &= rightResult.min.compareTo(node.getValue()) > 0;
            result.max = rightResult.max;
        }
        return result;
    }

    public static <T extends Comparable<? super T>> boolean isBST(BinaryTree.TreeNode<T> node) {
        return node == null ? true : isBSTInner(node).result;
    }

    @Override
    public void fromBracketNotation(String bracketStr) throws Exception {
        SimpleBinaryTree tempTree = new SimpleBinaryTree(this.fromStrFunc);
        tempTree.fromBracketNotation(bracketStr);
        CheckBSTResult<T> tempTreeResult = isBSTInner(tempTree.getRoot());
        if (!tempTreeResult.result) {
            throw new Exception("Заданное дерево не является деревом поиска!");
        }
        super.fromBracketNotation(bracketStr);
        this.size = tempTreeResult.size;
    }

    private T put(SimpleTreeNode node, T value) {
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value
            T oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {
            if (cmp < 0) {
                if (node.left == null) {
                    node.left = new SimpleTreeNode(value);
                    size++;
                    return null;
                } else {
                    return put(node.left, value);
                }
            } else {
                if (node.right == null) {
                    node.right = new SimpleTreeNode(value);
                    size++;
                    return null;
                } else {
                    return put(node.right, value);
                }
            }
        }
    }

    private T remove(SimpleTreeNode node, SimpleTreeNode nodeParent, T value) {
        if (node == null) {
            return null;
        }
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value
            T oldValue = node.value;
            if (node.left != null && node.right != null) {
                // если у node есть и левое и правое поддерево
                SimpleTreeNode minParent = getMinNodeParent(node.right);
                if (minParent == null) {
                    node.value = node.right.value;
                    node.right = node.right.right;
                } else {
                    node.value = minParent.left.value;
                    minParent.left = minParent.left.right;
                }
            } else {
                SimpleTreeNode child = (node.left != null) ? node.left : node.right;
                if (nodeParent == null) {
                    // возможно, если только node == root
                    root = child;
                } else if (nodeParent.left == node) {
                    nodeParent.left = child;
                } else {
                    nodeParent.right = child;
                }
            }
            size--;
            return oldValue;
        } else if (cmp < 0)
            return remove(node.left, node, value);
        else {
            return remove(node.right, node, value);
        }
    }

    private int findLowerLevel(BSTree<Integer> tree) {
        class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {
            int lowerLevel1 = 0;

            @Override
            public void visit(Integer value, int level) {
                if (level > lowerLevel1) {
                    lowerLevel1 = level;
                }
            }
        }
        InnerVisitor vis = new InnerVisitor();
        BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), vis);
        return vis.lowerLevel1;
    }

    public int removeNotLowerLevelElement (BSTree<Integer> tree) {
        class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {
            final int lowerLevel = findLowerLevel(tree);

            @Override
            public void visit(Integer value, int level) {
                if ((level < lowerLevel) && (tree.getNode(value).getRight() == null) && (tree.getNode(value).getLeft() ==  null)) {
                    tree.remove(value);
                }
            }
        }
        InnerVisitor vis = new InnerVisitor();
        BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), vis);
        return vis.lowerLevel;
    }

    private SimpleTreeNode getMinNodeParent(SimpleTreeNode node) {
        if (node == null) {
            return null;
        }
        SimpleTreeNode parent = null;
        for (; node.left != null; node = node.left) {
            parent = node;
        }
        return parent;
    }

    @Override
    public T put(T value) {
        if (root == null) {
            root = new SimpleBinaryTree.SimpleTreeNode(value);
            size++;
            return null;
        }
        return put(root, value);
    }

    @Override
    public T remove(T value) {
        return remove(root, null, value);
    }


    @Override
    public int size() {
        return size;
    }
}