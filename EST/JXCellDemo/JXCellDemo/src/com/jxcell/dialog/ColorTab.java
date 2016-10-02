package com.jxcell.dialog;

import com.jxcell.ss.SheetViewInfo;
import com.jxcell.tools.ColorPanel;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

class ColorTab extends TabBase
{

    private ColorPanel cpback;
    private ColorPanel cpextra;

    ColorTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Back Color");
        com.jxcell.tools.FramePanel framepanel1 = new FramePanel("Extra Color");
        cpback = getColorPanel(1);
        cpextra = getColorPanel(1);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insertHW(this, framepanel, cpback, 0, 0);
        gridManager.insert(this, this, framepanel1, 1, 0, 1, 1, 1, 1, 0, 0, gridManager.getInsets(2), 0, 18);
        gridManager.insertHW(this, framepanel1, cpextra, 0, 0);
    }

    public void updateControls()
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        cpback.setSelected1(m_sheetViewInfo.getBackColor());
        cpextra.setSelected1(m_sheetViewInfo.getExtraColor());
    }

    protected void setOptions()
        throws Throwable
    {
        SheetViewInfo m_sheetViewInfo = getSheetViewInfo();
        m_sheetViewInfo.setBackColor(cpback.getColorIndex());
        m_sheetViewInfo.setExtraColor(cpextra.getColorIndex());
    }
}
