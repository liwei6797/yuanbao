package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.ss.TArea;
import com.jxcell.ss.UndoableEdit;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SortDlg extends Dialog
{

    private JRadioButton chkrow;
    private JRadioButton chkcolumn;
    private JRadioButton chkascending;
    private JRadioButton chkdescending;
    private JComboBox chckeys;
    private JComboBox chcrefer;
    private boolean n_bRow;
    private int keyValues[];

    public SortDlg(View view)
        throws CellException
    {
        super(view, "Sort", true);
        n_bRow = true;
        if(getNrRanges() != 1)
            showException((short)14);
        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Sort By");
        com.jxcell.tools.FramePanel framepanel1 = new com.jxcell.tools.FramePanel("Keys");
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(chkrow = newJRadioButton("Rows;R", true));
        buttongroup.add(chkcolumn = newJRadioButton("Columns;C", false));
        ButtonGroup buttongroup1 = new ButtonGroup();
        buttongroup1.add(chkascending = newJRadioButton("Ascending;A", true));
        buttongroup1.add(chkdescending = newJRadioButton("Descending;D", false));
        chckeys = newJComboBox(false);
        chcrefer = newJComboBox(false);
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, container, framepanel, 0, 0, 1, 2, 1, 1, 0, 0, gridManager.getInsets(3), 1, 10);
        gridManager.insert(this, framepanel, chkrow, 0, 0);
        gridManager.insert(this, framepanel, chkcolumn, 0, 1);
        gridManager.insert(this, container, framepanel1, 0, 2, 2, 2);
        gridManager.insertHW(this, framepanel1, chckeys, 0, 0);
        gridManager.insertHW(this, framepanel1, chcrefer, 0, 2);
        gridManager.insertHW(this, framepanel1, new JLabel("Reference:"), 0, 1);
        gridManager.insertWithInset(this, framepanel1, chkascending, 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, framepanel1, chkdescending, 1, 1, 1, 1, gridManager.getInsets(2));
        gridManager.insertHN(this, container, this.btOK, 1, 0);
        gridManager.insertHN(this, container, this.btCancel, 1, 1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if((obj instanceof JRadioButton) || (obj instanceof JComboBox))
        {
            if(obj == chkrow || obj == chkcolumn)
            {
                n_bRow = obj == chkrow;
                getLock();
                try
                {
                    setdefault();
                }
                finally
                {
                    releaseLock();
                }
                return;
            }
            if(keyValues != null)
            {
                int keyIndex = chckeys.getSelectedIndex();
                int value = keyValues[keyIndex];
                if(obj == chkascending)
                {
                    keyValues[keyIndex] = Math.abs(value);
                    return;
                }
                if(obj == chkdescending)
                {
                    keyValues[keyIndex] = -Math.abs(value);
                    return;
                }
                if(obj == chckeys)
                {
                    if(value < 0)
                        chkdescending.setSelected(true);
                    else
                        chkascending.setSelected(true);
                    chcrefer.setSelectedIndex(Math.abs(value));
                    return;
                }
                if(obj == chcrefer)
                {
                    int i = chcrefer.getSelectedIndex();
                    keyValues[keyIndex] = chkascending.isSelected() ? i : -i;
                }
            }
        } else
        {
            super.actionPerformed(actionevent);
        }
    }

    protected void setdefault()
    {
        keyValues = null;
        TArea trange1 = getRange(0);
        int rcoff;
        String name;
        if(n_bRow)
        {
            rcoff = (trange1.getCol2() - trange1.getCol1()) + 1;
            name = "Column";
        } else
        {
            rcoff = (trange1.getRow2() - trange1.getRow1()) + 1;
            name = "Row";
        }
        if(rcoff > 10)
            rcoff = 10;
        if(chckeys.getItemCount() > 0)
            chckeys.removeAllItems();
        if(chcrefer.getItemCount() > 0)
            chcrefer.removeAllItems();
        String keyname = "Key";
        for(int i = 1; i <= rcoff; i++)
        {
            chckeys.addItem(keyname + Integer.toString(i));
            chcrefer.addItem(name + Integer.toString(i));
        }

        chcrefer.insertItemAt("None", 0);
        chkascending.setSelected(true);
        keyValues = new int[rcoff];
        keyValues[0] = 1;
    }

    protected void okClicked()
        throws Throwable
    {
        int len;
        for(len = 0; len < keyValues.length; len++)
            if(keyValues[len] == 0)
                break;

        if(len > 0)
        {
            int keys[] = new int[len];
            System.arraycopy(keyValues, 0, keys, 0, len);
            TArea trange1 = getRange(0);
            UndoableEdit viewUndoableEdit = getEdit(0x1200000d);
            getSheet().sort(trange1.getRow1(), trange1.getCol1(), trange1.getRow2(), trange1.getCol2(), chkrow.isSelected(), keys);
            if(viewUndoableEdit != null)
            {
                addEdit(viewUndoableEdit);
            }
        } else
        {
            throw new Throwable("You must have a key reference specified for Key 1.");
        }
    }
}
