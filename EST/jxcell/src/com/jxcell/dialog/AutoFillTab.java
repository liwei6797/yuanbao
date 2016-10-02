package com.jxcell.dialog;

import com.jxcell.ss.AutoFiller;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.JTextAreaEX;
import com.jxcell.util.Array;
import com.jxcell.util.CharBuffer;

import javax.swing.*;
import java.awt.event.ActionEvent;


class AutoFillTab extends TabBase
{

    private JButton btnadd;
    private JButton btndelete;
    private JComboBox m_cboLists;
    private JTextAreaEX txtcurlist;
    private Array m_items;

    AutoFillTab(TabDialog tabDialog)
    {
        super(tabDialog);
        m_items = new Array(java.lang.String.class);
        txtcurlist = new JTextAreaEX(4, 25);
        m_cboLists = getJComboBox(false);
        btnadd = getJButton("Add;A");
        btndelete = getJButton("Delete;D");
        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, this, new JLabel("AutoFill Lists:"), 0, 0);
        gridManager.insert(this, this, m_cboLists, 0, 1, 1, 1, 0, 0, 60, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insertHW(this, this, new JLabel("Current List:"), 0, 3);
        gridManager.insert(this, this, txtcurlist, 0, 4, 2, 1);
        gridManager.insertHN(this, this, btnadd, 1, 1);
        gridManager.insertHN(this, this, btndelete, 1, 2);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == btnadd)
        {
            Array array1 = m_items;
            CharBuffer charBuffer1 = new CharBuffer(txtcurlist.getText());
            array1.add(charBuffer1.toString());
            fillList();
            setApplyButtonEnabled(true);
            return;
        }
        if(obj == btndelete)
        {
            m_items.delete(m_cboLists.getSelectedIndex(), 1);
            fillList();
            setApplyButtonEnabled(true);
            return;
        }
        if(obj == m_cboLists)
        {
            int i = m_cboLists.getSelectedIndex();
            if(i < m_cboLists.getItemCount() - 1)
            {
                txtcurlist.setText((i < (m_items).getSize() ? m_items.getObject(i) : null).toString());
                btndelete.setEnabled(true);
            } else
            {
                txtcurlist.setText("");
                btndelete.setEnabled(false);
            }
            btnadd.setEnabled(false);
            txtcurlist.repaint();
        }
    }

    private void fillList()
    {
        if(m_cboLists.getItemCount() > 0)
            m_cboLists.removeAllItems();
        Array array1 = m_items;
        int size = array1.getSize();
        String item = "List Number ";
        for(int i = 1; i <= size; i++)
            m_cboLists.addItem(item + Integer.toString(i));

        m_cboLists.addItem("(New List)");
        m_cboLists.setSelectedIndex(size);
        txtcurlist.setText("");
        btnadd.setEnabled(false);
        btndelete.setEnabled(false);
    }

    public JComponent getFocusComponent()
    {
        return txtcurlist.getJTextArea();
    }

    public void updateControls()
    {
        AutoFiller autofiller = getWorkBook().getAutoFiller();
        int i = autofiller.getSize();
        for(int j = 0; j < i; j++)
        {
            Array array1 = m_items;
            CharBuffer charBuffer1 = new CharBuffer(autofiller.getItems(j));
            array1.add(charBuffer1.toString());
        }

        fillList();
    }

    protected void setOptions()
        throws Throwable
    {
        AutoFiller autoFiller = getWorkBook().getAutoFiller();
        Array array1 = m_items;
        int i = array1.getSize();
        autoFiller.setSize(0, false);
        for(int j = 0; j < i; j++)
        {
            autoFiller.setItems(j, (j < m_items.getSize() ? m_items.getObject(j) : null).toString());
        }

    }

    public void setEnabled()
    {
        btnadd.setEnabled(txtcurlist.getText().length() > 0);
        btndelete.setEnabled(false);
    }
}
