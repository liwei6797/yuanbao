package jxcell.demo;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.jxcell.CellException;
import com.jxcell.CellRef;
import com.jxcell.SelectionChangedEvent;
import com.jxcell.SelectionChangedListener;
import com.jxcell.StartEditEvent;
import com.jxcell.StartEditListener;
import com.jxcell.View;
import com.jxcell.dialog.AboutDlg;

public class WorkSheetDemo extends JFrame implements StartEditListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private BorderLayout borderLayout1 = new BorderLayout();

    private JSplitPane splitPane;
    private JTree leftTree;
    private JPanel rightPanel;
    private JPanel currentPanel;

    private final int minimumWidth = 800;
    private final int minimumHeight = 500;

    private View centView;

    DefaultMutableTreeNode root, child, chosen;

    DefaultTreeModel model;

    String[][] data = { { "分公司名称", "变电站名称", "电压等级1", "电压等级2" },
            { "Flavors", "Tart", "Sweet", "Bland" },
            { "Length", "Short", "Medium", "Long" },
            { "Volume", "High", "Medium", "Low" },
            { "Temperature", "High", "Medium", "Low" },
            { "Intensity", "High", "Medium", "Low" } };

    // 单元格名称
    private TextField cellNameTxt;

    // 单元格名称
    private JComboBox textTypeComb;

    // 单元格名称
    private TextField contentTxt;

    private Button merButton;

    private Button btnTest;

    //缇/像素
    private int twipsPerPixel = 1440 / getDpi();

    public WorkSheetDemo() {
        initWindow();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        new DropTarget(centView, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {

                    @Override
                    public void dragEnter(DropTargetDragEvent dtde) {
                        // TODO Auto-generated method stub
                        centView.grabFocus();
                        centView.getSelectionCount();
                    }

                    @Override
                    public void dragOver(DropTargetDragEvent e) {
                        // System.out.println(e.getLocation());

                        CellRef cr = centView.twipsToRC(e.getLocation().x
                                * twipsPerPixel, e.getLocation().y
                                * twipsPerPixel);
                        System.err.println("row = " + cr.getRow() + ",cel = "
                                + cr.getCol());
                        try {
                            centView.setActiveCell(cr.getRow(), cr.getCol());
                        } catch (CellException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void drop(DropTargetDropEvent e) {
                        try {
                            DataFlavor stringFlavor = DataFlavor.stringFlavor;
                            Transferable tr = e.getTransferable();

                            if (e.isDataFlavorSupported(stringFlavor)) {
                                String filename = (String) tr
                                        .getTransferData(stringFlavor);
                                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

                                try {
                                    System.err.println(e.getLocation());
                                    CellRef cr = centView.twipsToRC(
                                            e.getLocation().x * twipsPerPixel,
                                            e.getLocation().y * twipsPerPixel);
                                    System.err.println("row = " + cr.getRow()
                                            + ",cel = " + cr.getCol());
                                    centView.setActiveCell(cr.getRow(),
                                            cr.getCol());

                                    System.out.println("A1.colWidth = "
                                            + centView.getColWidth(1)
                                            + ",A1.colwidth2 ="
                                            + centView.getColWidthTwips(1));

                                    // centView.addPageBreak(); //新增水平线
                                    // centView.addSelection(0, 0, 5, 5) ;
                                    // //选择一个区域
                                    // centView.cancelEdit(); -- 实验无效果
                                    System.err.println(centView
                                            .charWidthToTwips(e.getLocation().x));

                                    centView.grabFocus();
                                    centView.setText(filename);

                                } catch (CellException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                e.dropComplete(true);

                            } else {
                                e.rejectDrop();
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } catch (UnsupportedFlavorException ufe) {
                            ufe.printStackTrace();
                        }
                    }
                });
        try {
            centView.setShowEditBar(true);
            centView.setShowRowHeading(true);
            centView.setShowColHeading(true);
            centView.setShowTabs((short) 2);
            centView.addStartEditListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * view加上鼠标移动事件
         */
        centView.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // System.out.println("x:" + e.getX() + " y:" + e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // System.out.println("x1:" + e.getX() + " y1:" + e.getY());
            }
        });

        centView.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            /**
             * 鼠标进入时获取焦点
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                centView.grabFocus();

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initWindow() {
        // this.getContentPane().setLayout(borderLayout1);
        //
        // this.setSize(new Dimension(1170, 730));
        // this.setTitle("WorkSheet Demo");
        //
        //
        // centerPanel = new JPanel();
        // centerPanel.setLayout(null);
        // this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        //
        // JLabel jLabel = new JLabel();
        // jLabel.setText("操作工具栏加密，无法引入");
        // jLabel.setBounds(200, 0, 756, 50);
        // centerPanel.add(jLabel);
        //
        // JLabel jLabel2 = new JLabel();
        // jLabel2.setText("左边树操作");
        // jLabel2.setBounds(20, 20, 200, 600);
        // centerPanel.add(jLabel2);
        //
        // centView = new View();
        // centView.setBounds(new Rectangle(200, 50, 756, 600));
        // centerPanel.add(centView);

        this.setSize(new Dimension(1170, 730));
        this.setTitle("WorkSheet Demo");
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(160);
        splitPane.setPreferredSize(new Dimension(this.minimumWidth,
                this.minimumHeight));

        root = new DefaultMutableTreeNode("变电站");
        // leftTree = new JTree(root);

        leftTree = new StationDragTree();
        model = (DefaultTreeModel) leftTree.getModel();

        child = new Branch(data[0]).node();
        // 生成子节点
        chosen = (DefaultMutableTreeNode)
        // 选择child的父节点
        leftTree.getLastSelectedPathComponent();
        if (chosen == null)
            chosen = root;
        model.insertNodeInto(child, chosen, 0);

        splitPane.add(leftTree, JSplitPane.LEFT);

        rightPanel = new JPanel();
        rightPanel.setLayout(null);

        centView = new View();

        centView.setBounds(new Rectangle(10, 50, 756, 600));

        rightPanel.add(centView);

        splitPane.add(rightPanel, JSplitPane.RIGHT);

        // 最右边显示单元格部分

        Label label = new Label();
        label.setBounds(780, 45, 61, 34);
        label.setText("单元格");

        cellNameTxt = new TextField();
        cellNameTxt.setBounds(850, 50, 120, 20);

        Label label2 = new Label();
        label2.setBounds(780, 82, 61, 34);
        label2.setText("插入数据");

        textTypeComb = new JComboBox();
        textTypeComb.addItem("文本");
        textTypeComb.addItem("数字");
        textTypeComb.setBounds(850, 82, 120, 20);

        rightPanel.add(label);
        rightPanel.add(cellNameTxt);

        rightPanel.add(label2);
        rightPanel.add(textTypeComb);

        contentTxt = new TextField();
        contentTxt.setBounds(780, 117, 180, 20);

        merButton = new Button("合并单元格");
        merButton.setBounds(780, 140, 180, 20);

        merButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // System.out.println( centView.getMinRow() );
                    // System.out.println( centView.getLastRow() );
                    //
                    // System.out.println( centView.getMinCol() );
                    // System.out.println( centView.getLastCol() );
                    // System.out.println(centView.getFormula(1, 1));
                    // centView.setActiveCell(1, 1);

                    double[][] test = null;
                    centView.copyDataToArray(0, 0, 0, centView.getLastRow(),
                            centView.getLastCol(), test);
                    System.out.println("test =" + test);
                    
                    
                    //centView.getMergedCells()

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });

        rightPanel.add(contentTxt);
        rightPanel.add(merButton);

        btnTest = new Button("test");
        btnTest.setBounds(780, 180, 180, 20);
        rightPanel.add(btnTest);

        btnTest.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    centView.setActiveCell(0, 0);
                    System.out.println("getCol():" + centView.getCol());
                    System.out.println("getHeaderHeight():"
                            + centView.getHeaderHeight());
                    System.out.println("getHeaderWidth():"
                            + centView.getHeaderWidth());

                    // System.out.println("getColWidth(-1):"
                    // + centView.getColWidth(-1));
                    System.out.println("getColWidth(0):"
                            + centView.getColWidth(0));
                    System.out.println("getColWidthTwips(0):"
                            + centView.getColWidthTwips(0));
                    System.out.println("getColWidthUnits():"
                            + centView.getColWidthUnits());
                    getDpi();
                } catch (CellException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });

        contentTxt.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    centView.setText(contentTxt.getText());
                } catch (CellException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        this.add(splitPane);
        cellQualit();
        drag();

    }

    private int getDpi() {
        // Dimension screen =
        // java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        // System.out.println("screen width: "+screen.getWidth());
        // System.out.println("screen height: "+screen.getHeight());
        int pixelPerInch = java.awt.Toolkit.getDefaultToolkit()
                .getScreenResolution();
        System.out.println("pixelPerInch: " + pixelPerInch);
        return pixelPerInch;

        // 屏幕尺寸
        // double height=screen.getHeight()/pixelPerInch;
        // double width=screen.getWidth()/pixelPerInch;
        // double x=Math.pow(height,2);
        // double y=Math.pow(width,2);

    }

    private void cellQualit() {
        centView.addSelectionChangedListener(new SelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
                cellNameTxt.setText(centView.getSelectionLocal());
            }
        });

    }

    private void createLeftTreePanel() {

        JLabel jLabel = new JLabel();
        jLabel.setText("左边树操作");
        jLabel.setBounds(20, 20, 200, 600);
        // leftTreePanel.add(jLabel);
    }

    public static void main(String[] args) {
        WorkSheetDemo workSheetDemo = new WorkSheetDemo();
        workSheetDemo.validate();
        workSheetDemo.setVisible(true);

    }

    public void drag() {

        DragSource dragSource = DragSource.getDefaultDragSource();

        dragSource.createDefaultDragGestureRecognizer(leftTree,
                DnDConstants.ACTION_COPY_OR_MOVE, new DragGestureListener() {

                    @Override
                    public void dragGestureRecognized(DragGestureEvent dge) {

                    }
                });
    }

    /**
     * 弹出窗口 需要鼠标进入时获取焦点
     */
    @Override
    public void startEdit(StartEditEvent ev) {
        View v = (View) ev.getSource();
        v.cancelEdit();
        AboutDlg d = new AboutDlg(v);
        d.show();
        try {
            v.setText("hello");
        } catch (CellException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
