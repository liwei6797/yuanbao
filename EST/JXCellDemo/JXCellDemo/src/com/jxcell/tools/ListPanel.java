package com.jxcell.tools;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.EventListener;

public class ListPanel extends JScrollPane
{

    private JList m_list;
    private DefaultListModel m_listModel;
    private boolean m_baddtofirst;

    public ListPanel()
    {
        m_baddtofirst = false;
        m_list = new JList();
        m_listModel = new DefaultListModel();
        m_list.setModel(m_listModel);
        setViewportView(m_list);
    }

    public void addElement(String item)
    {
        if(m_baddtofirst)
        {
            int i = m_listModel.getSize();
            for(int j = 0; j < i; j++)
                if(item.compareTo(getElementAt(j)) < 0)
                {
                    m_listModel.insertElementAt(item, j);
                    return;
                }

        }
        m_listModel.addElement(item);
    }

    public void addListSelectionListener(EventListener eventlistener)
    {
        m_list.addListSelectionListener((ListSelectionListener)eventlistener);
    }

    public void ensureIndexIsVisible(int i)
    {
        m_list.ensureIndexIsVisible(i);
    }

    public String getElementAt(int i)
    {
        return (String)m_listModel.getElementAt(i);
    }

    public int getListSize()
    {
        return m_listModel.getSize();
    }

    public JList getList()
    {
        return m_list;
    }

    public int getSelectedIndex()
    {
        return m_list.getSelectedIndex();
    }

    public int[] getSelectedIndices()
    {
        return m_list.getSelectedIndices();
    }

    public String getSelectedValue()
    {
        return (String)m_list.getSelectedValue();
    }

    public void removeAllElements()
    {
        m_listModel.removeAllElements();
    }

    public void removeElement(String item)
    {
        m_listModel.removeElement(item);
    }

    public void removeListSelectionListener(EventListener eventlistener)
    {
        m_list.removeListSelectionListener((ListSelectionListener)eventlistener);
    }

    public void requestFocus()
    {
        m_list.requestFocus();
    }

    public void setSelectedIndex(int index)
    {
        m_list.setSelectedIndex(index);
    }

    public void setSelectedValue(String obj)
    {
        m_list.setSelectedValue(obj, false);
    }

    public void setSelectionMode(int selMode)
    {
        m_list.setSelectionMode(selMode);
    }

    public void setaddtofirst(boolean addtoFirst)
    {
        m_baddtofirst = addtoFirst;
    }
}
