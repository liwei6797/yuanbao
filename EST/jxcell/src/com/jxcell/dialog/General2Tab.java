package com.jxcell.dialog;

import com.jxcell.tools.GridManager;

import javax.swing.*;


class General2Tab extends TabBase
{

    private JTextField txtsheetname;
    private JCheckBox cbLotus;

    General2Tab(TabDialog tabDialog)
    {
        super(tabDialog);
        txtsheetname = new JTextField("", 30);
        cbLotus = getJCheckBox("Lotus Style Formula Evaluation;L", false);
        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, this, new JLabel("Sheet Name:"), 0, 0);
        gridManager.insert(this, this, txtsheetname, 0, 1);
        gridManager.insert(this, this, cbLotus, 0, 2, 1, 1, 100, 100, 0, 0, gridManager.getInsets(1), 0, 18);
    }

    public JComponent getFocusComponent()
    {
        return txtsheetname;
    }

    public void updateControls()
    {
        txtsheetname.setText(getSheet().getName());
        cbLotus.setSelected(getSheet().isLotusEvaluation());
    }

    protected void setOptions()
        throws Throwable
    {
        getSheet().setName(txtsheetname.getText());
        getSheet().setLotusEvaluation(cbLotus.isSelected());
    }
}
