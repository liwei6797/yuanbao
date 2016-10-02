package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.FindReplaceInfo;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class FindReplaceDlg extends Dialog
{

    JTextField txtfind;
    private JComboBox chcsearch;
    private JComboBox chclookin;
    private JCheckBox chkmatch;
    private JCheckBox chkentire;
    private JButton btnfindnext;
    private JButton btnclose;
    private JButton btnreplace;
    private static String FIND = "";
    private static int SEARCH;
    private static int LOOKIN = 1;
    private static boolean MATCH;
    private static boolean ENTIRE;
    FindReplaceInfo frInfo;
    GridManager gridManager;

    public FindReplaceDlg(View view)
    {
        super(view, "Find", false);
        txtfind = new JTextField(FIND, 10);
        chcsearch = newJComboBox(false, "By Rows;By Columns");
        chclookin = newJComboBox(false, "Formulas;Values");
        chkmatch = newJCheckBox("Match Case;C", MATCH);
        chkentire = newJCheckBox("Find Entire Cells Only;O", ENTIRE);
        btnfindnext = newJButton("Find Next;F");
        btnclose = newJButton("Close");
        btnreplace = newJButton("Replace;R");
        chcsearch.setSelectedIndex(SEARCH);
        chclookin.setSelectedIndex(LOOKIN);
        java.awt.Container container = getContentPane();
        gridManager = new GridManager();
        gridManager.insertWithInsetHW(this, container, new JLabel("Find What:"), 0, 0, 2, 1, gridManager.getInsets(0));
        gridManager.insertWithInsetHW(this, container, txtfind, 0, 1, 3, 1, gridManager.getInsets(0));
        gridManager.insertHN(this, container, btnfindnext, 3, 0);
        gridManager.insertHN(this, container, btnclose, 3, 1);
        gridManager.insertHN(this, container, btnreplace, 3, 2);
        gridManager.insertHW(this, container, new JLabel("Search:"), 0, 4);
        gridManager.insertHW(this, container, new JLabel("Look In:"), 0, 5);
        gridManager.insertWithInsetHW(this, container, chcsearch, 1, 4, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, container, chclookin, 1, 5, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, container, chkmatch, 2, 4, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, container, chkentire, 2, 5, 1, 1, gridManager.getInsets(2));
        getRootPane().setDefaultButton(btnfindnext);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == btnfindnext)
        {
            btnfindnextClicked();
            return;
        }
        if(obj == btnclose)
        {
            btnClose();
            return;
        }
        if(obj == btnreplace)
            btnReplace();
    }

    void btnClose()
    {
        btncloseClicked();
        dispose();
    }

    boolean find()
    {
        frInfo = (new FindReplaceInfo(getSelection())).find(txtfind.getText(), getFlags(), false, true);
        return frInfo != null;
    }

    void btnfindnextClicked()
    {
        getLock();
        try
        {
            if(getNrRanges() == 0)
                showException((short)35);
            if(find())
                showActiveCell();
            else
                showMessage("No matching data was found.");
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

    int getFlags()
    {
        int options = 0;
        if(chcsearch.getSelectedIndex() == 1)
            options += 4;
        if(chclookin.getSelectedIndex() == 0)
            options += 8;
        if(chkmatch.isSelected())
            options++;
        if(chkentire.isSelected())
            options += 2;
        return options;
    }

    protected JComponent getFocusComponent()
    {
        return txtfind;
    }

    protected void setdefault()
    {
        btnreplace.setEnabled(!getSheet().isEnableProtection());
    }

    protected void onOK()
    {
        btnfindnextClicked();
    }

    void btnReplace()
    {
        btncloseClicked();
        (new ReplaceDlg(super.m_view)).setVisible(true);
        dispose();
    }

    private void btncloseClicked()
    {
        FIND = txtfind.getText();
        SEARCH = chcsearch.getSelectedIndex();
        LOOKIN = chclookin.getSelectedIndex();
        MATCH = chkmatch.isSelected();
        ENTIRE = chkentire.isSelected();
    }

    void showActiveCell()
    {
        int row1 = frInfo.getRow();
        int col1 = frInfo.getCol();
        getSelection().setActiveCell(row1, col1);
        getSSView().showCell(row1, col1);
    }
}
