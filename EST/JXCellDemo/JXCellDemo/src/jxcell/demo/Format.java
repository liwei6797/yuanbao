package jxcell.demo;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jxcell.CellFormat;
import com.jxcell.View;

public class Format extends JFrame implements java.awt.event.ActionListener {

	CellFormat originalCellFormat;

	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JButton btnSimple = new JButton();
	JButton btnClassic1 = new JButton();
	JButton btnClassic2 = new JButton();
	JButton btnClassic3 = new JButton();
	JButton btnAcct1 = new JButton();
	JButton btnAcct2 = new JButton();
	JButton btnAcct3 = new JButton();
	JButton btnColor1 = new JButton();
	JButton btnColor2 = new JButton();
	JButton btnColor3 = new JButton();
	JButton btnList1 = new JButton();
	JButton btnList2 = new JButton();
	JButton btnList3 = new JButton();
	JButton btn3DEffect1 = new JButton();

	View m_view = new View();

	// Construct the frame
	public Format() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
			m_view.setShowEditBar(false);
			m_view.setShowRowHeading(false);
			m_view.setShowColHeading(false);
			m_view.setShowTabs((short) 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void main(String args[]) {

		Format frame = new Format();
		frame.validate();
		frame.setVisible(true);
	}

	// Component initialization
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(borderLayout1);
		this.setSize(new Dimension(522, 493));
		this.setTitle("Format Demo");
		btnSimple.setText("Simple");
		btnSimple.setBounds(new Rectangle(6, 3, 111, 35));
		btnSimple.addActionListener(this);

		btnClassic1.setText("Classic 1");
		btnClassic1.setBounds(new Rectangle(136, 3, 112, 35));
		btnClassic1.addActionListener(this);

		btnClassic2.setText("Classic 2");
		btnClassic2.setBounds(new Rectangle(265, 3, 113, 35));
		btnClassic2.addActionListener(this);

		btnClassic3.setPreferredSize(new Dimension(100, 27));
		btnClassic3.setText("Classic 3");
		btnClassic3.setBounds(new Rectangle(397, 2, 113, 35));
		btnClassic3.addActionListener(this);

		btnAcct1.setPreferredSize(new Dimension(111, 27));
		btnAcct1.setText("Accounting 1");
		btnAcct1.setBounds(new Rectangle(6, 45, 111, 35));
		btnAcct1.addActionListener(this);

		btnAcct2.setText("Accounting 2");
		btnAcct2.setBounds(new Rectangle(137, 45, 112, 35));
		btnAcct2.addActionListener(this);

		btnAcct3.setText("Accounting 3");
		btnAcct3.setBounds(new Rectangle(267, 44, 113, 35));
		btnAcct3.addActionListener(this);

		btnColor1.setText("Color 1");
		btnColor1.setBounds(new Rectangle(398, 47, 113, 35));
		btnColor1.addActionListener(this);

		btnColor2.setText("Color 2");
		btnColor2.setBounds(new Rectangle(6, 86, 113, 35));
		btnColor2.addActionListener(this);

		btn3DEffect1.setText("3D Effects");
		btn3DEffect1.setBounds(new Rectangle(267, 130, 112, 35));
		btn3DEffect1.addActionListener(this);

		btnColor3.setMinimumSize(new Dimension(111, 27));
		btnColor3.setPreferredSize(new Dimension(111, 27));
		btnColor3.setText("Color 3");
		btnColor3.setBounds(new Rectangle(138, 87, 111, 35));
		btnColor3.addActionListener(this);

		btnList1.setText("List 1");
		btnList1.setBounds(new Rectangle(267, 88, 113, 35));
		btnList1.addActionListener(this);

		btnList2.setText("List 2");
		btnList2.setBounds(new Rectangle(399, 92, 112, 35));
		btnList2.addActionListener(this);

		btnList3.setText("List 3");
		btnList3.setBounds(new Rectangle(138, 129, 113, 35));
		btnList3.addActionListener(this);

		jPanel1.setLayout(null);
		m_view.setBounds(new Rectangle(4, 173, 507, 293));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);

		jPanel1.add(m_view, null);
		jPanel1.add(btnSimple, null);
		jPanel1.add(btnAcct1, null);
		jPanel1.add(btnColor2, null);
		jPanel1.add(btnClassic1, null);
		jPanel1.add(btnAcct2, null);
		jPanel1.add(btnColor3, null);
		jPanel1.add(btnList3, null);
		jPanel1.add(btnClassic3, null);
		jPanel1.add(btnList1, null);
		jPanel1.add(btnClassic2, null);
		jPanel1.add(btnAcct3, null);
		jPanel1.add(btn3DEffect1, null);
		jPanel1.add(btnColor1, null);
		jPanel1.add(btnList2, null);

	}

	// Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		Object buttonObj = e.getSource();
		if (buttonObj == btnSimple) {
			btnSimple_actionPerformed();
		} else if (buttonObj == btnClassic1) {
			btnClassic1_actionPerformed();
		} else if (buttonObj == btnClassic2) {
			btnClassic2_actionPerformed();
		} else if (buttonObj == btnClassic3) {
			btnClassic3_actionPerformed();
		} else if (buttonObj == btnAcct1) {
			btnAcct1_actionPerformed();
		} else if (buttonObj == btnAcct2) {
			btnAcct2_actionPerformed();
		} else if (buttonObj == btnAcct3) {
			btnAcct3_actionPerformed();
		} else if (buttonObj == btn3DEffect1) {
			btn3DEffect1_actionPerformed();
		} else if (buttonObj == btnColor1) {
			btnColor1_actionPerformed();
		} else if (buttonObj == btnColor2) {
			btnColor2_actionPerformed();
		} else if (buttonObj == btnColor3) {
			btnColor3_actionPerformed();
		} else if (buttonObj == btnList1) {
			btnList1_actionPerformed();
		} else if (buttonObj == btnList2) {
			btnList2_actionPerformed();
		} else if (buttonObj == btnList3) {
			btnList3_actionPerformed();
		}
	}

	public void btnSimple_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.SIMPLE);
	}

	public void btnClassic1_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.CLASSIC1);
	}

	public void btnClassic2_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.CLASSIC2);
	}

	public void btnClassic3_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.CLASSIC3);
	}

	public void btnAcct1_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.ACCOUNTING1);
	}

	public void btnAcct2_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.ACCOUNTING2);
	}

	public void btnAcct3_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.ACCOUNTING3);
	}

	public void btn3DEffect1_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.EFFECTS3D1);
	}

	public void btnColor1_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.COLORFUL1);
	}

	public void btnColor2_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.COLORFUL2);
	}

	public void btnColor3_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.COLORFUL3);
	}

	public void btnList1_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.LIST1);
	}

	public void btnList2_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.LIST2);
	}

	public void btnList3_actionPerformed() {
		CellFrmt cFrmt = new CellFrmt(m_view);
		cFrmt.FormatCells(cFrmt.LIST3);
	}
}
