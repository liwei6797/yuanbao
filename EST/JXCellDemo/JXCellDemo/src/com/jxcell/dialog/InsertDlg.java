package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.ss.UndoableEdit;
import com.jxcell.ss.TArea;


public class InsertDlg extends DeleteDlg
{

    public InsertDlg(View view)
        throws CellException
    {
        super(view);
        setTitle("Insert");
    }

    void edit(TArea trange1, short shift)
        throws CellException
    {
        UndoableEdit viewUndoableEdit = getEdit(0x4400017);
        if(viewUndoableEdit != null)
            addEdit(viewUndoableEdit);
        m_view.insertRange(trange1.getRow1(), trange1.getCol1(), trange1.getRow2(), trange1.getCol2(), shift);
        fireUndoableEdit(viewUndoableEdit);
    }

}
