package com.jxcell.dialog;


import com.jxcell.View;
import com.jxcell.FindReplaceInfo;
import com.jxcell.ss.UndoableEdit;
import com.jxcell.util.CharBuffer;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class ReplaceDlg extends FindReplaceDlg
{

    private JTextField txtReplace;
    private JButton buttonReplace;
    private static String m_strReplace = "";

    public ReplaceDlg(View view)
    {
        super(view);
        setTitle("Replace");
        txtReplace = new JTextField(m_strReplace, 10);
        buttonReplace = newJButton("Replace All;A");
        java.awt.Container container = getContentPane();
        gridManager.insertWithInsetHW(this, container, new JLabel("Replace With:"), 0, 2, 2, 1, gridManager.getInsets(0));
        gridManager.insertWithInsetHW(this, container, txtReplace, 0, 3, 3, 1, gridManager.getInsets(0));
        gridManager.insertHN(this, container, buttonReplace, 3, 3);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == buttonReplace)
        {
            replace();
        } else
        {
            super.actionPerformed(actionevent);
        }
    }

    void btnClose()
    {
        m_strReplace = txtReplace.getText();
        super.btnClose();
    }

    void btnReplace()
    {
        getLock();
        try
        {
            if(getController().isSelectedSheetProtected())
                showException((short)29);
            if(getNrRanges() == 0)
                showException((short)35);
            CharBuffer charBuffer1 = getCharBuffer();
            getSheet().getText(getActiveRow(), getActiveCol(), true, charBuffer1);
            if(frInfo == null && charBuffer1.toString().indexOf(txtfind.getText()) != -1)
                frInfo = (new FindReplaceInfo(getWorkBook(), getSheet().getSheetNumber(), getActiveRow(), getActiveCol(), getSheet().getSheetNumber(), getActiveRow(), getActiveCol())).find(super.txtfind.getText(), getFlags(), true, true);
            if(frInfo != null)
            {
                UndoableEdit viewUndoableEdit = getEdit(0x20000016);
                frInfo.replace(txtfind.getText(), txtReplace.getText(), getFlags());
                if(viewUndoableEdit != null)
                    addEdit(viewUndoableEdit);
                if(find())
                    showActiveCell();
            } else
            {
                showMessage("No matching data was found.");
            }
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
        finally
        {
            releaseLock();
        }
    }

    private void replace()
    {
        getLock();
        try
        {
            if(getController().isSelectedSheetProtected())
                showException((short)29);
            if(find())
            {
                UndoableEdit viewUndoableEdit = getEdit(0x20000016);
                frInfo.replace(txtfind.getText(), txtReplace.getText(), getFlags() + 16);
                if(viewUndoableEdit != null)
                    addEdit(viewUndoableEdit);
            } else
            {
                showMessage("No matching data was found.");
            }
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
        finally
        {
            releaseLock();
        }
    }
}
