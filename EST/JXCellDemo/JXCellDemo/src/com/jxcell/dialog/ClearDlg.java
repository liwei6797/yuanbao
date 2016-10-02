package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.GRObject;
import com.jxcell.View;
import com.jxcell.tools.GridManager;

import javax.swing.*;


public class ClearDlg extends Dialog
{

    private JRadioButton chkvalues;
    private JRadioButton chkformats;

    public ClearDlg(View view)
        throws CellException
    {
        super(view, "Clear", true);
        if(getController().isSelectedSheetProtected())
            showException((short)29);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(chkvalues = newJRadioButton("Values;V", true));
        buttongroup.add(chkformats = newJRadioButton("Formats;F", false));
        JRadioButton jradiobutton;
        buttongroup.add(jradiobutton = newJRadioButton("All;A", false));
        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Clear");
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, container, framepanel, 0, 0, 1, 2);
        gridManager.insert(this, framepanel, chkvalues, 0, 0);
        gridManager.insert(this, framepanel, chkformats, 0, 1);
        gridManager.insert(this, framepanel, jradiobutton, 0, 2);
        gridManager.insertHN(this, container, this.btOK, 1, 0);
        gridManager.insertHN(this, container, this.btCancel, 1, 1);
    }

    protected void setdefault()
    {
    }

    protected void okClicked()
        throws Throwable
    {
        short clearType = chkvalues.isSelected() ? 2 : ((short) (chkformats.isSelected() ? 1 : 3));
        GRObject grobject;
        if(clearType == 3)
            while((grobject = m_view.getSelectedObject(0)) != null)
                m_view.removeObject(grobject);
        m_view.editClear((short)6);

    }
}
