package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.tools.GridManager;

import javax.swing.*;


public class GotoDlg extends Dialog
{

    private JTextField tf;

    public GotoDlg(View view)
    {
        super(view, "Goto", true);
        tf = new JTextField("", 12);
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, container, new JLabel("Goto:"), 0, 0);
        gridManager.insertWithInsetHW(this, container, tf, 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insert(this, container, this.btOK, 2, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(2), 2, 10);
        gridManager.insertHN(this, container, this.btCancel, 2, 1);
    }

    protected JComponent getFocusComponent()
    {
        return tf;
    }

    protected void setdefault()
    {
        tf.setText(getSelection().getAreaConst(0).toString());
    }

    protected void okClicked()
        throws Throwable
    {
        getSSView().setSelection(tf.getText(), getWorkBook().getGroup().getBasicLocaleInfo(0x20000000));
        getSSView().showCell(getActiveRow(), getActiveCol());
    }
}
