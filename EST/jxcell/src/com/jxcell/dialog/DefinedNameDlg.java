package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.RangeRef;
import com.jxcell.View;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.JTextAreaEX;
import com.jxcell.tools.ListPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;


public class DefinedNameDlg extends Dialog
{

    private ListPanel lstnames;
    private JTextAreaEX txtformula;
    private JTextField txtname;
    private JButton btnadd;
    private JButton btndelete;

    public DefinedNameDlg(View view)
    {
        super(view, "Defined Name", true);
        txtname = new JTextField(null, 0);
        lstnames = newListPanel(8, 160);
        lstnames.setaddtofirst(true);
        txtformula = new JTextAreaEX(6, 30);
        btnadd = newJButton("Add;A");
        btndelete = newJButton("Delete;D");
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, container, new JLabel("Name:"), 0, 0);
        gridManager.insertHW(this, container, txtname, 0, 1);
        gridManager.insert(this, container, lstnames, 0, 2, 1, 4);
        gridManager.insertWithInsetHW(this, container, new JLabel("Formula:"), 1, 4, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetBC(this, container, txtformula, 1, 5, 2, 1, gridManager.getInsets(2));
        gridManager.insertHN(this, container, this.btOK, 2, 0);
        gridManager.insertHN(this, container, this.btCancel, 2, 1);
        gridManager.insertHN(this, container, btnadd, 2, 2);
        gridManager.insertHN(this, container, btndelete, 2, 3);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        getLock();
        try
        {
            Object obj = actionevent.getSource();
            if(obj == btnadd)
                okClicked();
            else
            if(obj == btndelete)
            {
                String name = lstnames.getSelectedValue();
                if(name != null)
                {
                    m_view.deleteDefinedName(name);
                    txtname.setText("");
                    txtformula.setText("");
                    lstnames.removeElement(name);
                    btndelete.setEnabled(false);
                } else
                {
                    showMessage("You must select a defined name from the list to delete.");
                }
            } else
            {
                super.actionPerformed(actionevent);
            }
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
        finally
        {
            releaseLock();
        }
    }

    private void getNames()
    {
        int names = m_view.getDefinedNameCount();
        lstnames.removeAllElements();
        for(int i = 0; i < names; i++)
        {
            try
            {
                String definedName1 = m_view.getDefinedName(i);
                if(!m_view.isHiddenDefinedName(i))
                lstnames.addElement(definedName1);
            }
            catch (CellException e)
            {
            }
        }
    }

    protected JComponent getFocusComponent()
    {
        return txtname;
    }

    protected void setdefault()
    {
        StringBuffer stringbuffer = new StringBuffer();
        int rangeCount = m_view.getSelectionCount();
        for(int i = 0; i < rangeCount; i++)
        {
            try
            {
                RangeRef rangeRef = m_view.getSelection(i);
                String address = "";
                if(i > 0)
                    stringbuffer.append(";");
                String sheetname = m_view.getSheetName(m_view.getSheet());
                address = address + sheetname + "!";
                String range = m_view.formatRCNr(rangeRef.getRow1(), rangeRef.getCol1(), rangeRef.getRow2(), rangeRef.getCol2(), true);
                address += range;
                stringbuffer.append(address.toString());
            }
            catch (CellException e)
            {
            }
        }

        txtformula.setText(stringbuffer.toString());
        getNames();
        btnadd.setEnabled(false);
        btndelete.setEnabled(false);
    }

    protected void okClicked()
        throws Throwable
    {
        String name = txtname.getText();
        String formula = txtformula.getText();
        if(name.length() != 0 && formula.length() != 0)
        {
            m_view.setDefinedName(name, formula);
            getNames();
        }
    }

    protected void setSelected()
    {
        btnadd.setEnabled(txtname.getText().length() > 0 && txtformula.getText().length() > 0);
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        getLock();
        try
        {
            String name = lstnames.getSelectedValue();
            if(name != null)
            {
                String formula = m_view.getDefinedName(name);
                txtformula.setText(formula);
                txtname.setText(name);
                txtname.requestFocus();
                btndelete.setEnabled(true);
            }
        }
        catch(Exception e){}
        finally
        {
            releaseLock();
        }
    }
}
