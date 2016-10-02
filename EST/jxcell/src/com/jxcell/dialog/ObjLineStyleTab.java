package com.jxcell.dialog;

import com.jxcell.GRObject;
import com.jxcell.paint.Pen;
import com.jxcell.ss.ShapeFormatImpl;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.LinePanel;


class ObjLineStyleTab extends TabBase
{

    private LinePanel linePanel;

    ObjLineStyleTab(TabDialog tabDialog)
    {
        super(tabDialog);
        linePanel = getLinePanel();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, linePanel, 0, 0, 1, 1, 100, 100, 0, 0, gridManager.getInsets(0), 0, 18);
    }

    public void updateControls()
            throws Throwable
    {
        GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
        ShapeFormatImpl format = grobject.getFormat();
        Pen pen = linePanel.getPen();
        pen.setStyle(format.line().getDashStyle().getValue());
        pen.setWeight((int) format.line().getWeight());
        pen.setColor(format.line().getForeColor());
        linePanel.setPen(pen);
    }

    protected void setOptions()
        throws Throwable
    {
        GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
        ShapeFormatImpl format = grobject.getFormat();
        Pen pen = linePanel.getPen();
        format.line().setVisible(!pen.isNull());
        format.line().setWeight(pen.getWeight());
        format.line().setForeColor(pen.getColor());
    }
}
