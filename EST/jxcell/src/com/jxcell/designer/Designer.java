package com.jxcell.designer;

import com.jxcell.*;
import com.jxcell.dialog.*;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Designer extends JFrame
    implements ActionListener, ItemListener, UpdateListener
{

    private View m_view;
    private DesignerPanel dPanel;
    private CellMenuBar menu;
    private static boolean isApplication;
    private static int bookNumber;
    private static int windowNumber;

    /**
     * Create a new instance of cuom.jxcell.designer.Designer.
     * <p/>
     * @param view - the view to attach this designer to or null if a new view should be created.
     * @return designer
     */

    public static Designer newDesigner(View view)
    {
        return new Designer(view);
    }

    /**
     * Default constructor for the Designer class.
     * <p/>
     * This is the default constructor for the Designer class.
     * A new workbook will be created to display in this Designer.
     */

    public Designer()
    {
        this(null);
    }

    /**
     * Primary constructor for the Designer class.
     * <p/>
     * @param view - the view to attach this designer to or null if a new view should be created.
     */

    public Designer(View view)
    {
        try
        {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e)
        {
        }
        dPanel = new DesignerPanel(view, isApplication);
        getContentPane().add(dPanel, "Center");
        m_view = dPanel.getView();
        setJMenuBar(menu = new CellMenuBar(new LocalDesignerInfo(), this, isApplication));
        try
        {
            setTitle(LocalDesignerInfo.getString((short)0));
            if(isApplication)
            {
                m_view.setGroup("com.jxcell.designer.Designer.group");
                m_view.setShowEditBar(true);
                String bookname = m_view.getWorkbookName();
                if(bookname.equals("Untitled"))
                    m_view.setWorkbookName(bookname = "Untitled" + ++bookNumber);
                setTitle(getTitle() + " - " + bookname);
            }
        }
        catch(CellException cellexception)
        {
            m_view.messageBox(cellexception.getMessage(), null, (short)1);
            m_view.requestFocus();
        }
        addFocusListener(dPanel);
        m_view.addUpdateListener(this);
        setDefaultCloseOperation ( JFrame. DO_NOTHING_ON_CLOSE );
        addWindowListener( new WindowAdapter()
        {
            public void windowClosing ( WindowEvent w )
            {
                dispose();
            }
        }
        );
        dPanel.clearModified();
        Dimension dimension = getToolkit().getScreenSize();
        int i = (windowNumber++ % 8) * 20;
        int j = (dimension.width * 65) / 100;
        setBounds((dimension.width * 10) / 100 + i, (dimension.height * 10) / 100 + i, j, ((j >= dimension.height ? dimension.height : j) * 75) / 100);
        show();
    }

    public void dispose()
    {
        CellException cellexception1;
        try
        {
            if(isApplication && dPanel.getModified())
            {
                String msg = LocalDesignerInfo.getString((short)2);
                int ret = showMessage(msg, 14);
                if(ret == 4)
                {
                    int ret1 = dPanel.fileSave(false);
                    if(ret1 == 2)
                        return;
                } else
                if(ret == 2)
                {
                    return;
                }
            }
            m_view.initWorkbook();
            super.dispose();
            return;
        }
        catch(CellException cellexception)
        {
            cellexception1 = cellexception;
        }
        m_view.messageBox(cellexception1.getMessage(), null, (short)1);
        m_view.requestFocus();
    }

    public View getView()
    {
        return m_view;
    }

    public CellMenuBar getMenu()
    {
        return menu;
    }

    private int showMessage(String msg, int type)
    {
        int ret = m_view.messageBox(msg, null, (short)type);
        m_view.requestFocus();
        return ret;
    }

    public static void main(String args[])
    {
        isApplication = true;
        int i = 0;
        do
        {
            Designer designer = new Designer(null);
            if(i >= args.length || designer.dPanel.read(args[i]))
                continue;
            if(i > 0)
            {
                designer.dispose();
                return;
            }
            break;
        } while(++i < args.length);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            Object obj = actionevent.getSource();
            if(obj == menu.fileNew)
                dPanel.fileNew();
            else
            if(obj == menu.fileOpen)
                dPanel.fileOpen();
            else
            if(obj == menu.fileClose)
                dispose();
            else
            if(obj == menu.fileSave)
                dPanel.fileSave(false);
            else
            if(obj == menu.fileSaveAs)
                dPanel.fileSave(true);
            else
            if(obj == menu.fileRead)
                dPanel.fileOpen();
            else
            if(obj == menu.fileWrite)
                dPanel.fileSave(true);
            else
            if(obj == menu.filePageSetup)
            {
                PageSetupDlg pagesetupdlg = new PageSetupDlg(m_view, 7);
                pagesetupdlg.show();
            } else
            if(obj == menu.filePrintSetArea)
                m_view.setPrintArea();
            else
            if(obj == menu.filePrintSetTitles)
                m_view.setPrintTitles();
            else
            if(obj == menu.filePrint)
                m_view.filePrint(false);
            else
            if(obj == menu.fileExit)
                dispose();
            else
            if(obj == menu.editCut)
                m_view.editCut();
            else
            if(obj == menu.editCopy)
                m_view.editCopy();
            else
            if(obj == menu.editPaste)
                m_view.editPaste();
            else
            if(obj == menu.editPasteSpecial)
            {
                PasteSpecialDlg pastespecialdlg = new PasteSpecialDlg(m_view);
                pastespecialdlg.show();
            } else
            if(obj == menu.editSelectAllObjects)
            {
                for(GRObject grobject = m_view.getFirstObject(); grobject != null; grobject = grobject.getNextObject())
                    m_view.addSelection(grobject);

            } else
            if(obj == menu.editSort)
            {
                SortDlg sortdlg = new SortDlg(m_view);
                sortdlg.show();
            } else
            if(obj == menu.editFillRight)
                m_view.editCopyRight();
            else
            if(obj == menu.editFillDown)
                m_view.editCopyDown();
            else
            if(obj == menu.editClearAll)
                m_view.editClear((short)3);
            else
            if(obj == menu.editClearFormats)
                m_view.editClear((short)1);
            else
            if(obj == menu.editClearContents)
                m_view.editClear((short)2);
            else
            if(obj == menu.editDelete)
            {
                if(m_view.getSelectionCount() > 0)
                {
                    DeleteDlg deletedlg = new DeleteDlg(m_view);
                    deletedlg.show();
                } else
                {
                    String msg = LocalDesignerInfo.getString((short)4);
                    if(showMessage(msg, 3) == 1)
                        m_view.editClear((short)3);
                }
            } else
            if(obj == menu.editDeleteSheet)
            {
                if(showMessage(LocalDesignerInfo.getString((short)8), 12) == 4)
                    m_view.editDeleteSheets();
            } else
            if(obj == menu.editFind || obj == menu.editReplace)
            {
                FindReplaceDlg findReplaceDlg = new FindReplaceDlg(m_view);
                findReplaceDlg.show();
            } else
            if(obj == menu.editGoto)
            {
                GotoDlg gotodlg = new GotoDlg(m_view);
                gotodlg.setVisible(true);
            } else
            if(obj == menu.insertCells)
            {
                InsertDlg insertdlg = new InsertDlg(m_view);
                insertdlg.show();
            } else
            if(obj == menu.insertRows)
                m_view.editInsert((short)3);
            else
            if(obj == menu.insertColumns)
                m_view.editInsert((short)4);
            else
            if(obj == menu.insertWorksheet)
                m_view.editInsertSheets();
            else
            if(obj == menu.insertPageBreak)
            {
                if(isPageBreakSelected())
                    m_view.removePageBreak();
                else
                    m_view.addPageBreak();
            } else
            if(obj == menu.insertName)
            {
                DefinedNameDlg definednamedlg = new DefinedNameDlg(m_view);
                definednamedlg.show();
            } else
            if(obj == menu.formatCells)
            {
                FormatCellsDlg formatCellsDlg = new FormatCellsDlg(m_view, 127);
                formatCellsDlg.show();
            } else
            if(obj == menu.formatRowHeight)
            {
                RowHeightDlg rowheightdlg = new RowHeightDlg(m_view);
                rowheightdlg.show();
            } else
            if(obj == menu.formatRowHide)
                setRowHidden(true);
            else
            if(obj == menu.formatRowUnhide)
                setRowHidden(false);
            else
            if(obj == menu.formatRowDefaultHeight)
            {
                DefRowHeightDlg defrowheightdlg = new DefRowHeightDlg(m_view);
                defrowheightdlg.show();
            } else
            if(obj == menu.formatColumnWidth)
            {
                ColWidthDlg colwidthdlg = new ColWidthDlg(m_view);
                colwidthdlg.show();
            } else
            if(obj == menu.formatColumnAutoFit)
            {
                m_view.getLock();
                try
                {
                    int selectionSize = m_view.getSelectionCount();
                    for(int i = 0; i < selectionSize; i++)
                    {
                        RangeRef rangeref = m_view.getSelection(i);
                        m_view.setColWidthAuto(rangeref.getRow1(), rangeref.getCol1(), rangeref.getRow2(), rangeref.getCol2(), false);
                    }

                }
                finally
                {
                    m_view.releaseLock();
                }
            } else
            if(obj == menu.formatColumnHide)
                setColHidden(true);
            else
            if(obj == menu.formatColumnUnhide)
                setColHidden(false);
            else
            if(obj == menu.formatColumnDefaultWidth)
            {
                DefColWidthDlg defcolwidthdlg = new DefColWidthDlg(m_view);
                defcolwidthdlg.show();
            } else
            if(obj == menu.formatSheetProperties)
            {
                FormatSheetDlg formatSheetDlg = new FormatSheetDlg(m_view, 19);
                formatSheetDlg.show();
            } else
            if(obj == menu.formatDefaultFont)
            {
                DefaultFontDlg defaultfontdlg = new DefaultFontDlg(m_view);
                defaultfontdlg.show();
            } else
            if(obj == menu.formatObject)
            {
                GRObject object = m_view.getSelectedObject(0);
                int objType = object.getType();
                if(objType == 4)
                {
                    ChartDataRangeDlg chartDataRangeDlg = new ChartDataRangeDlg(m_view,(GRChart)object);
                    chartDataRangeDlg.show();
                }
                else
                {
                    FormatObjectDlg formatobjectdlg = new FormatObjectDlg(m_view, 31);
                    formatobjectdlg.show();
                }
            } else
            if(obj == menu.formatBringToFront)
                m_view.objectBringToFront();
            else
            if(obj == menu.formatSendToBack)
                m_view.objectSendToBack();
            else
            if(obj == menu.toolsRecalc)
                m_view.recalc();
            else
            if(obj == menu.toolsOptions)
            {
                OptionsDlg optionsdlg = new OptionsDlg(m_view, 15);
                optionsdlg.show();
            } else
            if(obj == menu.helpContents)
                showMessage(LocalDesignerInfo.getString((short)6), 1);
            else
            if(obj == menu.helpAbout)
            {
                AboutDlg aboutdlg = new AboutDlg(m_view);
                aboutdlg.show();
            }
            //
            if(obj == menu.editCopyCellFormat)
                m_view.setFormatPaintMode(!m_view.isFormatPaintMode());
            else
            if(obj == menu.editPolygonPoints)
                m_view.setPolyEditMode((short)(m_view.getPolyEditMode() != 1 ? 1 : 0));
            else
            if(obj == menu.viewFormulaBar)
                m_view.setShowEditBar(!m_view.isShowEditBar());
            else
            if(obj == menu.viewToolbarsStandard)
                dPanel.setToolbarVisible((short)0);
            else
            if(obj == menu.viewToolbarsFormat)
                dPanel.setToolbarVisible((short)2);
            else
            if(obj == menu.viewToolbarsDrawing)
                dPanel.setToolbarVisible((short)1);
            else
            if(obj == menu.viewStatusBar)
                dPanel.setToolbarVisible((short)3);
            else
            if(obj == menu.formatSheetProtection)
                m_view.setEnableProtection(!m_view.isEnableProtection());
            else
            if(obj == menu.formatConditionFormat)
            {
                CondFormatDlg condFormatDlg= new CondFormatDlg(m_view);
                condFormatDlg.show();
            } else
            if(obj == menu.formatFreezePanes)
            {
                int row1 = m_view.getSelStartRow();
                int row2 = m_view.getSelEndRow();
                int col1 = m_view.getSelStartCol();
                int col2 = m_view.getSelEndCol();
                int topRow = m_view.getTopRow();
                int leftCol = m_view.getLeftCol();
                boolean bWholeRow = (col2 - col1) + 1 == 16383;
                boolean bWholeCol = (row2 - row1) + 1 == 255;
                if(m_view.getFixedRows() != 0 || m_view.getFixedCols() != 0)
                {
                    m_view.setFixedRows(0);
                    m_view.setFixedCols(0);
                } else
                if(bWholeRow || bWholeCol || row1 < topRow || col1 < leftCol)
                {
                    String msg = LocalDesignerInfo.getString((short)3);
                    showMessage(msg, 1);
                } else
                {
                    m_view.setFixedRows(topRow, row1 - topRow);
                    m_view.setFixedCols(leftCol, col1 - leftCol);
                }
            }
        }
        catch(SecurityException _ex)
        {
            showMessage(LocalDesignerInfo.getString((short)5), 1);
        }
        catch(Throwable throwable)
        {
            m_view.messageBox(throwable.getMessage(), null, (short)1);
            m_view.requestFocus();
        }
        finally
        {
            updateMenus();
        }
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        try
        {
            Object obj = itemevent.getSource();
            if(obj == menu.editCopyCellFormat)
                m_view.setFormatPaintMode(!m_view.isFormatPaintMode());
            else
            if(obj == menu.editPolygonPoints)
                m_view.setPolyEditMode((short)(m_view.getPolyEditMode() != 1 ? 1 : 0));
            else
            if(obj == menu.viewFormulaBar)
                m_view.setShowEditBar(!m_view.isShowEditBar());
            else
            if(obj == menu.viewToolbarsStandard)
                dPanel.setToolbarVisible((short)0);
            else
            if(obj == menu.viewToolbarsFormat)
                dPanel.setToolbarVisible((short)2);
            else
            if(obj == menu.viewToolbarsDrawing)
                dPanel.setToolbarVisible((short)1);
            else
            if(obj == menu.viewStatusBar)
                dPanel.setToolbarVisible((short)3);
            else
            if(obj == menu.formatSheetProtection)
                m_view.setEnableProtection(!m_view.isEnableProtection());
            else
            if(obj == menu.formatFreezePanes)
            {
                int row1 = m_view.getSelStartRow();
                int row2 = m_view.getSelEndRow();
                int col1 = m_view.getSelStartCol();
                int col2 = m_view.getSelEndCol();
                int topRow = m_view.getTopRow();
                int leftCol = m_view.getLeftCol();
                boolean bWholeRows = (col2 - col1) + 1 == 16383;
                boolean bWholeCols = (row2 - row1) + 1 == 255;
                if(m_view.getFixedRows() != 0 || m_view.getFixedCols() != 0)
                {
                    m_view.setFixedRows(0);
                    m_view.setFixedCols(0);
                } else
                if(bWholeRows || bWholeCols || row1 < topRow || col1 < leftCol)
                {
                    String msg = LocalDesignerInfo.getString((short)3);
                    showMessage(msg, 1);
                } else
                {
                    m_view.setFixedRows(topRow, row1 - topRow);
                    m_view.setFixedCols(leftCol, col1 - leftCol);
                }
            }
        }
        catch(Throwable throwable)
        {
            m_view.messageBox(throwable.getMessage(), null, (short)1);
            m_view.requestFocus();
        }
    }

    public void update(UpdateEvent updateevent)
    {
        updateMenus();
    }

    public void updateMenus()
    {
        try
        {
            boolean bHasSelection = m_view.getSelectionCount() > 0;
            boolean bSingleSelection = m_view.getSelectionCount() == 1;
            boolean bHeaderSelected = m_view.isRowHeaderSelected() || m_view.isColHeaderSelected() || m_view.isTopLeftHeaderSelected();
            boolean bHasObjSelected = m_view.getSelectedObjectCount() > 0;
            boolean bSingleObjSelected = m_view.getSelectedObjectCount() == 1;
            boolean bEdit = true;
            int i = 0;
            for(int j = 0; j < m_view.getNumSheets(); j++)
                if(m_view.isSheetSelected(j))
                {
                    i++;
                    if(m_view.isSheetProtected(j))
                        bEdit = false;
                }

            boolean bPolygon = false;
            for(int k = 0; k < m_view.getSelectedObjectCount(); k++)
            {
                GRObject grobject = m_view.getSelectedObject(k);
                if(grobject.getType() != 9)
                    continue;
                bPolygon = true;
                break;
            }

            menu.editCut.setEnabled(!bHeaderSelected);
            menu.editCopy.setEnabled(!bHeaderSelected);
            menu.editPaste.setEnabled(m_view.isCanEditPaste());
            menu.editPasteSpecial.setEnabled(m_view.isCanEditPasteSpecial());
            menu.editCopyCellFormat.setEnabled(bSingleSelection);
            menu.editCopyCellFormat.setState(m_view.isFormatPaintMode());
            menu.editPolygonPoints.setEnabled(bPolygon);
            menu.editPolygonPoints.setState(bPolygon && m_view.getPolyEditMode() == 1);
            menu.editSelectAllObjects.setEnabled(bEdit && m_view.getFirstObject() != null);
            menu.editSort.setEnabled(bEdit && bSingleSelection);
            menu.editFill.setEnabled(bSingleSelection);
            menu.editClear.setEnabled(bHasSelection);
            menu.editDelete.setEnabled(bEdit && (bSingleSelection || bHasObjSelected));
            menu.editDelete.setText(bHasObjSelected ? LocalDesignerInfo.getselectionString(0) : LocalDesignerInfo.getselectionString(1));
            menu.editDeleteSheet.setEnabled(m_view.getNumSheets() > 1 && i == 1);
            menu.editReplace.setEnabled(bEdit);
            menu.viewFormulaBar.setState(m_view.isShowEditBar());
            menu.viewFormulaBar.setSelected(m_view.isShowEditBar());
            menu.viewToolbarsStandard.setState(dPanel.isToolbarVisible((short)0));
            menu.viewToolbarsFormat.setState(dPanel.isToolbarVisible((short)2));
            menu.viewToolbarsDrawing.setState(dPanel.isToolbarVisible((short)1));
            menu.viewStatusBar.setState(dPanel.isToolbarVisible((short)3));
            menu.insertCells.setEnabled(bEdit && bSingleSelection);
            menu.insertRows.setEnabled(bEdit && bSingleSelection);
            menu.insertColumns.setEnabled(bEdit && bSingleSelection);
            menu.insertPageBreak.setEnabled(bSingleSelection);
            menu.insertPageBreak.setText(isPageBreakSelected() ? LocalDesignerInfo.getselectionString(2) : LocalDesignerInfo.getselectionString(3));
            menu.insertName.setEnabled(bEdit);
            menu.formatCells.setEnabled(bEdit && (bHasSelection || bHeaderSelected));
            menu.formatRow.setEnabled(bEdit);
            menu.formatRowHeight.setEnabled(bHasSelection);
            menu.formatRowHide.setEnabled(bHasSelection);
            menu.formatRowUnhide.setEnabled(bHasSelection);
            menu.formatColumn.setEnabled(bEdit);
            menu.formatColumnWidth.setEnabled(bHasSelection);
            menu.formatColumnAutoFit.setEnabled(bHasSelection);
            menu.formatColumnHide.setEnabled(bHasSelection);
            menu.formatColumnUnhide.setEnabled(bHasSelection);
            menu.formatSheetProperties.setEnabled(bEdit);
            menu.formatSheetProtection.setState(!bEdit);
            menu.formatFreezePanes.setState(m_view.getFixedRows() != 0 || m_view.getFixedCols() != 0);
            menu.formatDefaultFont.setEnabled(bEdit);
            menu.formatObject.setEnabled(bEdit && bSingleObjSelected);
            menu.formatBringToFront.setEnabled(bSingleObjSelected);
            menu.formatSendToBack.setEnabled(bSingleObjSelected);
            menu.toolsOptions.setEnabled(bEdit);
        }
        catch(Exception cellexception)
        {
        }
    }

    private void setRowHidden(boolean rowHidden)
        throws CellException
    {
        m_view.getLock();
        try
        {
            int len = m_view.getSelectionCount();
            for(int i = 0; i < len; i++)
            {
                RangeRef rangeref = m_view.getSelection(i);
                m_view.setRowHidden(rangeref.getRow1(), rangeref.getRow2(), rowHidden);
            }

        }
        finally
        {
            m_view.releaseLock();
        }
    }

    private void setColHidden(boolean colHidden)
        throws CellException
    {
        m_view.getLock();
        try
        {
            int len = m_view.getSelectionCount();
            for(int i = 0; i < len; i++)
            {
                RangeRef rangeref = m_view.getSelection(i);
                m_view.setColHidden(rangeref.getCol1(), rangeref.getCol2(), colHidden);
            }

        }
        finally
        {
            m_view.releaseLock();
        }
    }

    private boolean isPageBreakSelected()
    {
        try
        {
            if(m_view.getSelectionCount() == 1)
            {
                int col = m_view.getSelStartCol();
                int row = m_view.getSelStartRow();
                int colPageBreak = m_view.getNextColPageBreak(col);
                int rowPageBreak = m_view.getNextRowPageBreak(row);
                return col == colPageBreak || row == rowPageBreak;
            }
        }
        catch(CellException cellexception) { }
        return false;
    }
}
