package com.jxcell.dialog;

import com.jxcell.ss.SheetViewInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;


class EditTab extends TabBase
{

    private JCheckBox check1[];
    private JCheckBox check2[];

    EditTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Enable Options");
        com.jxcell.tools.FramePanel framepanel1 = new FramePanel("Allow User To");
        check1 = getJCheckBoxs("Enable Arrow Keys;A'Enable Delete Key;D'Enable Tab Key;T'Enable In-Cell Editing;I'Enter Moves Down;E");
        check2 = getJCheckBoxs("Resize Rows And Columns;R'Fill Ranges By Dragging;F'Move Ranges By Dragging;M'Edit Headings;H'Enter Formulas;o");
        GridManager gridManager = new GridManager();
        gridManager.insertWithInsetBC(this, this, framepanel, 0, 0, 1, 1, gridManager.getInsets(11));
        gridManager.insertWithInsetBC(this, this, framepanel1, 1, 0, 1, 1, gridManager.getInsets(11));
        gridManager.insert(this, framepanel, check1);
        gridManager.insert(this, framepanel1, check2);
    }

    public void updateControls()
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        check1[0].setSelected(m_sheetViewInfo.isAllowArrows());
        check1[1].setSelected(m_sheetViewInfo.isAllowDelete());
        check1[2].setSelected(m_sheetViewInfo.isAllowTabs());
        check1[3].setSelected(m_sheetViewInfo.isAllowInCellEditing());
        check1[4].setSelected(m_sheetViewInfo.isEnterMovesDown());
        check2[0].setSelected(m_sheetViewInfo.isAllowResize());
        check2[1].setSelected(m_sheetViewInfo.isAllowFillRange());
        check2[2].setSelected(m_sheetViewInfo.isAllowMoveRange());
        check2[3].setSelected(m_sheetViewInfo.isAllowEditHeaders());
        check2[4].setSelected(m_sheetViewInfo.isAllowFormulas());
    }

    protected void setOptions()
        throws Throwable
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        m_sheetViewInfo.setAllowArrows(check1[0].isSelected());
        m_sheetViewInfo.setAllowDelete(check1[1].isSelected());
        m_sheetViewInfo.setAllowTabs(check1[2].isSelected());
        m_sheetViewInfo.setAllowInCellEditing(check1[3].isSelected());
        m_sheetViewInfo.setEnterMovesDown(check1[4].isSelected());
        m_sheetViewInfo.setAllowResize(check2[0].isSelected());
        m_sheetViewInfo.setAllowFillRange(check2[1].isSelected());
        m_sheetViewInfo.setAllowMoveRange(check2[2].isSelected());
        m_sheetViewInfo.setAllowEditHeaders(check2[3].isSelected());
        m_sheetViewInfo.setAllowFormulas(check2[4].isSelected());
    }
}
