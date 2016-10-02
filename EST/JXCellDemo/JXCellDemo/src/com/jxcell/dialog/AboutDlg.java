package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.tools.GridManager;

import javax.swing.*;

public class AboutDlg extends Dialog
{

    public AboutDlg(View view)
    {
        super(view, "About", true);

        String ver = View.getVersionString();

        JLabel alabel[] = new JLabel[4];
        alabel[0] = new JLabel(ver.substring(0, ver.indexOf("\n")));
        alabel[1] = new JLabel(ver.substring(ver.indexOf("\n") + 2));

        GridManager gridManager = new GridManager();

        for(int index = 0; index < 2; index++)
        {
            AboutDlg aboutdlg = this;
            JLabel label = alabel[index];
            gridManager.insert(aboutdlg, getContentPane(), label, 0, index, 1, 1, 1, 1, 0, 0, gridManager.insets[0], 2, 17);
        }

        gridManager.insert(this, getContentPane(), btOK, 1, 0, 1, 1, 0, 0, 20, 0, gridManager.insets[0], 0, 10);
    }

    protected void setdefault()
    {
    }
}
