package com.jxcell.dialog;

import com.jxcell.paint.Brush;
import com.jxcell.paint.Palette;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FillPanel;

import java.awt.event.ActionEvent;


class FillTab extends TabBase
{

    private FillPanel m_FillPanel;
    private Palette m_Palette;

    FillTab(TabDialog tabDialog)
    {
        super(tabDialog);
        m_Palette = tabDialog.getAdapter().getView().getPalette();
        m_FillPanel = getFillPanel(110);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, m_FillPanel, 0, 0, 1, 1, 100, 100, 0, 0, gridManager.getInsets(0), 0, 18);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        apply();
    }

    public void updateControls()
    {
        com.jxcell.CellFormat cellformat = getCellFormat();
        m_FillPanel.setSelected(new Brush(cellformat.getPattern(), 
                m_Palette.getNearestPaletteIndex(cellformat.getPatternFG(),-1),
                m_Palette.getNearestPaletteIndex(cellformat.getPatternBG(), -1),
                false));
    }

    void set(com.jxcell.CellFormat cellformat)
        throws Throwable
    {
        Brush brush = m_FillPanel.getBrush();
        cellformat.setPattern((short)brush.getPattern());
        cellformat.setPatternFG(m_Palette.getRGB(brush.getFillColor()));
        cellformat.setPatternBG(m_Palette.getRGB(brush.getPatternColor()));
    }
}
