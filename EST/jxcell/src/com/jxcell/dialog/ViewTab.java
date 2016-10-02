package com.jxcell.dialog;

import com.jxcell.ss.Region;
import com.jxcell.ss.SheetViewInfo;
import com.jxcell.ss.TArea;
import com.jxcell.ss.Book;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.util.CharBuffer;

import javax.swing.*;


class ViewTab extends TabBase
{

    private JCheckBox chkformulas;
    private JCheckBox chkgridlines;
    private JCheckBox chkzerovalues;
    private JCheckBox chkrowheading;
    private JCheckBox chkcolheading;
    private JTextField txtviewscale;
    private JTextField txtlimits;
    private JComboBox chchorizontal;
    private JComboBox chcvertical;

    ViewTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Sheet and Data");
        com.jxcell.tools.FramePanel framepanel1 = new FramePanel("Scrollbars");
        chkformulas = getJCheckBox("Formulas;F", false);
        chkgridlines = getJCheckBox("Grid Lines;G", false);
        chkzerovalues = getJCheckBox("Zero Values;Z", false);
        chkrowheading = getJCheckBox("Row Heading;R", false);
        chkcolheading = getJCheckBox("Column Heading;C", false);
        txtviewscale = new JTextField("", 3);
        txtlimits = new JTextField("", 12);
        chchorizontal = getJComboBox(false, "Off;On;Automatic");
        chcvertical = getJComboBox(false, "Off;On;Automatic");
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, 1, 3, 0, 0, 0, 0, gridManager.getInsets(0), 3, 18);
        gridManager.insert(this, framepanel, chkformulas, 0, 0);
        gridManager.insert(this, framepanel, chkgridlines, 0, 1);
        gridManager.insert(this, framepanel, chkzerovalues, 0, 2);
        gridManager.insert(this, framepanel, chkrowheading, 0, 3);
        gridManager.insert(this, framepanel, chkcolheading, 0, 4);
        gridManager.insert(this, this, framepanel1, 1, 0, 2, 1, 1, 1, 0, 0, gridManager.getInsets(2), 0, 18);
        gridManager.insertHW(this, framepanel1, new JLabel("Horizontal:"), 0, 0);
        gridManager.insertHW(this, framepanel1, chchorizontal, 0, 1);
        gridManager.insertWithInsetHW(this, framepanel1, new JLabel("Vertical:"), 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel1, chcvertical, 1, 1, 1, 1, gridManager.getInsets(2));
        gridManager.insert(this, this, new JLabel("View Scale:"), 1, 1, 1, 1, 0, 0, 0, 0, gridManager.getInsets(5), 0, 18);
        gridManager.insert(this, this, txtviewscale, 2, 1, 1, 1, 0, 0, 0, 0, gridManager.getInsets(5), 0, 18);
        gridManager.insert(this, this, new JLabel("Sheet Limits:"), 1, 2, 1, 1, 1, 0, 0, 0, gridManager.getInsets(5), 0, 18);
        gridManager.insert(this, this, txtlimits, 2, 2, 1, 1, 10, 10, 0, 0, gridManager.getInsets(5), 0, 18);
    }

    public void updateControls()
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        chkformulas.setSelected(m_sheetViewInfo.isShowFormulas());
        chkgridlines.setSelected(m_sheetViewInfo.isShowGridLines());
        chkzerovalues.setSelected(m_sheetViewInfo.isShowZeroValues());
        chkrowheading.setSelected(m_sheetViewInfo.isShowRowHeading());
        chkcolheading.setSelected(m_sheetViewInfo.isShowColHeading());
        chchorizontal.setSelectedIndex(m_sheetViewInfo.getShowHScrollbar());
        chcvertical.setSelectedIndex(m_sheetViewInfo.getShowVScrollbar());
        txtviewscale.setText(Integer.toString(m_sheetViewInfo.getScale()));
        CharBuffer charBuffer1 = clearText();
        getWorkBook();
        int row1 = m_sheetViewInfo.getMinRow();
        int col1 = m_sheetViewInfo.getMinCol();
        Book.formatColNr(col1, charBuffer1);
        charBuffer1.append(row1 + 1);
        String address = charBuffer1.toString();
        charBuffer1 = clearText();
        getWorkBook();
        col1 = m_sheetViewInfo.getMaxRow();
        int maxcol = m_sheetViewInfo.getMaxCol();
        Book.formatColNr(maxcol, charBuffer1);
        charBuffer1.append(col1 + 1);
        String name = charBuffer1.toString();
        if(address.equals(name))
        {
            txtlimits.setText(address);
        } else
        {
            txtlimits.setText(address + ":" + name);
        }
    }

    protected void setOptions()
        throws Throwable
    {
        SheetViewInfo sheetViewInfo1 = getSheetViewInfo();
        this.m_comp = txtlimits;
        Region region1 = getView().getRegion(txtlimits.getText(), getWorkBook().getGroup().getBasicLocaleInfo(0x20000000));
        if(region1.getAreaCount() != 1)
            showError((short)14);
        TArea trange1 = (TArea) region1.getAreaConst(0);
        int row1 = trange1.getRow1();
        int row2 = trange1.getRow2();
        if(row1 <= sheetViewInfo1.getMaxRow())
        {
            sheetViewInfo1.setMinRow(row1);
            sheetViewInfo1.setMaxRow(row2);
        } else
        {
            sheetViewInfo1.setMaxRow(row2);
            sheetViewInfo1.setMinRow(row1);
        }
        row1 = trange1.getCol1();
        row2 = trange1.getCol2();
        if(row1 <= sheetViewInfo1.getMaxCol())
        {
            sheetViewInfo1.setMinCol(row1);
            sheetViewInfo1.setMaxCol(row2);
        } else
        {
            sheetViewInfo1.setMaxCol(row2);
            sheetViewInfo1.setMinCol(row1);
        }
        sheetViewInfo1.setScale(getnumint(txtviewscale));
        sheetViewInfo1.setShowFormulas(chkformulas.isSelected());
        sheetViewInfo1.setShowGridLines(chkgridlines.isSelected());
        sheetViewInfo1.setShowZeroValues(chkzerovalues.isSelected());
        sheetViewInfo1.setShowRowHeading(chkrowheading.isSelected());
        sheetViewInfo1.setShowColHeading(chkcolheading.isSelected());
        sheetViewInfo1.setShowHScrollbar((short)chchorizontal.getSelectedIndex());
        sheetViewInfo1.setShowVScrollbar((short)chcvertical.getSelectedIndex());
    }
}
