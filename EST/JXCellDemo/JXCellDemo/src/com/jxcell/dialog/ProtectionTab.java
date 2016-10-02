package com.jxcell.dialog;

import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;


class ProtectionTab extends TabBase
{

    private JCheckBox chklocked;
    private JCheckBox chkhidden;

    ProtectionTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Options");
        chklocked = getJCheckBox("Locked;L", false);
        chkhidden = getJCheckBox("Hidden;H", false);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0);
        gridManager.insert(this, framepanel, chklocked, 0, 0);
        gridManager.insertWithInset(this, framepanel, chkhidden, 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, this, new JLabel("These options are ignored unless protection is enabled."), 0, 1, 1, 1, gridManager.getInsets(1));
        gridManager.insert(this, this, new JLabel("To enable protection, choose Sheet from the Format menu."), 0, 2);
        gridManager.insert(this, this, new JLabel("You can toggle protection on and off with the Enable Protection option."), 0, 3, 1, 1, 100, 100, 0, 0, gridManager.getInsets(0), 0, 18);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        apply();
    }

    public void updateControls()
    {
        com.jxcell.CellFormat cellformat = getCellFormat();
        chklocked.setSelected(cellformat.isLocked());
        chkhidden.setSelected(cellformat.isHidden());
    }

    void set(com.jxcell.ss.CellFormat cellformat)
        throws Throwable
    {
        cellformat.setLocked(chklocked.isSelected());
        cellformat.setHidden(chkhidden.isSelected());
    }
}
