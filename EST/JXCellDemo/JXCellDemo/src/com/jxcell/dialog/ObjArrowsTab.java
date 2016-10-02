package com.jxcell.dialog;

import com.jxcell.GRObject;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;


class ObjArrowsTab extends TabBase
{

    private JRadioButton rb1[];
    private JRadioButton rb2[];
    private JRadioButton rb3[];

    ObjArrowsTab(TabDialog dlgsheet)
    {
        super(dlgsheet);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Style");
        com.jxcell.tools.FramePanel framepanel1 = new FramePanel("Length");
        com.jxcell.tools.FramePanel framepanel2 = new FramePanel("Width");
        rb1 = getJRadioButtons("None;N'Hollow;H'Filled;F");
        rb2 = getJRadioButtons("Small;S'Medium;M'Large;L");
        rb3 = getJRadioButtons("Small;S'Medium;M'Large;L");
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insert(this, this, framepanel1, 1, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(2), 0, 18);
        gridManager.insert(this, this, framepanel2, 2, 0, 1, 1, 1, 1, 0, 0, gridManager.getInsets(2), 0, 18);
        gridManager.insert(this, framepanel, rb1);
        gridManager.insert(this, framepanel1, rb2);
        gridManager.insert(this, framepanel2, rb3);
    }

    public void updateControls()
    {
        try
        {
            GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
            rb1[grobject.getArrowStyle()].setSelected(true);
            rb2[grobject.getArrowLength()].setSelected(true);
            rb3[grobject.getArrowWidth()].setSelected(true);
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }

    protected void setOptions()
        throws Throwable
    {
        GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
        grobject.setArrowStyle(getRadioIndex(rb1));
        grobject.setArrowLength(getRadioIndex(rb2));
        grobject.setArrowWidth(getRadioIndex(rb3));
    }
}
