package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.ss.*;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;


public class FormatCellsDlg extends TabDialog
{

    static int tabIndex;
    com.jxcell.CellFormat cellFormat;
    public static final int kAlignmentPage = 2;
    public static final int kAllPages = 127;
    public static final int kBorderPage = 8;
    public static final int kFillPage = 16;
    public static final int kFontPage = 4;
    public static final int kNumberPage = 1;
    public static final int kProtectionPage = 32;
    public static final int kValidationPage = 64;
    TabBase tabBase;

    public FormatCellsDlg(View view)
        throws CellException
    {
        this(view, 127);
    }

    public FormatCellsDlg(View view, int option)
        throws CellException
    {
        super(view, "Format Cells", false);
        checkOptions(1, option, 127);
        boolean bnHdr = !isHdrSelected();
        if((option & 1) != 0 && bnHdr)
            insertTabPanel("Number;Format number formats", new NumberTab(this));
        if((option & 2) != 0)
            insertTabPanel("Alignment;Format the alignment", new AlignmentTab(this));
        if((option & 4) != 0)
            insertTabPanel("Font;Format font colors and styles", new FontTab(this));
        if((option & 8) != 0)
            insertTabPanel("Border;Format border colors and styles", new BorderTab(this));
        if((option & 0x10) != 0)
            insertTabPanel("Fill;Format fill colors and patterns", new FillTab(this));
        if((option & 0x20) != 0 && bnHdr)
            insertTabPanel("Protection;Lock or hide cells", new ProtectionTab(this));
        if((option & 0x40) != 0 && bnHdr)
            insertTabPanel("Validation;Set validation rules", new ValidationTab(this));
    }

    protected boolean onApply()
    {
        if(isTabSelected())
        {
            getLock();
            try
            {
                if(m_view.isSheetProtected(m_view.getSheet()))
                    setSelectTabIndex((short)29);
                UndoableEdit undoableEdit = null;
                if(wantsUndoableEdit())
                    undoableEdit = getUndoableEdit();
                JTabbedPane jtabbedpane = getTabbedPane();
                int i = jtabbedpane.getTabCount();
                for(int j = 0; j < i; j++)
                {
                    TabBase tabBase = getTab(j);
                    if(tabBase.getSelected() && !tabBase.setOptions(undoableEdit))
                    {
                        jtabbedpane.setSelectedIndex(j);
                        return false;
                    }
                }
                m_view.setCellFormat(cellFormat);
                if(undoableEdit != null)
                    addEdit(undoableEdit);
                setTab(false);
            }
            catch(Throwable throwable)
            {
                showMessage(throwable);
                return false;
            }
            finally
            {
                releaseLock();
            }
        }
        return true;
    }

    protected int getSelectedTabIndex()
    {
        return tabIndex;
    }

    public com.jxcell.CellFormat getCellFormat()
    {
        if(cellFormat == null)
            cellFormat = m_view.getCellFormat();
        return cellFormat;
    }

    protected void init()
    {
        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Sample");
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insert(this, container, getTabbedPane(), 0, 0, 2, 1);
        gridManager.insert(this, container, framepanel, 0, 1, 1, 3, 1, 0, 0, 0, gridManager.getInsets(1), 1, 10);
        gridManager.insert(this, container, this.btOK, 1, 1, 1, 1, 0, 1, 0, 0, gridManager.getInsets(5), 2, 15);
        gridManager.insert(this, container, this.btApply, 1, 2, 1, 1, 0, 0, 0, 0, gridManager.getInsets(6), 2, 15);
        gridManager.insert(this, container, this.btCancel, 1, 3, 1, 1, 0, 0, 0, 0, gridManager.getInsets(6), 2, 15);
    }

    boolean isHdrSelected()
    {
        getLock();
        try
        {
            return m_view.isRowHeaderSelected() || m_view.isColHeaderSelected();
        }
        finally
        {
            releaseLock();
        }
    }

    protected void setSelectTabIndex(int i)
    {
        tabIndex = i;
    }
}
