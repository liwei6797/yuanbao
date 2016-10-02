package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.ss.*;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import java.awt.event.WindowEvent;


public class CondFormatDlg extends TabDialog
{
    static int tabIndex;
    TabBase tabBase;

    private ConditionalFormat m_conditionalFormats[];
    private com.jxcell.CellFormat m_curCellFormat;
    private int m_iCurFormatIndex;


    public CondFormatDlg(com.jxcell.View view)
        throws CellException
    {
        super(view, "Conditional Format Cells", true);
        insertTabPanel("Condition Format;Conditional Format Cell", new ConditionsTab(this));
        insertTabPanel("Font;Format font colors and styles", new FontTab(this));
        insertTabPanel("Border;Format border colors and styles", new BorderTab(this));
        insertTabPanel("Fill;Format fill colors and patterns", new FillTab(this));

        Selection selection = getSSView().getSelection();
        Sheet sheet = getSSView().getSheet();
        m_conditionalFormats = sheet.getConditionalFormats(getAreaArray(selection.getAreas()));
        m_iCurFormatIndex = m_conditionalFormats.length <= 0 ? -1 : 0;
        if(m_iCurFormatIndex == 0)
        {
            initFormats(0);
        }
    }

    private TAreaConst[] getAreaArray(TArea.Array array)
    {
        int i = array.getSize();
        TAreaConst atareaconst[] = new TAreaConst[i];
        System.arraycopy(((Object) (array.getObjects())), 0, atareaconst, 0, i);
        return atareaconst;
    }

    protected int getSelectedTabIndex()
    {
        return tabIndex;
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
                int tabs = jtabbedpane.getTabCount();
                for(int i = 0; i < tabs; i++)
                {
                    TabBase tabBase = getTab(i);
                    if(!tabBase.setOptions(undoableEdit))
                    {
                        jtabbedpane.setSelectedIndex(i);
                        return false;
                    }
                }
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

    protected void onOK()
    {
        super.onOK();
        Selection selection = getSSView().getSelection();
        getSSView().getSheet().setConditionalFormats(m_conditionalFormats, getAreaArray(selection.getAreas()));
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

    void addConditionalFormat()
    {
        int len = getConditionalFormatCount();
        ConditionalFormat aconditionalformat[] = new ConditionalFormat[len + 1];
        System.arraycopy(m_conditionalFormats, 0, aconditionalformat, 0, len);
        aconditionalformat[len] = createFormat();
        m_conditionalFormats = aconditionalformat;
        initFormats(len);
    }

    void deleteCurConditionalFormat()
    {
        int len = getConditionalFormatCount();
        m_conditionalFormats[m_iCurFormatIndex] = null;
        ConditionalFormat aconditionalformat[] = new ConditionalFormat[len - 1];
        int j = 0;
        for(int i = 0; i < len; i++)
        {
            ConditionalFormat conditionalformat = m_conditionalFormats[i];
            if(conditionalformat != null)
                aconditionalformat[j++] = conditionalformat;
        }

        m_conditionalFormats = aconditionalformat;
        int index = m_iCurFormatIndex - 1;
        if(index == -1 && getConditionalFormatCount() > 0)
            index = 0;
        initFormats(index);
    }

    int getCurConditionalFormatIndex()
    {
        return m_iCurFormatIndex;
    }

    void initFormats(int index)
    {
        try
        {
            m_iCurFormatIndex = index;
            setdefault();
        }
        catch (Throwable throwable)
        {
        }
    }

    int getConditionalFormatCount()
    {
        return m_conditionalFormats.length;
    }

    ConditionalFormat getCurConditionalFormat()
    {
        if(m_iCurFormatIndex < 0)
        {
            addConditionalFormat();
            m_iCurFormatIndex =0;
        }
        return m_conditionalFormats[m_iCurFormatIndex];
    }

    private ConditionalFormat createFormat()
    {
        return new ConditionalFormat(getSSView().getSheet());
    }

    protected void setSelectTabIndex(int i)
    {
        tabIndex = i;
    }
}