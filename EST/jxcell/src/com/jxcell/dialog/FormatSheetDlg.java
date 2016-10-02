package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;


public class FormatSheetDlg extends TabDialog
{

    public static final int kAllPages = 31;
    public static final int kColorPage = 16;
    public static final int kEditPage = 4;
    public static final int kGeneralPage = 1;
    public static final int kSelectionPage = 8;
    public static final int kViewPage = 2;
    static int m_nTab;

    public FormatSheetDlg(View view)
        throws CellException
    {
        this(view, 31);
    }

    public FormatSheetDlg(View view, int option)
        throws CellException
    {
        super(view, "Format Sheet", false);
        checkOptions(1, option, 31);
        if((option & 1) != 0)
            insertTabPanel("General;General sheet properties", new General2Tab(this));
        if((option & 2) != 0)
            insertTabPanel("View;Change view attributes", new ViewTab(this));
        if((option & 4) != 0)
            insertTabPanel("Edit;Enable and disable edit options", new EditTab(this));
        if((option & 8) != 0)
            insertTabPanel("Selection;Set selection options", new SelectionTab(this));
        if((option & 0x10) != 0)
            insertTabPanel("Color;Format sheet color", new ColorTab(this));
    }

    protected boolean onApply()
    {
        getLock();
        try
        {
            try
            {
                if(m_view.isSheetProtected(m_view.getSheet()))
                    setSelectTabIndex((short)29);
                return super.onApply();
            }
            catch(Throwable throwable)
            {
                showMessage(throwable);
            }
            return false;
        }
        finally
        {
            releaseLock();
        }
    }

    protected int getSelectedTabIndex()
    {
        return m_nTab;
    }

    protected void setSelectTabIndex(int i)
    {
        m_nTab = i;
    }
}
