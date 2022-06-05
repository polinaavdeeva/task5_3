package ru.vsu.cs.avdeeva_p_a.demo;

import ru.vsu.cs.avdeeva_p_a.BSTree.BSTree;
import ru.vsu.cs.avdeeva_p_a.BSTree.SimpleBSTree;
import ru.vsu.cs.avdeeva_p_a.BinaryTree;
import ru.vsu.cs.avdeeva_p_a.BinaryTreeAlgorithms;
import ru.vsu.cs.avdeeva_p_a.BinaryTreePainter;
import ru.vsu.cs.avdeeva_p_a.SimpleBinaryTree;
import util.ArrayUtils;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class TreeDemoFrame extends JFrame {
    private JPanel panelMain;
    private JButton buttonPreOrderTraverse;
    private JTextArea textAreaSystemOut;
    private JTextField textFieldBracketNotationTree;
    private JButton buttonMakeTree;
    private JButton buttonMakeBSTree;
    private JSplitPane splitPaneMain;
    private JTextField textFieldValues;
    private JSpinner spinnerRandomCount;
    private JButton buttonRandomGenerate;
    private JButton buttonSortValues;
    private JButton buttonMakeBSTree2;
    private JTextField textFieldSingleValue;
    private JButton buttonAddValue;
    private JButton buttonRemoveValue;
    private JPanel panelPaintArea;
    private JButton buttonSaveImage;
    private JButton buttonToBracketNotation;
    private JCheckBox checkBoxTransparent;
    private JSpinner spinnerSingleValue;
    private JButton buttonDeleteNotLowerLeaf;

    private JMenuBar menuBarMain;
    private JPanel paintPanel = null;
    private JFileChooser fileChooserSave;

    BinaryTree<Integer> tree = new SimpleBinaryTree<>();


    public TreeDemoFrame() {
        this.setTitle("Двоичные деревья");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        splitPaneMain.setDividerLocation(0.5);
        splitPaneMain.setResizeWeight(1.0);
        splitPaneMain.setBorder(null);

        paintPanel = new JPanel() {
            private Dimension paintSize = new Dimension(0, 0);

            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                paintSize = BinaryTreePainter.paint(tree, gr);
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };
        JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
        panelPaintArea.add(paintJScrollPane);

        fileChooserSave = new JFileChooser();
        fileChooserSave.setCurrentDirectory(new File("./images"));
        FileFilter filter = new FileNameExtensionFilter("SVG images", "svg");
        fileChooserSave.addChoosableFileFilter(filter);
        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        spinnerRandomCount.setValue(30);
        spinnerSingleValue.setValue(10);

        buttonMakeTree.addActionListener(actionEvent -> {
            try {
                SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = tree;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonMakeBSTree.addActionListener(actionEvent -> {
            try {
                SimpleBSTree<Integer> tree = new SimpleBSTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
                this.tree = tree;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonRandomGenerate.addActionListener(actionEvent -> {
            int size = ((Integer) spinnerRandomCount.getValue()).intValue();
            int[] arr = ArrayUtils.createRandomIntArray(size, (size <= 50) ? 100 : 1000);
            textFieldValues.setText(ArrayUtils.toString(arr));
        });

        buttonSortValues.addActionListener(actionEvent -> {
            try {
                int[] arr = ArrayUtils.toIntArray(textFieldValues.getText());
                Arrays.sort(arr);
                textFieldValues.setText(ArrayUtils.toString(arr));
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonMakeBSTree2.addActionListener(actionEvent -> {
            try {
                makeBSTFromValues(new SimpleBSTree<>(Integer::parseInt));
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });


        buttonAddValue.addActionListener(actionEvent -> {
            if (!(tree instanceof BSTree)) {
                SwingUtils.showInfoMessageBox("Текущее дерево не является деревом поиска!");
                return;
            }
            try {
                int value = Integer.parseInt(spinnerSingleValue.getValue().toString());
                ((BSTree<Integer>) tree).put(value);
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonRemoveValue.addActionListener(actionEvent -> {
            if (!(tree instanceof BSTree)) {
                SwingUtils.showInfoMessageBox("Текущее дерево не является деревом поиска!");
                return;
            }
            try {
                int value = Integer.parseInt(spinnerSingleValue.getValue().toString());
                ((BSTree<Integer>) tree).remove(value);
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonDeleteNotLowerLeaf.addActionListener(actionEvent -> {
            try {
                SimpleBSTree aaa = new SimpleBSTree();
                aaa.removeNotLowerLevelElement((BSTree<Integer>) tree);
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }

        });

        buttonToBracketNotation.addActionListener(actionEvent -> {
            if (tree == null) {
                return;
            }
            textFieldBracketNotationTree.setText(tree.toBracketStr());
        });

        buttonSaveImage.addActionListener(actionEvent -> {
            if (tree == null) {
                return;
            }
            try {
                if (fileChooserSave.showSaveDialog(TreeDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooserSave.getSelectedFile().getPath();
                    if (!filename.toLowerCase().endsWith(".svg")) {
                        filename += ".svg";
                    }
                    BinaryTreePainter.saveIntoFile(tree, filename, checkBoxTransparent.isSelected());
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonPreOrderTraverse.addActionListener(actionEvent -> {
            showSystemOut(() -> {

                class InnerVisitor implements BinaryTreeAlgorithms.Visitor<Integer> {

                    @Override
                    public void visit(Integer value, int level) {

                        System.out.println(value + " (уровень " + level + ")");

                    }
                }
                BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), new InnerVisitor());

                System.out.println();
                SimpleBSTree a = new SimpleBSTree();
                System.out.println("Глубина дерева = " + a.removeNotLowerLevelElement((BSTree<Integer>) tree));
                System.out.println();
                System.out.println("Итератор:");
                for (Integer i : BinaryTreeAlgorithms.preOrderValues(tree.getRoot())) {
                    System.out.println(i);
                }
            });
        });
    }

    /**
     * Перерисовка дерева
     */
    public void repaintTree() {
        //panelPaintArea.repaint();
        paintPanel.repaint();
        //panelPaintArea.revalidate();
    }

    /**
     * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
     *
     * @param action Выполняемое действие
     */
    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }

    /**
     * Заполнить дерево добавлением всех элементов (textFieldValues)
     *
     * @param tree Дерево
     */
    private void makeBSTFromValues(BSTree<Integer> tree) {
        int[] values = ArrayUtils.toIntArray(textFieldValues.getText());
        tree.clear();
        for (int v : values) {
            tree.put(v);
        }
        this.tree = tree;
        repaintTree();
    }
}
