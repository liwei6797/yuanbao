package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.ss.TArea;
import com.jxcell.tools.GridManager;

import javax.swing.*;


public class DeleteDlg extends Dialog
{

    JRadioButton buttons[];

    public DeleteDlg(View view)
        throws CellException
    {
        super(view, "Delete", true);
        if(getController().isSelectedSheetProtected())
            showException((short)29);
        if(getNrRanges() != 1)
            showException((short)14);
        buttons = newJRadioButtons("Shift Cells Left;L'Shift Cells Up;U'Entire Row;R'Entire Column;C");
        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Options");
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, container, framepanel, 0, 0, 1, 2);
        gridManager.insert(this, framepanel, buttons);
        gridManager.insertHN(this, container, this.btOK, 2, 0);
        gridManager.insertHN(this, container, this.btCancel, 2, 1);
    }

    void edit(TArea tArea, short mode)
        throws CellException
    {
        UndoableEdit viewUndoableEdit = getEdit(0x4400018);
        if(viewUndoableEdit != null)
            addEdit(viewUndoableEdit);
        m_view.deleteRange(tArea.getRow1(), tArea.getCol1(), tArea.getRow2(), tArea.getCol2(), mode);
    }

    protected JComponent getFocusComponent()
    {
        return buttons[1];
    }

    protected void setdefault()
    {
        buttons[1].setSelected(true);
    }

    protected void okClicked()
        throws Throwable
    {
        short mode;
        switch(getRadioIndex(buttons))
        {
        case 0:
            mode = 1;
            break;

        case 1:
            mode = 2;
            break;

        case 2:
            mode = 3;
            break;

        default:
            mode = 4;
            break;
        }
        edit(getRange(0), mode);
    }
}
