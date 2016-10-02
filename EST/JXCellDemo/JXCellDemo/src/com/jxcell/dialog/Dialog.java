package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.ss.*;
import com.jxcell.tools.DlgBase;
import com.jxcell.util.CharBuffer;

import java.awt.event.WindowEvent;

abstract class Dialog extends DlgBase
{

    private CharBuffer charbuffer;
    View m_view;

    Dialog(View view, String title, boolean model)
    {
        super(view, title, model);
        m_view = view;
        charbuffer = new CharBuffer();
        childIsActive();
    }

    int getActiveCol()
    {
        return getSSView().getActiveCol();
    }

    int getActiveRow()
    {
        return getSSView().getActiveRow();
    }

    Book getWorkBook()
    {
        return getSSView().getBook();
    }

    CharBuffer getCharBuffer()
    {
        charbuffer.clear();
        return charbuffer;
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

    com.jxcell.ss.Controller getController()
    {
        return (com.jxcell.ss.Controller) getSSView().getController();
    }

    com.jxcell.ss.UndoableEdit getEdit(int masks)
    {
        if(wantsUndoableEdit())
        {
            getLock();
            try
            {
                try
                {
                    return (UndoableEdit) m_view.getUndoableRegionEdit(getTitle());
                }
                catch(Exception e)
                {
                    showMessage(e);
                }
                return null;
            }
            finally
            {
                releaseLock();
            }
        } else
        {
            return null;
        }
    }

    public void getLock()
    {
        m_view.getLock();
    }

    protected com.jxcell.View getView()
    {
        return m_view;
    }

    TArea getRange(int index)
    {
        return (TArea) getSelection().getAreaConst(index);
    }

    public void releaseLock()
    {
        m_view.releaseLock();
    }

    Selection getSelection()
    {
        return getSSView().getSelection();
    }

    int getNrRanges()
    {
        return getSelection().getAreaCount();
    }

    Sheet getSheet()
    {
        return getSSView().getSheet();
    }

    com.jxcell.ss.SSView getSSView()
    {
        return (com.jxcell.ss.SSView) m_view.getView();
    }

    void showException(short i)
        throws CellException
    {
        throw new CellException(i);
    }

    public void windowActivated(WindowEvent windowevent)
    {
        childIsActive();
    }

    public void windowClosed(WindowEvent windowevent)
    {
        super.windowClosed(windowevent);
        m_view = null;
        charbuffer = null;
    }
}
