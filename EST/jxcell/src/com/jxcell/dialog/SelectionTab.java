package com.jxcell.dialog;

import com.jxcell.ss.SheetViewInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;


class SelectionTab extends TabBase
{

    private JCheckBox chkselections;
    private JCheckBox chkobjects;
    private JCheckBox chkrowmode;
    private JComboBox chcshow;

    SelectionTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Options");
        chkselections = getJCheckBox("Allow Selections;S", false);
        chkobjects = getJCheckBox("Allow Object Selections;O", false);
        chkrowmode = getJCheckBox("Row Mode;R", false);
        chcshow = getJComboBox(false, "Off;On;Automatic");
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, 1, 1, 1, 1, 0, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insertWithInset(this, framepanel, chkselections, 0, 0, 2, 1, gridManager.getInsets(0));
        gridManager.insertWithInset(this, framepanel, chkobjects, 0, 1, 2, 1, gridManager.getInsets(0));
        gridManager.insertWithInset(this, framepanel, chkrowmode, 0, 2, 2, 1, gridManager.getInsets(0));
        gridManager.insertWithInsetHW(this, framepanel, new JLabel("Show Selections:"), 0, 3, 1, 1, gridManager.getInsets(1));
        gridManager.insertWithInsetHW(this, framepanel, chcshow, 1, 3, 1, 1, gridManager.getInsets(5));
    }

    public void updateControls()
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        chkselections.setSelected(m_sheetViewInfo.isAllowSelections());
        chkobjects.setSelected(m_sheetViewInfo.isAllowObjectSelections());
        chkrowmode.setSelected(m_sheetViewInfo.isRowMode());
        chcshow.setSelectedIndex(m_sheetViewInfo.getShowSelections());
    }

    protected void setOptions()
        throws Throwable
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        m_sheetViewInfo.setAllowSelections(chkselections.isSelected());
        m_sheetViewInfo.setAllowObjectSelections(chkobjects.isSelected());
        m_sheetViewInfo.setRowMode(chkrowmode.isSelected());
        m_sheetViewInfo.setShowSelections((short)chcshow.getSelectedIndex());
    }
}
