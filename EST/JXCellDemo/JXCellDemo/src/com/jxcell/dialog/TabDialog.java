package com.jxcell.dialog;


import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.ss.UndoableGlobalEdit;
import com.jxcell.tools.DlgBase;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;


public abstract class TabDialog extends DlgBase
{

    View m_view;
    private JTabbedPane tabbedPane;
    public JButton btApply;

    public TabDialog(View view, String title, boolean model)
    {
        super(view, title, model);
        tabbedPane = new JTabbedPane();
        btApply = new JButton("Apply");
        m_view = view;
        init();
        childIsActive();
    }

    public void getLock()
    {
        m_view.getLock();
    }

    public void releaseLock()
    {
        m_view.releaseLock();
    }

    protected com.jxcell.View getView()
    {
        return m_view;
    }

    com.jxcell.ss.SSView getSSView()
    {
        return (com.jxcell.ss.SSView) m_view.getView();
    }

    void checkOptions(int min, int value, int max)
        throws CellException
    {
        if(value < min || value > max)
            throw new CellException((short)2);
    }

    protected void init()
    {
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, container, tabbedPane, 0, 0, 4, 1);
        gridManager.insert(this, container, new JLabel(), 0, 1, 1, 1, 1, 0, 0, 0, gridManager.getInsets(1), 2, 10);
        gridManager.insert(this, container, this.btOK, 1, 1, 1, 1, 0, 0, 22, 0, gridManager.getInsets(1), 2, 10);
        gridManager.insert(this, container, btApply, 2, 1, 1, 1, 0, 0, 9, 0, gridManager.getInsets(12), 2, 10);
        gridManager.insert(this, container, this.btCancel, 3, 1, 1, 1, 0, 0, 0, 0, gridManager.getInsets(12), 2, 10);
    }

    private void childIsActive()
    {
        if(m_view != null)
        {
            getLock();
            try
            {
                m_view.childIsActive();
            }
            finally
            {
                releaseLock();
            }
        }
    }

    public void windowActivated(WindowEvent windowevent)
    {
        childIsActive();
    }

    public void windowClosed(WindowEvent windowevent)
    {
        setSelectTabIndex(tabbedPane.getSelectedIndex());
        int i = tabbedPane.getTabCount();
        for(int j = 0; j < i; j++)
            getTab(j).DestroyObj();
        super.windowClosed(windowevent);
        m_view = null;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == btApply)
        {
            onApply();
        } else
        {
            super.actionPerformed(actionevent);
        }
    }

    protected void insertTabPanel(String text, TabBase tabBase)
    {
        String strs[] = splitString(text);
        tabbedPane.addTab(strs.length <= 0 ? "" : strs[0], null, tabBase, strs.length <= 1 ? null : strs[1]);
    }

    protected boolean onApply()
    {
        if(isTabSelected())
        {
            getLock();
            try
            {
                UndoableEdit undoableEdit1 = null;
                if(wantsUndoableEdit())
                    undoableEdit1 = getUndoableEdit();
                int i = tabbedPane.getTabCount();
                for(int j = 0; j < i; j++)
                {
                    TabBase tabBase = getTab(j);
                    if(tabBase.getSelected() && !tabBase.setOptions(undoableEdit1))
                    {
                        tabbedPane.setSelectedIndex(j);
                        return false;
                    }
                }

                setTab(false);
                if(undoableEdit1 != null)
                    addEdit(undoableEdit1);
            }
            catch(CellException _ex) { }
            finally
            {
                releaseLock();
            }
        }
        return true;
    }

    protected UndoableEdit getUndoableEdit()
        throws CellException
    {
        return new UndoableGlobalEdit(m_view.getModelBook());
    }

    protected abstract int getSelectedTabIndex();

    protected abstract void setSelectTabIndex(int i);

    protected JComponent getFocusComponent()
    {
        return getTab(tabbedPane.getSelectedIndex()).getFocusComponent();
    }

    protected TabBase getTab(int i)
    {
        return (TabBase)tabbedPane.getComponentAt(i);
    }

    public JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    }

    public void undo(UndoableEdit undoableEdit1)
    {
        if(undoableEdit1 != null)
        {
            getLock();
            try
            {
                undoableEdit1.undo();
            }
            catch(Exception cellexception)
            {
                showMessage(cellexception);
            }
            finally
            {
                releaseLock();
            }
        }
    }

    protected void setdefault()
            throws Throwable
    {
        int i = tabbedPane.getTabCount();
        for(int j = 0; j < i; j++)
        {
            TabBase tabBase = getTab(j);
            tabBase.updateControls();
            tabBase.setEnabled();
            tabBase.setApplyButtonEnabled(false);
        }
    }

    protected boolean isTabSelected()
    {
        int i = tabbedPane.getTabCount();
        for(int j = 0; j < i; j++)
            if(getTab(j).getSelected())
                return true;

        return false;
    }

    protected void onOK()
    {
        if(onApply())
            dispose();
    }

    protected void setTab(boolean enabled)
    {
        int i = tabbedPane.getTabCount();
        for(int j = 0; j < i; j++)
            getTab(j).setApplyButtonEnabled(enabled);
    }

    public void show()
    {
        int i = getSelectedTabIndex();
        if(tabbedPane.getTabCount() > i)
            tabbedPane.setSelectedIndex(i);
        super.show();
    }

    public com.jxcell.CellFormat getCellFormat()
    {
        return null;
    }

}
