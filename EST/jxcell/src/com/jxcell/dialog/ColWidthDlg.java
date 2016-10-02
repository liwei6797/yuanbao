package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.ss.TArea;
import com.jxcell.View;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class ColWidthDlg extends DefColWidthDlg
{

    private JCheckBox chkdefault;
    private JButton btndefault;

    public ColWidthDlg(View view)
        throws CellException
    {
        super(view);
        if(getNrRanges() == 0)
            showException((short)35);
        setTitle("Column Width");
        chkdefault = newJCheckBox("Use Default;D", false);
        btndefault = newJButton("Default;e");
        super.gridManager.insert(this, super.framepanel, chkdefault, 0, 1);
        super.gridManager.insertHN(this, getContentPane(), btndefault, 2, 2);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == chkdefault)
        {
            if(chkdefault.isSelected())
            {
                super.setdefault();
            }
        } else
        if(obj == btndefault)
        {
            (new DefColWidthDlg(this.m_view)).setVisible(true);
            if(chkdefault.isSelected())
            {
                super.setdefault();
            }
        } else
        {
            super.actionPerformed(actionevent);
        }
    }

    protected void setdefault()
    {
        int width = getSheet().getColWidth(getRange(0).getCol1());
        short unit = getColWidthUnits();
        setUnit(width, unit);
        super.chcunits.setSelectedIndex(getcurrentunit());
        super.txtwidth.setText(getUnitStr());
        chkdefault.setSelected(width == getSheet().getDefaultColWidth());
    }

    protected void okClicked()
        throws Throwable
    {
        int i = super.chcunits.getSelectedIndex();
        setUnitAndSize(super.txtwidth.getText(), i);
        getWorkBook().setColWidthInTwips(i != 0);
        int size = (int)getUnitAndSize1(i);
        int nrRanges = getNrRanges();
        for(int index = 0; index < nrRanges; index++)
        {
            TArea trange1 = getRange(index);
            m_view.setColWidth(trange1.getCol1(), trange1.getCol2(), size, false);
        }

    }

    protected void setSelected()
    {
        if(getFocusOwner() instanceof JTextField)
            chkdefault.setSelected(false);
    }
}
