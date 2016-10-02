package com.jxcell.dialog;

import com.jxcell.ss.Book;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;


class CalculationTab extends TabBase
{

    private JCheckBox chkauto;
    private JCheckBox chkiter;
    private JTextField txtiter;
    private JTextField txtchan;

    CalculationTab(TabDialog dlgsheet)
    {
        super(dlgsheet);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Iteration");
        chkauto = getJCheckBox("Automatic Recalc;A", false);
        chkiter = getJCheckBox("Iteration;I", false);
        txtiter = new JTextField("", 10);
        txtchan = new JTextField("", 10);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, chkauto, 0, 0);
        gridManager.insert(this, this, framepanel, 0, 2, 1, 1, 100, 100, 0, 0, gridManager.getInsets(1), 0, 18);
        gridManager.insert(this, framepanel, chkiter, 0, 0);
        gridManager.insertHW(this, framepanel, new JLabel("Maximum Iterations:"), 0, 1);
        gridManager.insert(this, framepanel, txtiter, 1, 1, 1, 1, 4, 4, 0, 0, gridManager.getInsets(2), 0, 17);
        gridManager.insertHW(this, framepanel, new JLabel("Maximum Change:"), 0, 2);
        gridManager.insert(this, framepanel, txtchan, 1, 2, 1, 1, 4, 4, 0, 0, gridManager.getInsets(2), 0, 17);
    }

    public void updateControls()
    {
        Book book = getWorkBook();
        chkauto.setSelected(book.isAutoRecalc());
        chkiter.setSelected(book.isIterationEnabled());
        txtiter.setText(Integer.toString(book.getIterationMax()));
        txtchan.setText(Double.toString(book.getIterationMaxChange()));
    }

    protected void setOptions()
        throws Throwable
    {
        Book book = getWorkBook();
        book.setIterationMax(getnumint(txtiter));
        book.setIterationMaxChange(getTextfieldValue(txtchan));
        book.setAutoRecalc(chkauto.isSelected());
        book.setIterationEnabled(chkiter.isSelected());
    }
}
