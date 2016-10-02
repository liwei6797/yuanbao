package com.jxcell.dialog;

import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.tools.ListPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;


class AlignmentTab extends TabBase
{

    private ListPanel hAlignPanel;
    private ListPanel vAlignPanel;
    private JCheckBox cbWrap;

    AlignmentTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Options");
        hAlignPanel = getListPanel(7, 160, "General;Left;Center;Right;Fill;Justify;Center Across Cells");
        vAlignPanel = getListPanel(7, 80, "Top;Center;Bottom");
        cbWrap = getJCheckBox("Wrap Text;W", false);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, 1, 1, 1, 1, 0, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insert(this, framepanel, new JLabel("Horizontal:"), 0, 0);
        gridManager.insert(this, framepanel, hAlignPanel, 0, 1);
        gridManager.insertWithInset(this, framepanel, new JLabel("Vertical:"), 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, framepanel, vAlignPanel, 1, 1, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, framepanel, cbWrap, 0, 2, 1, 1, gridManager.getInsets(1));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        apply();
    }

    public void updateControls()
    {
        com.jxcell.CellFormat cellformat = getCellFormat();
        hAlignPanel.setSelectedIndex(cellformat.getHorizontalAlignment());
        vAlignPanel.setSelectedIndex(cellformat.getVerticalAlignment());
        cbWrap.setSelected(cellformat.isWordWrap());
    }

    void set(com.jxcell.CellFormat cellformat)
        throws Throwable
    {
        cellformat.setHorizontalAlignment((short)hAlignPanel.getSelectedIndex());
        cellformat.setVerticalAlignment((short)vAlignPanel.getSelectedIndex());
        cellformat.setWordWrap(cbWrap.isSelected());
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        apply();
    }
}
