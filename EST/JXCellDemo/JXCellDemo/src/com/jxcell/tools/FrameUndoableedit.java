package com.jxcell.tools;

import com.jxcell.mvc.UndoableEdit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


public class FrameUndoableedit extends AbstractUndoableEdit
{

    private UndoableEdit m_UndoableEdit;

    public FrameUndoableedit(UndoableEdit undoableEdit1)
    {
        m_UndoableEdit = undoableEdit1;
    }

    public void die()
    {
        m_UndoableEdit.die();
        m_UndoableEdit = null;
        super.die();
    }

    public String getPresentationName()
    {
        return m_UndoableEdit.getPresentationName();
    }

    public void redo()
        throws CannotRedoException
    {
        try
        {
            m_UndoableEdit.redo();
        }
        catch(Throwable _ex)
        {
            throw new CannotRedoException();
        }
        super.redo();
    }

    public void undo()
        throws CannotUndoException
    {
        try
        {
            m_UndoableEdit.undo();
        }
        catch(Throwable _ex)
        {
            throw new CannotUndoException();
        }
        super.undo();
    }
}
