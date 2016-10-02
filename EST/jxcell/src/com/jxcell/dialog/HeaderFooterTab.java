package com.jxcell.dialog;

import com.jxcell.ss.PrintInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.JTextAreaEX;

import javax.swing.*;


class HeaderFooterTab extends TabBase
{

    private JTextAreaEX txtheader;
    private JTextAreaEX txtfooter;

    HeaderFooterTab(TabDialog tabDialog)
    {
        super(tabDialog);
        txtheader = new JTextAreaEX(4, 20);
        txtfooter = new JTextAreaEX(4, 20);
        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, this, new JLabel("Header:"), 0, 0);
        gridManager.insert(this, this, txtheader, 0, 1, 1, 1);
        gridManager.insertHW(this, this, new JLabel("Footer:"), 0, 2);
        gridManager.insert(this, this, txtfooter, 0, 3, 1, 1, 100, 100, 0, 0, gridManager.getInsets(0), 2, 18);
    }

    public JComponent getFocusComponent()
    {
        return txtheader.getJTextArea();
    }

    public void updateControls()
    {
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        txtheader.setText(printInfo.getHeader());
        txtfooter.setText(printInfo.getFooter());
    }

    protected void setOptions()
        throws Throwable
    {
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        printInfo.setHeader(txtheader.getText());
        printInfo.setFooter(txtfooter.getText());
    }
}
