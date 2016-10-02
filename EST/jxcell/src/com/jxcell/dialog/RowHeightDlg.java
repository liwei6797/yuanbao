package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.ss.TArea;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class RowHeightDlg extends DefRowHeightDlg
{

    private JCheckBox chkdefault;
    private JButton btndefault;

    public RowHeightDlg(View view)
        throws CellException
    {
        super(view);
        if(getNrRanges() == 0)
            showException((short)35);
        setTitle("Row Height");
        chkdefault = newJCheckBox("Use Default;D", false);
        btndefault = newJButton("Default;e");
        gridManager.insert(this, framepanel, chkdefault, 0, 3);
        gridManager.insertHN(this, getContentPane(), btndefault, 2, 2);
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
            (new DefRowHeightDlg(m_view)).setVisible(true);
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
        TArea trange1 = getRange(0);
        int rc1 = trange1.getRow1();
        int size = getSheet().getRowHeight(rc1);
        setUnitAndSize(size);
        if(getSheet().isRowHeightAutomatic(rc1))
        {
            chkauto.setSelected(true);
            chkdefault.setSelected(getSheet().isDefaultRowHeightAutomatic());
        } else
        {
            chkcustom.setSelected(true);
            if(size == getSheet().getDefaultRowHeight())
                chkdefault.setSelected(true);
            txtheight.setText(getUnitStr());
        }
        chcunits.setSelectedIndex(SizerDlg.ISMETRIC ? 0 : 1);
    }

    protected void okClicked()
        throws Throwable
    {
        int unit = chcunits.getSelectedIndex() + 1;
        boolean auto = chkauto.isSelected();
        if(!auto)
            setUnitAndSize(txtheight.getText(), unit);
        int size = (int)getUnitAndSize(unit);
        int ranges = getNrRanges();
        for(int i = 0; i < ranges; i++)
        {
            TArea trange1 = getRange(i);
            m_view.setRowHeight(trange1.getRow1(), trange1.getRow2(), size, false, auto);
        }

    }

    protected void setSelected()
    {
        if(getFocusOwner() instanceof JTextField)
        {
            chkdefault.setSelected(false);
            chkcustom.setSelected(true);
        }
    }
}
