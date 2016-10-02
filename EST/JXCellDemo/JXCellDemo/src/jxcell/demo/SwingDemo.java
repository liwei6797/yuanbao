package jxcell.demo;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jxcell.CellFormat;
import com.jxcell.ChartShape;
import com.jxcell.View;

public class SwingDemo extends JFrame implements java.awt.event.ActionListener {
	BorderLayout borderLayout1 = new BorderLayout();
	GridLayout gridLayout1 = new GridLayout(1, 4, 15, 15);
	View m_view = new View();

	public SwingDemo() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void main(String args[]) {
		SwingDemo frame = new SwingDemo();
		frame.validate();
		frame.setVisible(true);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(borderLayout1);
		this.setSize(new Dimension(522, 493));
		this.setTitle("Spreadsheet Component Demo");
		m_view.setShowEditBar(true);
		m_view.setTextAsValue(1, 2, "Jan");
		m_view.setTextAsValue(1, 3, "Feb");
		m_view.setTextAsValue(1, 4, "Mar");
		m_view.setTextAsValue(1, 5, "Apr");
		m_view.setTextAsValue(2, 1, "Bananas");
		m_view.setTextAsValue(3, 1, "Papaya");
		m_view.setTextAsValue(4, 1, "Mango");
		m_view.setTextAsValue(5, 1, "Lilikoi");
		m_view.setTextAsValue(6, 1, "Comfrey");
		m_view.setTextAsValue(7, 1, "Total");
		for (int col = 2; col <= 5; col++)
			for (int row = 2; row <= 7; row++)
				m_view.setFormula(row, col, "RAND()");
		m_view.setFormula(7, 2, "SUM(C3:C7)");
		m_view.setSelection("C8:F8");
		m_view.editCopyRight();
		m_view.recalc();
		ChartShape chart = m_view.addChart(1, 8, 7, 22);
		chart.setTitle("Sample Chart");
		chart.setChartType(ChartShape.TypeColumn);
		chart.setLinkRange("B2:F8", true);

		m_view.setActiveCell(0, 0);
		m_view.setText(0, 0, "Hello,you are welcome!");
		this.getContentPane().add(m_view, BorderLayout.CENTER);

		m_view.setAllowCellTextDlg(true);

		CellFormat cellfmt = m_view.getCellFormat(0, 0, 2, 12);// A1:C13
		cellfmt.setFontName("Courier New");
		cellfmt.setFontSize(12);
		m_view.setCellFormat(cellfmt);

		m_view.setTextAntialiasing(true);
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
