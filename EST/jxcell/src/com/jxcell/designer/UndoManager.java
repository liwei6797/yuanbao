package com.jxcell.designer;

import com.jxcell.mvc.UndoableEditListener;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.tools.FrameUndoableedit;

public class UndoManager
        implements UndoableEditListener
{

    private javax.swing.undo.UndoManager m_undoManager = new javax.swing.undo.UndoManager();

    public void clearUndoableEdits()
    {
    }

    public void undoableEditHappened(UndoableEdit undoableedit)
    {
        if(wantsUndoableEdit())
            m_undoManager.addEdit(new FrameUndoableedit(undoableedit));
    }

    public boolean wantsUndoableEdit()
    {
        return m_undoManager != null;
    }

    void redo()
    {
        if(wantsUndoableEdit())
            m_undoManager.redo();
    }

    void undo()
    {
        if(wantsUndoableEdit())
            m_undoManager.undo();
    }

    public boolean canUndo()
    {
        return m_undoManager.canUndo();
    }

    public boolean canRedo() 
    {
        return m_undoManager.canRedo();
    }
}
