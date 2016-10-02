package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.tools.TextFieldEx;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class DefRowHeightDlg extends SizerDlg
{

    JRadioButton chkauto;
    JRadioButton chkcustom;
    JComboBox chcunits;
    TextFieldEx txtheight;
    FramePanel framepanel;
    GridManager gridManager;

    public DefRowHeightDlg(View view)
    {
        super(view, "Default Row Height", true);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(chkauto = newJRadioButton("Auto;A", false));
        buttongroup.add(chkcustom = newJRadioButton("Custom;C", false));
        txtheight = new TextFieldEx(0.0D, 256D);
        chcunits = newJComboBox(false, "Centimeters;Inches");
        framepanel = new FramePanel("Height");
        java.awt.Container container = getContentPane();
        gridManager = new GridManager();
        gridManager.insert(this, container, framepanel, 0, 0, 2, 3);
        gridManager.insert(this, framepanel, chkauto, 0, 0);
        gridManager.insert(this, framepanel, chkcustom, 0, 1);
        gridManager.insertHW(this, framepanel, txtheight, 0, 2);
        gridManager.insertWithInsetHW(this, container, new JLabel("Units:"), 0, 3, 1, 1, gridManager.getInsets(1));
        gridManager.insertWithInsetHW(this, container, chcunits, 1, 3, 1, 1, gridManager.getInsets(5));
        gridManager.insertHN(this, container, this.btOK, 2, 0);
        gridManager.insertHN(this, container, this.btCancel, 2, 1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == chcunits)
        {
            if(!chkauto.isSelected())
            {
                txtheight.setText(getUnitStr(chcunits.getSelectedIndex() + 1));
            }
        } else
        {
            if(obj == chkcustom)
            {
                txtheight.setText(getUnitStr(chcunits.getSelectedIndex() + 1));
                return;
            }
            if(obj == chkauto)
            {
                txtheight.setText("");
                return;
            }
            super.actionPerformed(actionevent);
        }
    }

    protected JComponent getFocusComponent()
    {
        return txtheight;
    }

    protected void setdefault()
    {
        setUnitAndSize(getSheet().getDefaultRowHeight());
        if(getSheet().isDefaultRowHeightAutomatic())
        {
            chkauto.setSelected(true);
            txtheight.setText("");
        } else
        {
            chkcustom.setSelected(true);
            txtheight.setText(getUnitStr());
        }
        chcunits.setSelectedIndex(SizerDlg.ISMETRIC ? 0 : 1);
    }

    protected void okClicked()
        throws Throwable
    {
        if(chkauto.isSelected())
        {
            m_view.setDefaultRowHeightAutomatic(true);
        } else
        {
            int i = chcunits.getSelectedIndex() + 1;
            setUnitAndSize(txtheight.getText(), i);
            m_view.setDefaultRowHeight((int)getUnitAndSize(i));
        }
    }

    protected void setSelected()
    {
        if(getFocusOwner() instanceof JTextField)
            chkcustom.setSelected(true);
    }

    void setUnitAndSize(double size)
    {
        if(SizerDlg.ISMETRIC)
        {
            setUnitAndSize((size / 1440D) * 2.54D, 1);
        } else
        {
            setUnitAndSize(size / 1440D, 2);
        }
    }
}
