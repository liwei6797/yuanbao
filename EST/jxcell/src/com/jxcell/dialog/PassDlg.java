package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.tools.GridManager;

import javax.swing.*;

public class PassDlg extends Dialog
{

    private JTextField tf;
    private String pass;

    public PassDlg(View view)
    {
        super(view, "Password", true);
        tf = new JTextField("", 12);

        GridManager gridManager = new GridManager();

        java.awt.Container container = getContentPane();
        gridManager.insert(this, container, new JLabel("Password"), 0, 0, 1, 1, 0, 0, 20, 0, gridManager.insets[0], 0, 10);
        gridManager.insert(this, container, tf, 1, 0, 1, 1, 0, 0, 20, 0, gridManager.insets[0], 0, 10);
        gridManager.insert(this, container, btOK, 0, 1, 1, 1, 0, 0, 20, 0, gridManager.insets[0], 0, 10);
        gridManager.insert(this, container, btCancel, 1, 1, 1, 1, 0, 0, 20, 0, gridManager.insets[0], 0, 10);
    }

    protected JComponent getFocusComponent()
    {
        return tf;
    }

    protected void setdefault()
    {
    }

    protected void okClicked()
    {
        pass = tf.getText();
        setVisible(false);
    }

    public String getPass()
    {
        return pass;
    }
}
