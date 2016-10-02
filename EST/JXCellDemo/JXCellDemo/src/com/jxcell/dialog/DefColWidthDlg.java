package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.tools.TextFieldEx;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DefColWidthDlg extends SizerDlg
{

    JComboBox chcunits;
    TextFieldEx txtwidth;
    FramePanel framepanel;
    GridManager gridManager;

    public DefColWidthDlg(View view)
    {
        super(view, "Default Column Width", true);
        txtwidth = new TextFieldEx(0.0D, 256D);
        chcunits = newJComboBox(false, "Characters;Centimeters;Inches");
        framepanel = new FramePanel("Width");
        java.awt.Container container = getContentPane();
        gridManager = new GridManager();
        gridManager.insert(this, container, framepanel, 0, 0, 2, 3);
        gridManager.insertHW(this, framepanel, txtwidth, 0, 0);
        gridManager.insertWithInsetHW(this, container, new JLabel("Units:"), 0, 3, 1, 1, gridManager.getInsets(1));
        gridManager.insertWithInsetHW(this, container, chcunits, 1, 3, 1, 1, gridManager.getInsets(5));
        gridManager.insertHN(this, container, this.btOK, 2, 0);
        gridManager.insertHN(this, container, this.btCancel, 2, 1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == chcunits)
        {
            txtwidth.setText(getUnitStr(chcunits.getSelectedIndex()));
        } else
        {
            super.actionPerformed(actionevent);
        }
    }

    short getColWidthUnits()
    {
        return (short)(!getWorkBook().isColWidthInTwips() ? 0 : 1);
    }

    protected JComponent getFocusComponent()
    {
        return txtwidth;
    }

    protected void setdefault()
    {
        short unit = getColWidthUnits();
        setUnit(getSheet().getDefaultColWidth(), unit);
        chcunits.setSelectedIndex(getcurrentunit());
        txtwidth.setText(getUnitStr());
    }

    protected void okClicked()
        throws Throwable
    {
        int j = chcunits.getSelectedIndex();
        setUnitAndSize(txtwidth.getText(), j);
        getWorkBook().setColWidthInTwips(j != 0);
        m_view.setDefaultColWidth((int)getUnitAndSize1(j));
    }

    void setUnit(double size, int unit)
    {
        if(unit == 0)
        {
            setUnitAndSize(size / 256D, 0);
            return;
        }
        if(SizerDlg.ISMETRIC)
        {
            setUnitAndSize((size / 1440D) * 2.54D, 1);
        } else
        {
            setUnitAndSize(size / 1440D, 2);
        }
    }
}
