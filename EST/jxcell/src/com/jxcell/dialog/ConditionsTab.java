package com.jxcell.dialog;

import com.jxcell.CellFormat;
import com.jxcell.ss.ConditionalFormat;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;


class ConditionsTab extends TabBase
{
    private JTextField m_txtCell1;
    private JTextField m_txtCell2;
    private JTextField m_txtFormula;

    private JRadioButton[] m_rdoConditions;
    private JRadioButton[] m_rdoValues;
    private JComboBox m_cboOperator;
    private JLabel m_lblBetween;
    private JButton m_btnAdd;
    private JButton m_btnDelete;

    ConditionsTab(TabDialog tabDialog)
    {
        super(tabDialog);
        m_txtCell1 = new JTextField("", 10);
        m_txtCell2 = new JTextField("", 10);
        m_txtFormula = new JTextField("", 10);
        m_rdoConditions = getJRadioButtons("Condition 1;1'Condition 2;2'Condition 3;3");
        m_rdoConditions[0].setSelected(true);
        m_rdoValues = getJRadioButtons("Cell Value;C'Formula;F");
        m_btnAdd = getJButton("Add;D");
        m_btnDelete = getJButton("Delete;E");
        m_lblBetween = new JLabel("and");
        m_cboOperator = getJComboBox(false, "between;not between;equal to;not equal to;greater than;less than;greater than or equal to;less than or equal to");
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Criteria");
        GridManager gridManager = new GridManager();
        gridManager.insertWithInsetHW(this, this, m_rdoConditions[0], 0, 0, 1, 1, gridManager.getInsets(0));
        gridManager.insert(this, this, m_btnAdd, 1, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(2), 2, 10);
        gridManager.insertWithInsetHW(this, this, m_rdoConditions[1], 0, 1, 1, 1, gridManager.getInsets(0));
        gridManager.insert(this, this, m_btnDelete, 1, 1, 1, 1, 0, 0, 0, 0, gridManager.getInsets(2), 2, 10);
        gridManager.insertWithInsetHW(this, this, m_rdoConditions[2], 0, 2, 1, 1, gridManager.getInsets(0));
        gridManager.insertWithInsetHW(this, this, framepanel, 0, 3, 5, 3, gridManager.getInsets(2));
        gridManager.insert(this, framepanel, m_rdoValues[0], 0, 0);
        gridManager.insert(this, framepanel, m_cboOperator, 1, 0);
        gridManager.insert(this, framepanel, m_txtCell1, 2, 0);
        gridManager.insert(this, framepanel, m_lblBetween, 3, 0);
        gridManager.insert(this, framepanel, m_txtCell2, 4, 0);
        gridManager.insert(this, framepanel, m_rdoValues[1], 0, 1);
        gridManager.insert(this, framepanel, m_txtFormula, 1, 1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == m_rdoConditions[0])
            ((CondFormatDlg) dlgBase).initFormats(0);
        else
        if(obj == m_rdoConditions[1])
            ((CondFormatDlg) dlgBase).initFormats(1);
        else
        if(obj == m_rdoConditions[2])
            ((CondFormatDlg) dlgBase).initFormats(2);
        else
        if(obj == m_btnAdd)
        {
            ((CondFormatDlg) dlgBase).addConditionalFormat();
            setConditionIndex(((CondFormatDlg) dlgBase).getCurConditionalFormatIndex());
        } else
        if(obj == m_btnDelete)
        {
            ((CondFormatDlg) dlgBase).deleteCurConditionalFormat();
            setConditionIndex(((CondFormatDlg) dlgBase).getCurConditionalFormatIndex());
        }
        enableComponents();
        super.actionPerformed(actionevent);
    }

    private void setConditionIndex(int conds)
    {
        m_rdoConditions[0].setSelected(conds == 0);
        m_rdoConditions[1].setSelected(conds == 1);
        m_rdoConditions[2].setSelected(conds == 2);
    }

    public void updateControls()
    {
        initCriteria();
        enableComponents();
    }

    private void initCriteria()
    {
        ConditionalFormat conditionalformat = ((CondFormatDlg) dlgBase).getCurConditionalFormat();
        short type = conditionalformat.getType();
        m_rdoValues[0].setSelected(type == 1);
        m_rdoValues[1].setSelected(type == 2);
        m_cboOperator.setSelectedIndex(conditionalformat.getOperator() - 1);
        int row1 = m_view.getActiveRow();
        int col1 = m_view.getActiveCol();
        m_txtCell1.setText(conditionalformat.getFormula1(row1, col1));
        m_txtCell2.setText(conditionalformat.getFormula2(row1, col1));
        m_txtFormula.setText(conditionalformat.getFormula1(row1, col1));
    }

    protected void enableComponents()
    {
        int count = ((CondFormatDlg) dlgBase).getConditionalFormatCount();
        boolean has = count > 0;
        m_rdoConditions[0].setEnabled(has);
        m_rdoConditions[1].setEnabled(count > 1);
        m_rdoConditions[2].setEnabled(count > 2);
        m_btnAdd.setEnabled(count < 3);
        m_btnDelete.setEnabled(has);
        if(has)
        {
            enableRdoCell();
            m_txtFormula.setEnabled(m_rdoValues[1].isSelected());
        }
        int i = m_cboOperator.getSelectedIndex();
        m_txtCell2.setVisible(i <= 1);
        m_lblBetween.setVisible(i <= 1);
    }

    private void enableRdoCell()
    {
        boolean iscell = m_rdoValues[0].isSelected();
        m_cboOperator.setEnabled(iscell);
        m_lblBetween.setEnabled(iscell);
        m_lblBetween.setEnabled(iscell);
        m_lblBetween.setEnabled(iscell);
    }

    void set(CellFormat cellformat)
        throws Throwable
    {
        ConditionalFormat conditionalformat = ((CondFormatDlg) dlgBase).getCurConditionalFormat();
        int row1 = m_view.getActiveRow();
        int col1 = m_view.getActiveCol();
        if(m_rdoValues[0].isSelected())
        {
            conditionalformat.setType((short)1);
            conditionalformat.setOperator((short)(m_cboOperator.getSelectedIndex() + 1));
            conditionalformat.setFormula1(m_txtCell1.getText(), row1, col1);
            conditionalformat.setFormula2(m_txtCell2.getText(), row1, col1);
        } else
        if(m_rdoValues[1].isSelected())
        {
            conditionalformat.setType((short)2);
            conditionalformat.setFormula1(m_txtFormula.getText(), row1, col1);
        }
    }

    void apply()
    {
        try
        {
            com.jxcell.CellFormat cellformat = getCellFormat();
            set(cellformat);
            setApplyButtonEnabled(true);
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        apply();
    }

    protected void setOptions()
        throws Throwable
    {
        apply();
    }
}