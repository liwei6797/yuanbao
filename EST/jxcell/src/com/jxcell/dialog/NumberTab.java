package com.jxcell.dialog;

import com.jxcell.tools.GridManager;
import com.jxcell.tools.ListPanel;
import com.jxcell.util.CharBuffer;
import com.jxcell.util.ValueFormat;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;


class NumberTab extends TabBase
{

    private JTextField txtformat;
    private ListPanel lstcategory;
    private ListPanel lstformat;

    NumberTab(TabDialog tabDialog)
    {
        super(tabDialog);
        txtformat = new JTextField("", 30);
        lstcategory = getListPanel(10, 80, "All;Number;Currency;Date;Time;Percent;Fraction;Scientific;Text");
        lstformat = getListPanel(10);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, new JLabel("Category:"), 0, 0);
        gridManager.insert(this, this, lstcategory, 0, 1, 1, 2, 0, 0, 0, 0, gridManager.getInsets(0), 1, 18);
        gridManager.insertWithInset(this, this, new JLabel("Number Format:"), 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insert(this, this, txtformat, 1, 1, 1, 1, 0, 0, 0, 0, gridManager.getInsets(2), 2, 18);
        gridManager.insert(this, this, lstformat, 1, 2, 1, 1, 1, 1, 0, 0, gridManager.getInsets(2), 1, 18);
    }

    private void addFormat(int type)
    {
        getLock();
        try
        {
            lstformat.removeAllElements();
            int count = getWorkBook().getValueFormatCount();
            for(int i = 0; i < count; i++)
            {
                ValueFormat valFormat1 = getWorkBook().getValueFormat(i);
                if(!valFormat1.isHidden() && (type == 0 || type == valFormat1.getType()))
                {
                    CharBuffer charBuffer1 = clearText();
                    valFormat1.unparse(getWorkBook().getGroup(), getWorkBook().getGroup().getBasicLocaleInfo(0x20000000), charBuffer1);
                    lstformat.addElement(charBuffer1.toString());
                    int len = lstformat.getListSize() - 1;
                    if(txtformat.getText().equals(lstformat.getElementAt(len)))
                        lstformat.setSelectedIndex(len);
                }
            }
        }
        finally
        {
            releaseLock();
        }
    }

    public JComponent getFocusComponent()
    {
        return txtformat;
    }

    public void updateControls()
    {
        com.jxcell.CellFormat cellformat = getCellFormat();
        lstcategory.setSelectedIndex(cellformat.getCustomFormatType());
        txtformat.setText(cellformat.getCustomFormat());
        addFormat(cellformat.getCustomFormatType());
    }

    protected void setOptions()
        throws Throwable
    {
        this.m_comp = txtformat;
        com.jxcell.CellFormat cellformat = getCellFormat();
        String nf1 = txtformat.getText();
        if(!nf1.equals(cellformat.getCustomFormat()))
            cellformat.setCustomFormat(nf1);
    }

    public void setEnabled()
    {
        setApplyButtonEnabled(true);
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        if(isShowing())
        {
            Object obj = listselectionevent.getSource();
            if(obj == lstcategory.getList())
            {
                addFormat(lstcategory.getSelectedIndex());
                return;
            }
            if(obj == lstformat.getList())
            {
                String s = lstformat.getSelectedValue();
                if(s != null && !txtformat.getText().equals(s))
                {
                    txtformat.setText(s);
                    setApplyButtonEnabled(true);
                }
            }
        }
    }
}
