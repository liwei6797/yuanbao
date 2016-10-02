package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.ss.UndoableEdit;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PasteSpecialDlg extends Dialog
{

    private JCheckBox chkformulas;
    private JCheckBox chkvalues;
    private JCheckBox chkformats;
    private JCheckBox chkall;

    public PasteSpecialDlg(View view)
    {
        super(view, "Paste Special", true);
        chkformulas = newJCheckBox("Formulas;F", true);
        chkvalues = newJCheckBox("Values;V", true);
        chkformats = newJCheckBox("Formats;m", true);
        chkall = newJCheckBox("All;A", true);
        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Paste");
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, container, framepanel, 0, 0, 1, 2);
        gridManager.insert(this, framepanel, chkformulas, 0, 0);
        gridManager.insert(this, framepanel, chkvalues, 0, 1);
        gridManager.insert(this, framepanel, chkformats, 0, 2);
        gridManager.insert(this, framepanel, chkall, 0, 3);
        gridManager.insertHN(this, container, this.btOK, 1, 0);
        gridManager.insertHN(this, container, this.btCancel, 1, 1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj instanceof JCheckBox)
        {
            if(obj == chkall)
            {
                boolean bAll = chkall.isSelected();
                chkformulas.setSelected(bAll);
                chkvalues.setSelected(bAll);
                chkformats.setSelected(bAll);
                return;
            }
            if(obj == chkformulas && chkformulas.isSelected())
                chkvalues.setSelected(true);
            chkall.setSelected(chkformulas.isSelected() && chkvalues.isSelected() && chkformats.isSelected());
        } else
        {
            super.actionPerformed(actionevent);
        }
    }

    protected void setdefault()
    {
    }

    protected void okClicked()
        throws Throwable
    {
        UndoableEdit viewUndoableEdit = getEdit(0x2400009);
        if(viewUndoableEdit != null)
            addEdit(viewUndoableEdit);
        short what = 0;
        what |= chkformulas.isSelected() ? 1 : 0;
        what |= chkvalues.isSelected() ? 2 : 0;
        what |= chkformats.isSelected() ? 4 : 0;
        what |= chkall.isSelected() ? 7 : 0;
        super.m_view.editPasteSpecial(what);
    }
}
