package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;


public class PageSetupDlg extends TabDialog
{

    public static final int kAllPages = 15;
    public static final int kHeaderFooterPage = 4;
    public static final int kMarginsPage = 2;
    public static final int kPagePage = 8;
    public static final int kSheetPage = 1;
    private static int m_nTabIndex;

    public PageSetupDlg(View view)
        throws CellException
    {
        this(view, 15);
    }

    public PageSetupDlg(View view, int option)
        throws CellException
    {
        super(view, "Page Setup", true);
        checkOptions(1, option, 15);
        if((option & 8) != 0)
            insertTabPanel("Page;Page settings", new PageTab(this));
        if((option & 2) != 0)
            insertTabPanel("Margins;Set print margins", new MarginsTab(this));
        if((option & 4) != 0)
            insertTabPanel("Header & Footer;Set print headers and footers", new HeaderFooterTab(this));
        if((option & 1) != 0)
            insertTabPanel("Sheet;Sheet settings", new SheetTab(this));
    }

    protected int getSelectedTabIndex()
    {
        return m_nTabIndex;
    }

    protected void setSelectTabIndex(int i)
    {
        m_nTabIndex = i;
    }
}
