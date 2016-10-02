package com.jxcell.dialog;

import com.jxcell.RangeRef;
import com.jxcell.ss.DVRecord;
import com.jxcell.ss.Sheet;
import com.jxcell.ss.TArea;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.tools.JTextAreaEX;

import javax.swing.*;
import java.awt.event.ActionEvent;


class ValidationTab extends TabBase
{

    private JTextField errTitle;
    private JTextAreaEX errText;

    private JTextField m_txtCell1;
    private JTextField m_txtCell2;
    private JComboBox m_cboOperator;
    private JComboBox m_cbtype;

    private JLabel m_lbFormula1;
    private JLabel m_lbFormula2;

    ValidationTab(TabDialog tabDialog)
    {
        super(tabDialog);
        errTitle = new JTextField("", 25);
        errText = new JTextAreaEX(2, 25);

        m_txtCell1 = new JTextField("", 25);
        m_txtCell2 = new JTextField("", 25);
        m_lbFormula1 = new JLabel("and");
        m_lbFormula2 = new JLabel("and");
        m_cboOperator = getJComboBox(false, "between;not between;equal to;not equal to;greater than;less than;greater than or equal to;less than or equal to");
        m_cbtype = getJComboBox(false, "Any value;Integer values;Decimal values;User defined list;Date;Time;Text length;Formula");

        com.jxcell.tools.FramePanel criterialpanel = new FramePanel("Validation criteria");
        com.jxcell.tools.FramePanel errpanel = new FramePanel("Error message");

        GridManager gridManager = new GridManager();

        gridManager.insertWithInsetHW(this, this, criterialpanel, 0, 0, 2, 4, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, this, errpanel, 0, 5, 2, 4, gridManager.getInsets(2));

        gridManager.insert(this, criterialpanel, new JLabel("Allow:"), 0, 0);
        gridManager.insert(this, criterialpanel, m_cbtype, 1, 0);

        gridManager.insert(this, criterialpanel, new JLabel("Data:"), 0, 1);
        gridManager.insert(this, criterialpanel, m_cboOperator, 1, 1);

        gridManager.insert(this, criterialpanel, m_lbFormula1, 0, 2);
        gridManager.insert(this, criterialpanel, m_txtCell1, 1, 2);

        gridManager.insert(this, criterialpanel, m_lbFormula2, 0, 3);
        gridManager.insert(this, criterialpanel, m_txtCell2, 1, 3);

        gridManager.insert(this, errpanel, new JLabel("Title:"), 0, 0);
        gridManager.insertWithInset(this, errpanel, errTitle, 1, 0, 2, 1, gridManager.getInsets(0));

        gridManager.insert(this, errpanel, new JLabel("Message:"), 0, 1);
        gridManager.insertWithInset(this, errpanel, errText, 1, 1, 2, 2, gridManager.getInsets(0));
    }

    public JComponent getFocusComponent()
    {
        return errText.getJTextArea();
    }

    public void updateControls()
    {
        initCriteria();
        int type = m_cbtype.getSelectedIndex();
        int operator = m_cboOperator.getSelectedIndex();
        enableControls(type, operator);
    }

    private void enableControls(int type, int operator)
    {
        m_lbFormula1.setEnabled(type != 0);
        m_txtCell1.setEnabled(type != 0);
        errTitle.setEnabled(true);
        errText.setEnabled(true);
        if(type == 0)
        {
            m_cboOperator.setEnabled(false);
            m_lbFormula2.setEnabled(false);
            m_txtCell2.setEnabled(false);
            errTitle.setEnabled(false);
            errText.setEnabled(false);
        }
        else if(type == 1 || type == 2 || type == 6)
        {
            m_lbFormula1.setText("Min value:");
            m_lbFormula2.setText("Max value:");
            m_cboOperator.setEnabled(true);
            m_txtCell2.setEnabled(true);
            m_lbFormula2.setEnabled(operator <= 1);
            m_txtCell2.setEnabled(operator <= 1);
        }
        else if(type == 3)
        {
            m_lbFormula1.setText("Source:");
            m_cboOperator.setEnabled(false);
            m_txtCell2.setEnabled(false);
            m_lbFormula2.setEnabled(false);
            m_txtCell2.setEnabled(false);
        }
        else if(type == 4)
        {
            m_lbFormula1.setText("Start date:");
            m_lbFormula2.setText("End date:");
            m_cboOperator.setEnabled(true);
            m_txtCell2.setEnabled(true);
            m_lbFormula2.setEnabled(operator <= 1);
            m_txtCell2.setEnabled(operator <= 1);
        }
        else if(type == 5)
        {
            m_lbFormula1.setText("Start time:");
            m_lbFormula2.setText("End time:");
            m_cboOperator.setEnabled(true);
            m_txtCell2.setEnabled(true);
            m_lbFormula2.setEnabled(operator <= 1);
            m_txtCell2.setEnabled(operator <= 1);
        }
        else if(type == 7)
        {
            m_lbFormula1.setText("Formula:");
            m_cboOperator.setEnabled(false);
            m_txtCell2.setEnabled(false);
            m_lbFormula2.setEnabled(false);
            m_txtCell2.setEnabled(false);
        }
    }

    protected void setOptions()
        throws Throwable
    {
        int row1 = m_view.getActiveRow();
        int col1 = m_view.getActiveCol();
        Sheet sheet = m_view.getSelSheet();
        DVRecord dv = sheet.getValidation(row1, col1);
        if(dv == null)
        {
            dv = new DVRecord(sheet);
        }
        int type = m_cbtype.getSelectedIndex();
        dv.setType(type);
        dv.setOperator(m_cboOperator.getSelectedIndex());
        String f1 = m_txtCell1.getText();
        if(type == 3 && f1.length() > 0)
        {
            if(!f1.startsWith("="))
            {
                f1 = "\"" + f1;
                f1 = f1.replaceAll(",", "\0");
                f1 += "\"";
            }
            else
            {
                f1 = f1.substring(1);
            }
         }
        dv.setFormula1(f1);
        f1 = m_txtCell2.getText();
        if(type == 3 && f1.length() > 0)
        {
            if(!f1.startsWith("="))
            {
                f1 = "\"" + f1;
                f1 = f1.replaceAll(",", "\0");
                f1 += "\"";
            }
            else
            {
                f1 = f1.substring(1);
            }
         }
        dv.setFormula2(f1);
        dv.setErrorBoxTitle(errTitle.getText());
        dv.setErrorBoxText(errText.getText());
        int selcount = m_view.getSelectionCount();
        TArea[] ranges = new TArea[selcount];
        for(int i=0; i<selcount; i++)
        {
            RangeRef ref = m_view.getSelection(i);
            ranges[i] = new TArea(ref.getRow1(),ref.getCol1(),ref.getRow2(),ref.getCol2());

        }
        sheet.addValidation(dv, ranges);
    }

    private void initCriteria()
    {
        m_cbtype.setSelectedIndex(0);
        m_cboOperator.setSelectedIndex(0);
        m_txtCell1.setText("");
        m_txtCell2.setText("");
        errTitle.setText("");
        errText.setText("");
        int row1 = m_view.getActiveRow();
        int col1 = m_view.getActiveCol();
        Sheet sheet = m_view.getSelSheet();
        DVRecord dv = sheet.getValidation(row1, col1);
        if(dv == null)
            return;
        int type = dv.getType();
        m_cbtype.setSelectedIndex(type);

        int operator = dv.getOperator();
        m_cboOperator.setSelectedIndex(operator);
        String f1 = dv.getFormula1();
        if(type == 3 && f1 != null)
        {
            f1 = f1.replaceAll("\"", "");
            f1 = f1.replaceAll("\0", ",");
            if(!dv.isStringToken())
            {
                f1 = "=" + f1;
            }
        }
        m_txtCell1.setText(f1);
        m_txtCell2.setText(dv.getFormula2());
        errTitle.setText(dv.getErrorBoxTitle());
        errText.setText(dv.getErrorBoxText());
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        int type = m_cbtype.getSelectedIndex();
        int operator = m_cboOperator.getSelectedIndex();
        enableControls(type, operator);
        super.actionPerformed(actionevent);
    }
}
