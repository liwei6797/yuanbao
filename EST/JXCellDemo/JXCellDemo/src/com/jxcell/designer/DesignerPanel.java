package com.jxcell.designer;

import com.jxcell.*;
import com.jxcell.CellFormat;
import com.jxcell.View;
import com.jxcell.dialog.PassDlg;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.tools.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DesignerPanel extends JPanel
    implements ActionListener, PopupMenuListener, FocusListener, ToolbarListener, ModifiedListener, UpdateListener
{

    private View m_view;
    private ToolbarStd tb;
    private ToolbarDraw tbdraw;
    private ToolbarFormat tbformat;
    public  Statusbar sbar;
    public  String defaultStatus;
    private boolean tbIsVisible;
    private boolean tbdrawIsVisible;
    private boolean tbformatIsVisible;
    private boolean sbarIsVisible;
    private boolean modified;
    private boolean hasFileName;
    private boolean isApplication;
    private String currentDirectoryPath;

    private com.jxcell.designer.UndoManager undoRedoManager = new com.jxcell.designer.UndoManager();

    FileChooser openDialog;
    FileChooser saveDialog;

    public DesignerPanel(View view, boolean isApp)
    {
        isApplication = isApp;
        currentDirectoryPath = ".";
        addFocusListener(this);
        setLayout(null);
        m_view = new View(view);
        add(m_view);
        try
        {
            tb = new ToolbarStd(m_view);
            tbdraw = new ToolbarDraw(m_view);
            tbformat = new ToolbarFormat(m_view);
            add(tb);
            add(tbdraw);
            add(tbformat);
            tb.addToolbarListener(this);
            tbdraw.addToolbarListener(this);
            tbformat.addToolbarListener(this);
        }
        catch(IOException _ex)
        {
            tb = null;
            tbdraw = null;
            tbformat = null;
        }
        sbar = new Statusbar();
        add(sbar);
        tbIsVisible = tb != null;
        tbdrawIsVisible = tbdraw != null;
        tbformatIsVisible = tbformat != null;
        sbarIsVisible = sbar != null;
        m_view.addModifiedListener(this);
        m_view.addUpdateListener(this);
        m_view.getController().addUndoableEditListener(undoRedoManager);
    }

    public View getView()
    {
        return m_view;
    }

    public void setToolbarVisible(short index)
    {
        switch(index)
        {
        default:
            break;

        case 0:
            if(tb != null)
                tb.setVisible(tbIsVisible = !tbIsVisible);
            break;

        case 1:
            if(tbdraw == null)
                break;
            tbdraw.setVisible(tbdrawIsVisible = !tbdrawIsVisible);
            if(tb != null)
                tb.setButtonState(8, tbdrawIsVisible, false);
            break;

        case 2:
            if(tbformat != null)
                tbformat.setVisible(tbformatIsVisible = !tbformatIsVisible);
            break;

        case 3:
            if(sbar != null)
                sbar.setVisible(sbarIsVisible = !sbarIsVisible);
            break;
        }
        validate();
    }

    public boolean isToolbarVisible(short index)
    {
        switch(index)
        {
        case 0:
            return tbIsVisible;

        case 1:
            return tbdrawIsVisible;

        case 2:
            return tbformatIsVisible;

        case 3:
            return sbarIsVisible;
        }
        return false;
    }

    public boolean getModified()
    {
        return modified;
    }

    private void showError(Throwable throwable)
    {
        String msg = throwable.getMessage();
        m_view.messageBox(msg, null, (short)1);
        m_view.requestFocus();
    }

    private int showMessage(String msg, int type)
    {
        int ret = m_view.messageBox(msg, null, (short)type);
        m_view.requestFocus();
        return ret;
    }

    public void addNotify()
    {
        super.addNotify();
        m_view.requestFocus();
    }

    public Dimension getPreferredSize()
    {
        Dimension dimension = getToolkit().getScreenSize();
        dimension.width = (dimension.width * 65) / 100;
        dimension.height = (dimension.height * 65) / 100;
        return dimension;
    }

    public void doLayout()
    {
        Rectangle rectangle = getBounds();
        Insets insets = getInsets();
        Dimension dimension = tb == null ? new Dimension(0, 0) : tb.getPreferredSize();
        Dimension dimension1 = tbformat == null ? new Dimension(0, 0) : tbformat.getPreferredSize();
        int y1 = insets.top;
        int x1 = insets.left;
        int y2 = insets.bottom;
        int x2 = insets.right;
        int width2 = rectangle.width - (x1 + x2);
        int height2 = rectangle.height - (y1 + y2);
        int tbdrawheight = !tbIsVisible && !tbdrawIsVisible ? 0 : dimension.height;
        int tbdrawwidth = tbIsVisible ? tbdrawIsVisible ? dimension.width : width2 : 0;
        int width1 = tbIsVisible ? width2 - tbdrawwidth : width2;
        int tbformatheight = tbformatIsVisible ? dimension1.height : 0;
        int sbarheight = sbarIsVisible ? sbar.getPreferredSize().height : 0;
        if(tb != null)
            tb.setBounds(x1, y1, tbdrawwidth, tbdrawheight);
        if(tbdraw != null)
            tbdraw.setBounds(x1 + tbdrawwidth, y1, width1, tbdrawheight);
        if(tbformat != null)
            tbformat.setBounds(x1, y1 + tbdrawheight, width2, tbformatheight);
        sbar.setBounds(x1, (y1 + height2) - sbarheight, width2, sbarheight);
        m_view.setBounds(x1, y1 + tbdrawheight + tbformatheight, width2, height2 - tbdrawheight - tbformatheight - sbarheight);
    }

    public void update(UpdateEvent updateevent)
    {
        updateToolbars();
    }

    public void updateToolbars()
    {
        try
        {
            if(tb != null)
            {
                tb.compareAndSelect((short)0, String.valueOf(m_view.getViewScale())+"%");
                tb.setButtonState(7, m_view.isFormatPaintMode(), false);
                tb.setButtonState(8, tbdrawIsVisible, false);
            }
            if(tbdraw != null)
            {
                short mode = m_view.getMode();
                tbdraw.setButtonState(0, mode == 1, true);
                tbdraw.setButtonState(1, mode == 3, true);
                tbdraw.setButtonState(2, mode == 2, true);
                tbdraw.setButtonState(3, mode == 11, true);
                tbdraw.setButtonState(4, mode == 20, true);
                tbdraw.setButtonState(5, mode == 5, true);
                tbdraw.setButtonState(6, mode == 8, true);
                for(int j = 0; j < m_view.getSelectedObjectCount(); j++)
                {
                    GRObject grobject = m_view.getSelectedObject(j);
                    if(grobject.getType() != 9)
                        continue;
                    break;
                }
            }
            if(tbformat != null)
            {
                CellFormat cellformat = m_view.getCellFormat();
                tbformat.compareAndSelect((short)0, cellformat.getFontName());
                tbformat.compareAndSelect((short)1, Integer.toString((int) cellformat.getFontSize()));
                tbformat.setButtonState(0, cellformat.isFontBold(), false);
                tbformat.setButtonState(1, cellformat.isFontItalic(), false);
                tbformat.setButtonState(2, cellformat.getFontUnderline()!=0, false);
                for(int index = 4; index <= 7; index++)
                    tbformat.setButtonState(index, false, false);

                if(cellformat.getMergeCells())
                    tbformat.setButtonState(7, true, false);

                switch(cellformat.getHorizontalAlignment())
                {
                case 1:
                    tbformat.setButtonState(4, true, false);
                    return;

                case 2:
                    tbformat.setButtonState(5, true, false);
                    return;

                case 3:
                    tbformat.setButtonState(6, true, false);
                    return;

                case 6:
                    tbformat.setButtonState(7, true, false);
                    return;
                }
            }
        }
        catch(CellException cellexception) { }
    }

    public void toolbarButtonClicked(ToolbarEvent toolbarevent)
    {
        boolean bFocus = true;
        try
        {
            Object obj = toolbarevent.getSource();
            Toolbar toolbar = (Toolbar)toolbarevent.getSource();
            short buttonindex = toolbarevent.getButtonIndex();
            boolean isSticky = toolbarevent.isButtonSticky();
            ColorPanel colorPanel = new ColorPanel(m_view.getBookAdapter());

            JPopupMenu jpopupmenu = new JPopupMenu();
            jpopupmenu.setBorder(BorderFactory.createBevelBorder(0));
            if(obj == tb)
                switch(buttonindex)
                {
                    case 0:
                        bFocus = false;
                        fileNew();
                        break;

                    case 1:
                        bFocus = false;
                        fileOpen();
                        break;

                    case 2:
                        bFocus = false;
                        fileSave(false);
                        break;

                    case 3:
                        bFocus = false;
                        m_view.filePrint(false);
                        break;

                    case 4:
                        addUndoableRegionEdit(0x12200008, "editCut");
                        m_view.editCut();
                        break;

                    case 5:
                        m_view.editCopy();
                        break;

                    case 6:
                        addUndoableRegionEdit(0x34000001, "editPaste");
                        m_view.editPaste();
                        break;

                    case 7:
                        m_view.setFormatPaintMode(isSticky);
                        break;

                    case 8:
                        if(tbdraw != null)
                        {
                            setToolbarVisible((short)1);
                            if(getParent() instanceof Designer)
                                ((Designer)getParent()).getMenu().viewToolbarsDrawing.setState(isToolbarVisible((short)1));
                        }
                        break;

                    case 9:
                            undo();
                            break;

                    case 10:
                            redo();
                            break;
                }
            else
            if(obj == tbdraw && isViewCanEdit())
            {
                addUndoableRegionEdit(0x14000007, "draw");
                if(buttonindex == 7)
                    m_view.setPolyEditMode((short)(isSticky ? 1 : 0));
                else
                if(!isSticky)
                {
                    m_view.setMode((short)0);
                } else
                {
                    short mode1;
                    switch(buttonindex)
                    {
                        case 0:  //Line
                            mode1 = 1;
                            break;

                        case 1:   //Oval
                            mode1 = 3;
                            break;

                        case 2: //rectangle
                            mode1 = 2;
                            break;

                        case 3:   //chart
                            mode1 = 5;
                            break;

                        case 4:  //picture
                            mode1 = 8;   //actual
                            addPicture();
                            mode1 = 0;   //cancel drawing mode
                            break;

                        default:
                            mode1 = 0;
                            break;
                    }
                    m_view.setMode(mode1);
                }
            } else
            if(obj == tbformat && isViewCanEdit())
            {
                CellFormat cellformat = m_view.getCellFormat();
                switch(buttonindex)
                {
                default:
                    break;

                case 0:
                    cellformat.setFontBold(isSticky);
                    break;

                case 1:
                    cellformat.setFontItalic(isSticky);
                    break;

                case 2:
                    cellformat.setFontUnderline((short) (isSticky?1:0));
                    break;

                case 3:
                    bFocus = false;
                    colorPanel.addActionListener(this, "colorFontRDOAction");
                    jpopupmenu.add(colorPanel);
                    jpopupmenu.addPopupMenuListener(this);
                    jpopupmenu.show(toolbar.getButton(buttonindex), 0, toolbar.getButton(buttonindex).getSize().height);
                   break;

                case 13:
                    bFocus = false;
                    colorPanel.addActionListener(this, "colorFillRDOAction");
                    jpopupmenu.add(colorPanel);
                    jpopupmenu.addPopupMenuListener(this);
                    jpopupmenu.show(toolbar.getButton(buttonindex), 0, toolbar.getButton(buttonindex).getSize().height);
                   break;

                case 4:
                    cellformat.setHorizontalAlignment((short)(isSticky ? 1 : 0));
                    break;

                case 5:
                    cellformat.setHorizontalAlignment((short)(isSticky ? 2 : 0));
                    break;

                case 6:
                    cellformat.setHorizontalAlignment((short)(isSticky ? 3 : 0));
                    break;

                case 7:
                    if(isSticky)
                       cellformat.setMergeCells(true);
                    else
                        cellformat.setMergeCells(false);
                    cellformat.setHorizontalAlignment((short)(isSticky ? 2 : 0));
                    break;

                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    bFocus = false;
                    byte index = 0;
                    switch(buttonindex)
                    {
                    case 8:
                        setPopupMenuString(jpopupmenu, 0);
                        index = 1;
                        break;

                    case 9:
                        index = 2;
                        break;

                    case 10:
                        index = 5;
                        break;

                    case 11:
                        index = 6;
                        break;

                    case 12:
                        setPopupMenuString(jpopupmenu, 3);
                        index = 4;
                        break;
                    }
                    setPopupMenuString(jpopupmenu, index);
                    jpopupmenu.addPopupMenuListener(this);
                    jpopupmenu.show(toolbar.getButton(buttonindex), 0, toolbar.getButton(buttonindex).getSize().height);
                    break;
                }
                m_view.setCellFormat(cellformat);
            }
        }
        catch(SecurityException _ex)
        {
            showMessage(LocalDesignerInfo.getString((short)5), 1);
        }
        catch(Throwable throwable)
        {
            String s = throwable.getMessage();
            m_view.messageBox(s, null, (short)1);
            m_view.requestFocus();
        }
        if(bFocus)
            m_view.requestFocus();
        updateToolbars();
    }

    private void addPicture()
    {
        try
        {
            MyFileFilter bmpfilter = new MyFileFilter(new String[] {"bmp", "dib"}, "Windows Bitmap", (short)0);
            MyFileFilter pngfilter = new MyFileFilter("png", "Portable Network Graphics", (short)1);
            MyFileFilter giffilter = new MyFileFilter("gif", "Graphics Interchange Format", (short)2);
            MyFileFilter jpgfilter = new MyFileFilter(new String[] {"jpg", "jpeg"}, "JPEG File Interchange Format", (short)3);
            MyFileFilter nofilter = new MyFileFilter("*", "All Files", (short)103);
            openDialog = new FileChooser(null, false);
            openDialog.addFilter(bmpfilter);
            openDialog.addFilter(pngfilter);
            openDialog.addFilter(giffilter);
            openDialog.addFilter(jpgfilter);
            openDialog.addFilter(nofilter);
            openDialog.setFileFilters((short)0);
            openDialog.setSelectedFile(null);
            openDialog.rescanCurrentDirectory();
            if(openDialog.showOpenDialog(this) == 0)
            {
                openDialog.cancelSelection();
                File file =  openDialog.getSelectedFile();
                FileInputStream fileinputstream = new FileInputStream(file);
                m_view.addPicture(fileinputstream);
            }
        }
        catch(Exception e)
        {
        }
    }

    private void setPopupMenuString(JPopupMenu jpopupmenu, int index)
    {
        String[] strs = getFormatStrings(index);
        for(int i = 0; i < strs.length; i++)
        {
                JMenuItem jmenuitem = new JMenuItem(strs[i]);
                jmenuitem.addActionListener(this);
                jpopupmenu.add(jmenuitem);
        }
    }

    private String[] getFormatStrings(int type)
    {
        String fm1[] = new String[0];
        try
        {
            m_view.getLock();
            for(NumberFormat numberformat = m_view.getFirstNumberFormat(); numberformat != null; numberformat = m_view.getNextNumberFormat(numberformat))
                if(numberformat.getType() == type)
                {
                    String fm2[] = new String[fm1.length + 1];
                    System.arraycopy(fm1, 0, fm2, 0, fm1.length);

                    fm2[fm2.length - 1] = numberformat.getNumberFormatLocal();
                    fm1 = fm2;
                }

        }
        catch(CellException cellexception)
        {
            String s = cellexception.getMessage();
            m_view.messageBox(s, null, (short)1);
            m_view.requestFocus();
        }
        finally
        {
            m_view.releaseLock();
        }
        return fm1;
    }

    public void toolbarItemChanged(ToolbarEvent toolbarevent)
    {
        try
        {
            if(isViewCanEdit())
            {
                Object obj = toolbarevent.getSource();
                short index1 = toolbarevent.getControlIndex();
                String val1 = toolbarevent.getItemValue();
                if(obj == tbformat)
                {
                    CellFormat cellformat = m_view.getCellFormat();
                    if(index1 == 0)
                        cellformat.setFontName(val1);
                    else
                    if(index1 == 1)
                        cellformat.setFontSize(Double.valueOf(val1).doubleValue());
                    m_view.setCellFormat(cellformat);
                    m_view.requestFocus();
                }
                else if(obj == tb && index1 == 0)
                {
                    int scale = Integer.parseInt(val1.substring(0,val1.indexOf("%")));
                    try
                    {
                        m_view.getLock();
                        m_view.setViewScale(scale);
                    }
                    finally
                    {
                        m_view.releaseLock();
                    }
                    m_view.requestFocus();
                }
            }
        }
        catch(Throwable throwable)
        {
            String s = throwable.getMessage();
            m_view.messageBox(s, null, (short)1);
            m_view.requestFocus();
        }
        updateToolbars();
    }

    private boolean isViewCanEdit()
        throws CellException
    {
        boolean bCanEdit = true;
        for(int i = 0; i < m_view.getNumSheets(); i++)
        {
            if(!m_view.isSheetSelected(i) || !m_view.isEnableProtection())
                continue;
            bCanEdit = false;
            break;
        }
        return bCanEdit;
    }

    void clearModified()
    {
        m_view.flushModifiedEvents();
        modified = false;
    }

    public void fileNew()
        throws CellException
    {
        if(isApplication)
        {
            Designer.newDesigner(null);
            return;
        }
        String msg = LocalDesignerInfo.getString((short)1);
        if(showMessage(msg, 12) != 8)
        {
            hasFileName = false;
            m_view.initWorkbook();
            clearModified();
        }
    }

    public void fileOpen()
        throws CellException
    {
        File file = getFile(true);
        if(file != null)
        {
            currentDirectoryPath = file.getPath();
            read(currentDirectoryPath);
        }
        m_view.requestFocus();
    }

    boolean read(String file)
    {
        String err = null;
        try
        {
            m_view.initWorkbook();
            hasFileName = false;
            if(openDialog.getFileType() == View.FileXLSX)
            {
                m_view.readXLSX(file);
                hasFileName = true;
            }
            else
            {
                if(m_view.read(file) == 11)
                    hasFileName = true;
                else
                    m_view.setShowEditBar(true);
            }
            m_view.setWorkbookName(file);
            setTitle();
            clearModified();
        }
        catch(CellException e)
        {
            if(e.getError() != 64)
                err =  "Unable to read " + file + '.';
            else
            {
                PassDlg passdlg = new PassDlg(m_view);
                passdlg.setVisible(true);
                try
                {
                    if(m_view.read(file,passdlg.getPass())  == 11)
                        hasFileName = true;
                    else
                        m_view.setShowEditBar(true);
                    m_view.setWorkbookName(file);
                    setTitle();
                    clearModified();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                    err = e1.toString();
                } catch (CellException e1)
                {
                    err = e1.toString();
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            err = throwable.toString();
        }
        if(err == null)
        {
            return true;
        } else
        {
            m_view.messageBox(err, null, (short)1);
            m_view.requestFocus();
            return false;
        }
    }

    public int fileSave(boolean select)
        throws CellException
    {
        try
        {
            String filePath = m_view.getWorkbookName();
            if(select || !hasFileName)
            {
                File file= getFile(false);
                if(file != null)
                {
                    currentDirectoryPath = file.getPath();
                    String name = file.getName();
                    String path = file.getPath();

                    if(name.length() < 4 || (!name.substring(name.length() - 5).equalsIgnoreCase(".xlsx")
                            && !name.substring(name.length() - 4).equalsIgnoreCase(".xls")))
                        path += ".xls";
                    filePath = path;
                }
                else
                  return 2;
            }
            String name = filePath.toUpperCase();
            short filetype = 13;
            if(name.endsWith(".XLS"))
                filetype = View.FileExcel97;
            else if(name.endsWith(".XLSX"))
                filetype = View.FileXLSX;
            else
            if(name.endsWith(".TXT"))
                filetype = 3;
            m_view.saveWindowInfo();
            m_view.write(filePath, filetype);
            m_view.setWorkbookName(filePath);
            hasFileName = filetype == 13;
            setTitle();
            clearModified();
        }
        catch(IOException ioexception)
        {
            String s3 = ioexception.getMessage();
            m_view.messageBox(s3, null, (short)1);
            m_view.requestFocus();
        }
        m_view.requestFocus();
        return 1;
    }

    File getFile(boolean bRead)
    {
        try
        {
            if(openDialog == null)
            {
                MyFileFilter xls97filter = new MyFileFilter("xls", "Microsoft Excel 97-2003 Workbook", (short)View.FileExcel97);
                MyFileFilter xlsxfilter = new MyFileFilter("xlsx", "Microsoft Excel 2007 Workbook", (short)View.FileXLSX);
                MyFileFilter nofilter = new MyFileFilter("*", "All Files", (short)103);
                openDialog = new FileChooser(null, false);
                openDialog.addFilter(xls97filter);
                openDialog.addFilter(xlsxfilter);
                openDialog.addFilter(nofilter);
                openDialog.setFileFilters((short)11);
                saveDialog = new FileChooser(null, true);
                saveDialog.addFilter(xls97filter);
                saveDialog.addFilter(xlsxfilter);
                saveDialog.setFileFilters((short)11);
            }
            if(bRead)
            {
                openDialog.setSelectedFile(null);
                openDialog.rescanCurrentDirectory();
                if(openDialog.showOpenDialog(this) == 0)
                {
                    openDialog.cancelSelection();
                    return openDialog.getSelectedFile();
                }
            } else
            {
                saveDialog.setFileFilters((short)11);
                File file = new File(m_view.getWorkbookName());
                saveDialog.setSelectedFile1(file);

                saveDialog.rescanCurrentDirectory();
                if(saveDialog.showSaveDialog(this) == 0)
                {
                    saveDialog.cancelSelection();
                    return saveDialog.getFilterSelectedFile();
                }
            }
        }
        catch(Throwable throwable)
        {
            String msg = throwable.getMessage();
            showMessage("Error retrieving file." + msg, 1);
        }
        return null;
    }

    private void setTitle()
    {
        if(isApplication && (getParent() instanceof Frame))
            ((Frame)getParent()).setTitle(LocalDesignerInfo.getString((short)0) + " - " + m_view.getWorkbookName());
    }

    public void focusGained(FocusEvent focusevent)
    {
        if(m_view != null)
            m_view.requestFocus();
    }

    public void focusLost(FocusEvent focusevent)
    {
    }

    public void modified(ModifiedEvent modifiedevent)
    {
        modified = true;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        String cmd = actionevent.getActionCommand();
        try
        {
            CellFormat cellformat = m_view.getCellFormat();
            if(cmd.startsWith("color"))
            {
                int rgb = ((ColorPanel)obj).getRGB();
                if(cmd.equals("colorFillRDOAction"))
                {
                    if(cellformat.isUndefined((short)13) || cellformat.getPattern() == 0)
                        cellformat.setPattern((short)1);
                    cellformat.setPatternFG(rgb);
                } else
                if(cmd.equals("colorFontRDOAction"))
                    cellformat.setFontColor(rgb);
                addUndoableRegionEdit(0x14000007, "color");
            }
            else
            {
                cellformat.setCustomFormat(cmd);
            }
            m_view.setCellFormat(cellformat);
        }
        catch(Throwable throwable)
        {
            showError(throwable);
        }
        if(obj instanceof ColorPanel)
            ((ColorPanel)obj).getParent().setVisible(false);
    }

    public void popupMenuCanceled(PopupMenuEvent e)
    {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupmenuevent)
    {
        JPopupMenu jpopupmenu = (JPopupMenu)popupmenuevent.getSource();
        jpopupmenu.removeAll();
        jpopupmenu.setInvoker(null);
        m_view.requestFocus();
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    {
    }

    public boolean wantsUndoableEdit()
    {
        return m_view.wantsUndoableEdit();
    }

    void undo()
    {
        if(wantsUndoableEdit() && undoRedoManager.canUndo())
        {
            undoRedoManager.undo();
        }
    }

    void redo()
    {
        if(wantsUndoableEdit() && undoRedoManager.canRedo())
        {
            undoRedoManager.redo();
        }
    }

    public UndoableEdit addUndoableRegionEdit(int mask, String title)
        throws CellException
    {
        UndoableEdit viewUndoableEdit1 = null;
        if(wantsUndoableEdit())
        {
            viewUndoableEdit1 = m_view.getUndoableRegionEdit(title);
            undoRedoManager.undoableEditHappened(viewUndoableEdit1);
        }
        return viewUndoableEdit1;
    }
}
