package com.jxcell.dialog;

import com.jxcell.GRObject;
import com.jxcell.paint.Brush;
import com.jxcell.ss.ShapeFormatImpl;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FillPanel;


class ObjFillTab extends TabBase
{

    private FillPanel fillPanel;

    ObjFillTab(TabDialog dlgsheet)
    {
        super(dlgsheet);
        fillPanel = getFillPanel();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, fillPanel, 0, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
    }

    public void updateControls()
    {
        Brush brush = new Brush();
        fillPanel.setSelected(brush);
    }


    protected void setOptions()
        throws Throwable
    {
        GRObject grobject = m_view.getSelectedObject(0);
        fillPanel.getBrush().setFillFormat(grobject.getFormat());
        grobject.setFormat();
    }
}
