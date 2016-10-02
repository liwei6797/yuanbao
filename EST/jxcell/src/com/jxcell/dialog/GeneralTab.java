package com.jxcell.dialog;

import com.jxcell.ss.BookViewInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;


class GeneralTab extends TabBase
{

    private JComboBox chctabs;
    private JCheckBox chkbar;
    private JCheckBox chkcellbar;
    private JCheckBox chkborder;

    GeneralTab(TabDialog dlgsheet)
    {
        super(dlgsheet);
        chctabs = getJComboBox(false, "Off;Bottom;Top");
        chkbar = getJCheckBox("Formula Bar;F", false);
        chkcellbar = getJCheckBox("Cell Reference In Formula Bar;C", false);
        chkborder = getJCheckBox("Border;B", false);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Show Options");
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, new JLabel("Sheet Tabs:"), 0, 0);
        gridManager.insert(this, this, chctabs, 0, 1, 1, 1, 0, 0, 40, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insert(this, this, framepanel, 0, 2, 1, 1, 4, 4, 0, 0, gridManager.getInsets(1), 0, 18);
        gridManager.insert(this, framepanel, chkbar, 0, 0);
        gridManager.insert(this, framepanel, chkcellbar, 0, 1);
        gridManager.insert(this, framepanel, chkborder, 0, 3);
    }

    public void updateControls()
    {
        BookViewInfo bookViewInfo = getBookViewInfo();
        chctabs.setSelectedIndex(bookViewInfo.getShowTabs());
        chkbar.setSelected(bookViewInfo.isShowEditBar());
        chkcellbar.setSelected(bookViewInfo.isShowEditBarCellRef());
        chkborder.setSelected(bookViewInfo.isBorder());
    }

    protected void setOptions()
        throws Throwable
    {
        BookViewInfo bookViewInfo = getBookViewInfo();
        bookViewInfo.setShowTabs((short)chctabs.getSelectedIndex());
        bookViewInfo.setShowEditBar(chkbar.isSelected());
        bookViewInfo.setShowEditBarCellRef(chkcellbar.isSelected());
        bookViewInfo.setBorder(chkborder.isSelected());
    }
}
