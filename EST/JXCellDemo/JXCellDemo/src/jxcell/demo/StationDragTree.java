package jxcell.demo;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class StationDragTree extends JTree implements DragGestureListener,
		DragSourceListener, DropTargetListener {

	private static final long serialVersionUID = -7123350256666099899L;

	BufferedImage ghostImage;

	private Rectangle2D ghostRect = new Rectangle2D.Float();

	private Point ptOffset = new Point();

	private Point lastPoint = new Point();

	private TreePath lastPath;

	private Timer hoverTimer;

	StationNode sourceNode;

	public StationDragTree() {
		DragSource dragSource = DragSource.getDefaultDragSource();

		dragSource.createDefaultDragGestureRecognizer(this, // component where
				// drag originates
				DnDConstants.ACTION_COPY_OR_MOVE, // actions
				this); // drag gesture recognizer
		setModel(createTreeModel());

		addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
			}

			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();

				if (path != null) {
					StationNode node = (StationNode) path
							.getLastPathComponent();

					DefaultTreeModel model = (DefaultTreeModel) getModel();
					model.nodeStructureChanged(node);
				}
			}

		});
		this.setCellRenderer(new DefaultTreeCellRenderer() {

			private static final long serialVersionUID = 7616415055309322562L;

			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {

				return super.getTreeCellRendererComponent(tree, value,
						selected, expanded, leaf, row, hasFocus);
			}

		});

		super.setScrollsOnExpand(true);
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

		// Set up a hover timer, so that a node will be automatically expanded
		// or collapsed
		// if the user lingers on it for more than a short time
		hoverTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lastPath == null) {
					return;
				}
				if (getRowForPath(lastPath) == 0)
					return; // Do nothing if we are hovering over the root node
				if (isExpanded(lastPath))
					collapsePath(lastPath);
				else
					expandPath(lastPath);
			}
		});
		hoverTimer.setRepeats(false); // Set timer to one-shot mode

	}

	public void dragGestureRecognized(DragGestureEvent e) {
		// drag anything …

		TreePath path = getLeadSelectionPath();
		if (path == null)
			return;
		StationNode node = (StationNode) path.getLastPathComponent();
		sourceNode = node;
		// Work out the offset of the drag point from the TreePath bounding
		// rectangle origin
		Rectangle raPath = getPathBounds(path);
		Point ptDragOrigin = e.getDragOrigin();
		ptOffset.setLocation(ptDragOrigin.x - raPath.x, ptDragOrigin.y
				- raPath.y);
		// Get the cell renderer (which is a JLabel) for the path being dragged
		int row = this.getRowForLocation(ptDragOrigin.x, ptDragOrigin.y);
		JLabel lbl = (JLabel) getCellRenderer().getTreeCellRendererComponent(
				this, // tree
				path.getLastPathComponent(), // value
				false, // isSelected (dont want a colored background)
				isExpanded(path), // isExpanded
				getModel().isLeaf(path.getLastPathComponent()), // isLeaf
				row, // row (not important for rendering)
				false // hasFocus (dont want a focus rectangle)
				);
		lbl.setSize((int) raPath.getWidth(), (int) raPath.getHeight()); // <-
		// The
		// layout
		// manager
		// would
		// normally
		// do
		// this

		// Get a buffered image of the selection for dragging a ghost image
		this.ghostImage = new BufferedImage((int) raPath.getWidth(),
				(int) raPath.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2 = ghostImage.createGraphics();

		// Ask the cell renderer to paint itself into the BufferedImage
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
		// Make the image ghostlike
		lbl.paint(g2);

		g2.dispose();
		// this.getGraphics().drawImage(ghostImage, e.getDragOrigin().x,
		// e.getDragOrigin().y, this);

		e.startDrag(
				null, // cursor
				ghostImage, new Point(5, 5),
				new StringSelection(getFilename()), // transferable
				this); // drag source listener
	}

	public void dragDropEnd(DragSourceDropEvent e) {
		ghostImage = null;
		sourceNode = null;
	}

	public void dragEnter(DragSourceDragEvent e) {
	}

	public void dragExit(DragSourceEvent e) {
		if (!DragSource.isDragImageSupported()) {
			repaint(ghostRect.getBounds());
		}
	}

	public void dragOver(DragSourceDragEvent e) {

	}

	public void dropActionChanged(DragSourceDragEvent e) {
	}

	public String getFilename() {
		TreePath path = getLeadSelectionPath();
		StationNode node = (StationNode) path.getLastPathComponent();
		return node.getName();
	}

	private DefaultTreeModel createTreeModel() {
		// File root = FileSystemView.getFileSystemView().getRoots()[0];
		StationNode rootNode = new StationNode();
		rootNode.setName("变电站");

		StationNode node1 = new StationNode();
		node1.setName("分公司名称");

		StationNode node11 = new StationNode();
		node11.setName("变电站名称");

		StationNode node12 = new StationNode();
		node12.setName("变电站等级");

		StationNode node2 = new StationNode();
		node2.setName("分公司地址");

		rootNode.add(node1);
		rootNode.add(node2);

		node1.add(node11);
		node1.add(node12);

		return new DefaultTreeModel(rootNode);
	}

	public void dragEnter(DropTargetDragEvent dtde) {

	}

	public void dragOver(DropTargetDragEvent dtde) {

		Point pt = dtde.getLocation();
		if (pt.equals(lastPoint)) {
			return;
		}
		if (ghostImage != null) {
			Graphics2D g2 = (Graphics2D) getGraphics();
			// If a drag image is not supported by the platform, then draw my
			// own drag image
			if (!DragSource.isDragImageSupported()) {
				paintImmediately(ghostRect.getBounds()); // Rub out the last
				// ghost image and cue
				// line
				// And remember where we are about to draw the new ghost image
				ghostRect.setRect(pt.x - ptOffset.x, pt.y - ptOffset.y,
						ghostImage.getWidth(), ghostImage.getHeight());
				g2.drawImage((ghostImage), AffineTransform
						.getTranslateInstance(ghostRect.getX(),
								ghostRect.getY()), null);
			}
		}
		TreePath path = getClosestPathForLocation(pt.x, pt.y);
		if (!(path == lastPath)) {
			lastPath = path;
			hoverTimer.restart();
		}
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	public void drop(DropTargetDropEvent e) {
		try {
			DataFlavor stringFlavor = DataFlavor.stringFlavor;
			Transferable tr = e.getTransferable();

			TreePath path = this.getPathForLocation(e.getLocation().x,
					e.getLocation().y);
			if (path == null) {
				e.rejectDrop();
				return;
			}
			if (e.isDataFlavorSupported(stringFlavor)) {
				String filename = (String) tr.getTransferData(stringFlavor);
				if (filename.endsWith("变电站名称")) {
					e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

					((StationNode) sourceNode.getParent()).remove(sourceNode);
					e.dropComplete(true);
					this.updateUI();

					System.err.println("接受拖动！！！！");

				} else {
					e.rejectDrop();
				}
			} else {
				e.rejectDrop();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		} finally {
			ghostImage = null;
			this.repaint();
		}
	}

	public void dragExit(DropTargetEvent dte) {

	}
}

class StationNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {

		return name;
	}

}
