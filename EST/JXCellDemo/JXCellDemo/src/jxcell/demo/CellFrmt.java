package jxcell.demo;

import java.awt.Color;

import com.jxcell.CellFormat;
import com.jxcell.RangeRef;
import com.jxcell.View;

public class CellFrmt {
	public final int SIMPLE = 0;
	public final int CLASSIC1 = 1;
	public final int CLASSIC2 = 2;
	public final int CLASSIC3 = 3;
	public final int ACCOUNTING1 = 4;
	public final int ACCOUNTING2 = 5;
	public final int ACCOUNTING3 = 6;
	public final int LIST1 = 7;
	public final int LIST2 = 8;
	public final int LIST3 = 9;
	public final int COLORFUL1 = 10;
	public final int COLORFUL2 = 11;
	public final int COLORFUL3 = 12;
	public final int EFFECTS3D1 = 13;

	View m_view;
	CellFormat cfmt;

	CellFrmt(View view1) {
		m_view = view1;

		try {
			m_view.setSelection(1, 1, 7, 5);
		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}

	String hdrRange = null;
	String ftrRange = null, colRange = null, bodyRange = null, eRange,
			StartRange;
	RangeRef savedSelection = null;

	// FrmtType -- format type ie Simple, Accounting etc.
	public void FormatCells(int FrmtType) {

		int StartRow = 0, StartCol = 0, EndRow = 0, EndCol = 0;
		String fileName, dirName;

		dirName = System.getProperty("user.dir");
		fileName = "book.xls";

		try {
			m_view.read(dirName + "\\" + fileName);
			m_view.setSelection(1, 1, 7, 5);
			m_view.setShowGridLines(false);
			m_view.setShowEditBar(false);
			m_view.setShowRowHeading(false);
			m_view.setShowColHeading(false);
			m_view.setShowTabs((short) 0);
			cfmt = m_view.getCellFormat();
			m_view.editClear(View.ClearFormats);

			savedSelection = m_view.getSelection();

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		} catch (java.io.IOException io) {
			System.out.println(io.getMessage());
		}

		try {
			StartRow = m_view.getSelStartRow();
			StartCol = m_view.getSelStartCol();
			EndRow = m_view.getSelEndRow();
			EndCol = m_view.getSelEndCol();
			StartRange = m_view.formatRCNr(StartRow, StartCol, false);
			eRange = m_view.formatRCNr(StartRow, EndCol, false);
			hdrRange = StartRange + ":" + eRange;

			eRange = m_view.formatRCNr(EndRow, StartCol, false);
			colRange = StartRange + ":" + eRange;

			StartRange = m_view.formatRCNr(EndRow, StartCol, false);
			eRange = m_view.formatRCNr(EndRow, EndCol, false);
			ftrRange = StartRange + ":" + eRange;

			StartRange = m_view.formatRCNr(StartRow + 1, StartCol + 1, false);
			eRange = m_view.formatRCNr(EndRow - 1, EndCol, false);
			bodyRange = StartRange + ":" + eRange;

			m_view.setSelection(hdrRange);
			cfmt.setTopBorder(CellFormat.BorderMedium);
			cfmt.setBottomBorder(CellFormat.BorderMedium);
			AdjustFont(java.awt.Color.black.getRGB(), true, false, false, cfmt);
			cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
			cfmt.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);

			m_view.setCellFormat(cfmt);
			// m_view.update();

			m_view.setSelection(ftrRange);
			cfmt.setBottomBorder(CellFormat.BorderMedium);
			cfmt.setTopBorder(CellFormat.BorderThin);
			m_view.setCellFormat(cfmt);
			// m_view.update();

		} catch (com.jxcell.CellException e) {
			System.out.println("Can't set border " + e.getMessage());
		}

		if (FrmtType == SIMPLE) {
			try {
				m_view.setSelection(colRange);
				m_view.setCellFormat(cfmt);

				m_view.setSelection(hdrRange);
				cfmt.setTopBorder(CellFormat.BorderMedium);
				cfmt.setBottomBorder(CellFormat.BorderMedium);
				AdjustFont(java.awt.Color.black.getRGB(), true, false, false,
						cfmt);
				cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
				cfmt.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
				m_view.setCellFormat(cfmt);
				// m_view.update();

				m_view.setSelection(ftrRange);
				cfmt.setBottomBorder(CellFormat.BorderMedium);
				cfmt.setTopBorder(CellFormat.BorderThin);
				m_view.setCellFormat(cfmt);
				// m_view.update();

			} catch (com.jxcell.CellException e) {
				System.out.println("Can't set border " + e.getMessage());
			}
		} else if (FrmtType == CLASSIC1) {

			try {

				m_view.setSelection(colRange);
				cfmt.setTopBorder(CellFormat.BorderNone);
				cfmt.setBottomBorder(CellFormat.BorderNone);
				cfmt.setRightBorder(CellFormat.BorderThin);
				m_view.setCellFormat(cfmt);
				// m_view.update();

				cfmt.setRightBorder(CellFormat.BorderNone);
				m_view.setSelection(hdrRange);
				cfmt.setTopBorder(CellFormat.BorderMedium);
				cfmt.setBottomBorder(CellFormat.BorderThin);
				AdjustFont(java.awt.Color.black.getRGB(), false, true, false,
						cfmt);
				cfmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
				cfmt.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
				m_view.setCellFormat(cfmt);
				// m_view.update();

				m_view.setSelection(ftrRange);
				cfmt.setTopBorder(CellFormat.BorderThin);
				cfmt.setBottomBorder(CellFormat.BorderMedium);
				m_view.setCellFormat(cfmt);
				// m_view.update();

			} catch (com.jxcell.CellException e) {
				System.out.println(e.getMessage());
			}

		} else if (FrmtType == CLASSIC2) {
			Classic2(cfmt);
		} else if (FrmtType == CLASSIC3) {
			Classic3(cfmt);
		} else if (FrmtType == ACCOUNTING1) {
			Accounting1(cfmt);
		} else if (FrmtType == ACCOUNTING2) {
			Accounting2(cfmt);
		} else if (FrmtType == ACCOUNTING3) {
			Accounting3(cfmt);
		} else if (FrmtType == LIST1) {
			List1(cfmt);
		} else if (FrmtType == LIST2) {
			List2(cfmt);
		} else if (FrmtType == LIST3) {
			List3(cfmt);
		} else if (FrmtType == COLORFUL1) {
			Colorful1(cfmt);
		} else if (FrmtType == COLORFUL2) {
			Colorful2(cfmt);
		} else if (FrmtType == COLORFUL3) {
			Colorful3(cfmt);
		}

		else if (FrmtType == EFFECTS3D1) {
			Effects3D1(cfmt);
		}

//		m_view.setRepaint(true);
		m_view.repaint();

	}// FormatCells

	private void Classic2(CellFormat cellFmt) {

		short nPattern;
		try {

			m_view.setSelection(colRange);
			AdjustFont(java.awt.Color.black.getRGB(), true, false, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorder(CellFormat.BorderThin);
			AdjustFont(java.awt.Color.white.getRGB(), false, false, false,
					cellFmt);
			nPattern = 1;
			cellFmt.setPattern(nPattern);
			cellFmt.setPatternFG(java.awt.Color.magenta);
			cellFmt.setPatternBG(java.awt.Color.magenta);
			cellFmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
			cellFmt.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(ftrRange);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			cellFmt.setTopBorder(CellFormat.BorderThin);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}

	} // Classic2

	private void Classic3(CellFormat cellFmt) {

		try {
			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			AdjustFont(java.awt.Color.white.getRGB(), true, true, false,
					cellFmt);
			AlignRight(cellFmt);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(11).getRGB(),
					java.awt.Color.black.getRGB());
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(ftrRange);
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(15).getRGB(),
					java.awt.Color.black.getRGB());
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(bodyRange);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(15).getRGB(),
					java.awt.Color.black.getRGB());
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(colRange);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(15).getRGB(),
					java.awt.Color.black.getRGB());
			m_view.setCellFormat(cellFmt);
			// m_view.update();

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}

	}// classic3

	private void Accounting1(CellFormat cellFmt) {
		String numberFormat;
		try {
			m_view.setSelection(colRange);
			m_view.setCellFormat(cfmt);

			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderThin);
			cellFmt.setBottomBorder(CellFormat.BorderThin);
			AdjustFont(java.awt.Color.magenta.getRGB(), true, true, false,
					cellFmt);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(bodyRange);
			numberFormat = "#,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(savedSelection);
			m_view.setSelection(m_view.getSelStartRow() + 1,
					m_view.getSelStartCol() + 1, m_view.getSelStartRow() + 1,
					m_view.getSelEndCol());
			cellFmt.setBottomBorder(CellFormat.BorderNone);
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(ftrRange);
			cellFmt.setBottomBorder(CellFormat.BorderDouble);
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	} // Accounting 1

	private void Accounting2(CellFormat cellFmt) {

		String numberFormat;

		try {
			m_view.setSelection(colRange);
			m_view.setCellFormat(cfmt);

			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderThick);
			cellFmt.setTopBorderColor(java.awt.Color.lightGray);
			cellFmt.setBottomBorder(CellFormat.BorderThin);
			cellFmt.setBottomBorderColor(java.awt.Color.lightGray);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(bodyRange);
			numberFormat = "#,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(savedSelection);
			m_view.setSelection(m_view.getSelStartRow() + 1,
					m_view.getSelStartCol() + 1, m_view.getSelStartRow() + 1,
					m_view.getSelEndCol());
			cellFmt.setBottomBorder(CellFormat.BorderNone);
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(ftrRange);
			cellFmt.setBottomBorder(CellFormat.BorderThick);
			cellFmt.setBottomBorderColor(java.awt.Color.lightGray);
			cellFmt.setTopBorder(CellFormat.BorderThin);
			cellFmt.setTopBorderColor(java.awt.Color.lightGray);
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}

	} // Accounting2

	private void Accounting3(CellFormat cellFmt) {
		String numberFormat;
		try {

			m_view.setSelection(colRange);
			AdjustFont(java.awt.Color.black.getRGB(), false, true, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(bodyRange);
			numberFormat = "#,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(savedSelection);
			m_view.setSelection(m_view.getSelStartRow() + 1,
					m_view.getSelStartCol() + 1, m_view.getSelStartRow() + 1,
					m_view.getSelEndCol());
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setTopBorder(CellFormat.BorderNone);
			cellFmt.setBottomBorder(CellFormat.BorderNone);
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(ftrRange);
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(m_view.getSelEndRow(),
					m_view.getSelStartCol() + 1, m_view.getSelEndRow(),
					m_view.getSelEndCol());
			cellFmt.setTopBorder(CellFormat.BorderThin);
			cellFmt.setBottomBorder(CellFormat.BorderDouble);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

			m_view.setSelection(savedSelection);
			m_view.setSelection(m_view.getSelStartRow(),
					m_view.getSelStartCol(), m_view.getSelStartRow(),
					m_view.getSelEndCol());
			cellFmt.setTopBorder(CellFormat.BorderNone);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorderColor(java.awt.Color.green);
			AdjustFont(m_view.getPaletteEntry(16).getRGB(), false, true, false,
					cellFmt);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);
			// m_view.update();

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	} // Accounting3

	private void Effects3D1(CellFormat cellFmt) {
		try {
			SetSolidPattern(cellFmt, java.awt.Color.lightGray.getRGB(), 0);

			Set3DBorder(cellFmt, m_view.getSelStartRow(),
					m_view.getSelStartCol(), m_view.getSelEndRow(),
					m_view.getSelEndCol(), java.awt.Color.darkGray.getRGB(),
					java.awt.Color.darkGray.getRGB(),
					java.awt.Color.darkGray.getRGB());
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(bodyRange);
			Set3DBorder(cellFmt, m_view.getSelStartRow(),
					m_view.getSelStartCol(), m_view.getSelEndRow(),
					m_view.getSelEndCol(), java.awt.Color.lightGray.getRGB(),
					java.awt.Color.darkGray.getRGB(),
					java.awt.Color.lightGray.getRGB());
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(hdrRange);
			AdjustFont(java.awt.Color.magenta.getRGB(), true, false, false,
					cellFmt);
			AlignCenter(cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(colRange);
			Set3DBorder(cellFmt, m_view.getSelStartRow(),
					m_view.getSelStartCol(), m_view.getSelEndRow(),
					m_view.getSelEndCol(), java.awt.Color.lightGray.getRGB(),
					java.awt.Color.lightGray.getRGB(),
					java.awt.Color.darkGray.getRGB());
			AdjustFont(java.awt.Color.black.getRGB(), true, false, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(ftrRange);
			Set3DBorder(cellFmt, m_view.getSelStartRow(),
					m_view.getSelStartCol(), m_view.getSelEndRow(),
					m_view.getSelEndCol(), java.awt.Color.lightGray.getRGB(),
					java.awt.Color.darkGray.getRGB(),
					java.awt.Color.darkGray.getRGB());
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}

	}// Effects3D1

	private void Colorful1(CellFormat cellFmt) {
		int color = java.awt.Color.red.getRGB();
		try {
			m_view.setSelection(savedSelection);
			cellFmt.setBottomBorder(CellFormat.BorderThin);
			cellFmt.setBottomBorderColor(java.awt.Color.red);
			SetSolidPattern(cellFmt, java.awt.Color.darkGray.getRGB(),
					java.awt.Color.black.getRGB());

			// outline border
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setLeftBorder(CellFormat.BorderMedium);
			cellFmt.setRightBorder(CellFormat.BorderMedium);
			cellFmt.setTopBorderColor(java.awt.Color.red);
			cellFmt.setLeftBorderColor(java.awt.Color.red);
			cellFmt.setRightBorderColor(java.awt.Color.red);

			AdjustFont(color, false, false, false, cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(hdrRange);
			SetSolidPattern(cellFmt, java.awt.Color.black.getRGB(),
					java.awt.Color.black.getRGB());
			AdjustFont(color, true, true, false, cellFmt);
			AlignCenter(cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(colRange);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(11).getRGB(),
					java.awt.Color.black.getRGB());
			AdjustFont(color, true, true, false, cellFmt);
			m_view.setCellFormat(cellFmt);
		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}// Colorful1

	private void Colorful2(CellFormat cellFmt) {

		try {
			int color = m_view.getPaletteEntry(14).getRGB();
			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorder(CellFormat.BorderThin);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(9).getRGB(),
					java.awt.Color.black.getRGB());
			AdjustFont(color, true, true, false, cellFmt);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(ftrRange);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			cellFmt.setTopBorder(CellFormat.BorderThin);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(colRange);
			AdjustFont(java.awt.Color.black.getRGB(), true, true, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(savedSelection);
			SetHatchPattern(cellFmt, m_view.getPaletteEntry(16).getRGB(),
					java.awt.Color.red.getRGB());
			m_view.setCellFormat(cellFmt);

		}

		catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}

	}// Colorful2

	private void Colorful3(CellFormat cellFmt) {
		try {
			m_view.setSelection(savedSelection);
			SetSolidPattern(cellFmt, java.awt.Color.black.getRGB(),
					java.awt.Color.black.getRGB());
			AdjustFont(java.awt.Color.white.getRGB(), false, false, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(hdrRange);

			AdjustFont(java.awt.Color.green.getRGB(), true, true, false,
					cellFmt);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(colRange);
			AdjustFont(java.awt.Color.magenta.getRGB(), true, true, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}// Colorful3

	private void List1(CellFormat cellFmt) {
		String newSelection, numberFormat;
		int fcolor, bcolor;

		try {
			m_view.setSelection(savedSelection);
			// Outline Border - thin
			cellFmt.setTopBorder(CellFormat.BorderThin);
			cellFmt.setLeftBorder(CellFormat.BorderThin);
			cellFmt.setRightBorder(CellFormat.BorderThin);
			cellFmt.setTopBorderColor(m_view.getPaletteEntry(16));
			cellFmt.setLeftBorderColor(m_view.getPaletteEntry(16));
			cellFmt.setRightBorderColor(m_view.getPaletteEntry(16));
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(hdrRange);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(14).getRGB(),
					java.awt.Color.black.getRGB());
			AdjustFont(java.awt.Color.blue.getRGB(), true, true, false, cellFmt);
			AlignCenter(cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(ftrRange);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(14).getRGB(),
					java.awt.Color.black.getRGB());
			AdjustFont(java.awt.Color.blue.getRGB(), true, false, false,
					cellFmt);
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);

			fcolor = m_view.getPaletteEntry(19).getRGB();
			bcolor = java.awt.Color.red.getRGB();
			for (int i = m_view.getSelStartRow() + 1; i < m_view.getSelEndRow() - 1; i = i + 2) {
				newSelection = m_view.formatRCNr(i, m_view.getSelStartCol(),
						false)
						+ ":"
						+ m_view.formatRCNr(i, m_view.getSelEndCol(), false);

				m_view.setSelection(newSelection);
				SetHatchPattern(cellFmt, fcolor, bcolor);
				m_view.setCellFormat(cellFmt);
			}

			fcolor = java.awt.Color.white.getRGB();
			bcolor = m_view.getPaletteEntry(15).getRGB();
			for (int i = m_view.getSelStartRow() + 2; i < m_view.getSelEndRow() - 1; i = i + 2) {
				newSelection = m_view.formatRCNr(i, m_view.getSelStartCol(),
						false)
						+ ":"
						+ m_view.formatRCNr(i, m_view.getSelEndCol(), false);

				m_view.setSelection(newSelection);
				SetHatchPattern(cellFmt, fcolor, bcolor);
				m_view.setCellFormat(cellFmt);
			}

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}

	private void List2(CellFormat cellFmt) {
		String numberFormat, newSelection;
		int fcolor, bcolor;

		try {
			m_view.setSelection(savedSelection);
			// Outline Border - thin
			cellFmt.setTopBorder(CellFormat.BorderThin);
			cellFmt.setLeftBorder(CellFormat.BorderThin);
			cellFmt.setRightBorder(CellFormat.BorderThin);
			cellFmt.setTopBorderColor(m_view.getPaletteEntry(16));
			cellFmt.setLeftBorderColor(m_view.getPaletteEntry(16));
			cellFmt.setRightBorderColor(m_view.getPaletteEntry(16));
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderThick);
			cellFmt.setTopBorderColor(m_view.getPaletteEntry(16));
			cellFmt.setBottomBorder(CellFormat.BorderThin);
			SetSolidPattern(cellFmt, m_view.getPaletteEntry(15).getRGB(),
					java.awt.Color.black.getRGB());
			AlignCenter(cellFmt);
			AdjustFont(java.awt.Color.red.getRGB(), true, true, false, cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(ftrRange);
			cellFmt.setBottomBorder(CellFormat.BorderThick);
			cellFmt.setBottomBorderColor(m_view.getPaletteEntry(16));
			cellFmt.setTopBorder(CellFormat.BorderThin);
			numberFormat = "$ #,##0.00_);(#,##0.00)";
			cellFmt.setCustomFormat(numberFormat);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);

			fcolor = java.awt.Color.red.getRGB();
			bcolor = java.awt.Color.white.getRGB();
			for (int i = m_view.getSelStartRow() + 1; i < m_view.getSelEndRow() - 1; i = i + 2) {
				newSelection = m_view.formatRCNr(i, m_view.getSelStartCol(),
						false)
						+ ":"
						+ m_view.formatRCNr(i, m_view.getSelEndCol(), false);

				m_view.setSelection(newSelection);
				SetHatchPattern(cellFmt, fcolor, bcolor);
				m_view.setCellFormat(cellFmt);
			}

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}

	} // List2

	private void List3(CellFormat cellFmt) {
		try {
			m_view.setSelection(colRange);
			m_view.setCellFormat(cfmt);
			m_view.setSelection(hdrRange);
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setTopBorderColor(java.awt.Color.darkGray);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorderColor(java.awt.Color.darkGray);
			AlignCenter(cellFmt);
			AdjustFont(m_view.getPaletteEntry(11).getRGB(), true, false, false,
					cellFmt);
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(ftrRange);
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setTopBorderColor(java.awt.Color.darkGray);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorderColor(java.awt.Color.darkGray);
			AlignRight(cellFmt);
			m_view.setCellFormat(cellFmt);
		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}// List3

	private void AdjustFont(int color, boolean bold, boolean italic,
			boolean underline, CellFormat cellFmt) {
		cellFmt.setFontBold(bold);
		cellFmt.setFontItalic(italic);
		cellFmt.setFontUnderline((short) (underline ? 1 : 0));

		cellFmt.setFontColor(new Color(color));

	} // AdjustFont

	private void AlignCenter(CellFormat cellFmt) {
		try {
			cellFmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
			cellFmt.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
			cellFmt.setWordWrap(false);

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}// AlignCenter

	private void AlignRight(CellFormat cellFmt) {
		try {
			cellFmt.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
			cellFmt.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
			cellFmt.setWordWrap(false);

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	}// AlignRight

	private void Set3DBorder(CellFormat cellFmt, int row1, int col1, int row2,
			int col2, int outlineColor, int rightColor, int bottomColor) {
		try {
			m_view.setSelection(row1, col1, row2, col2);
			// create an outline around the selection
			cellFmt.setTopBorder(CellFormat.BorderMedium);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			cellFmt.setLeftBorder(CellFormat.BorderMedium);
			cellFmt.setRightBorder(CellFormat.BorderMedium);
			cellFmt.setTopBorderColor(new Color(outlineColor));
			cellFmt.setBottomBorderColor(new Color(outlineColor));
			cellFmt.setLeftBorderColor(new Color(outlineColor));
			cellFmt.setRightBorderColor(new Color(outlineColor));
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(row1, col2, row2, col2);
			cellFmt.setRightBorder(CellFormat.BorderMedium);
			cellFmt.setRightBorderColor(new Color(rightColor));
			m_view.setCellFormat(cellFmt);

			m_view.setSelection(row2, col1, row2, col2);
			cellFmt.setBottomBorder(CellFormat.BorderMedium);
			// m_view.getPaletteEntry(bottomColor);
			cellFmt.setBottomBorderColor(new Color(bottomColor));
			m_view.setCellFormat(cellFmt);

		} catch (com.jxcell.CellException e) {
			System.out.println(e.getMessage());
		}
	} // 3DBorder

	private void SetSolidPattern(CellFormat cellFmt, int fcolor, int bcolor) {
		short nPattern;
		nPattern = 1;
		try {
			cellFmt.setPattern(nPattern);
			cellFmt.setPatternFG(new Color(fcolor));
			cellFmt.setPatternBG(new Color(bcolor));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	} // SetSolidPattern

	private void SetHatchPattern(CellFormat cellFmt, int fcolor, int bcolor)

	{
		try {
			cellFmt.setPattern((short) 4);
			cellFmt.setPatternFG(new Color(fcolor));
			cellFmt.setPatternBG(new Color(bcolor));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}// SetHatchPattern

}
