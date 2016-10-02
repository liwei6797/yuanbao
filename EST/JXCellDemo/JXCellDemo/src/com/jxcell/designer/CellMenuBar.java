package com.jxcell.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

class CellMenuBar extends JMenuBar
{

    LocalDesignerInfo localInfo;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem fileNew;
    JMenuItem fileOpen;
    JMenuItem fileClose;
    JMenuItem fileSave;
    JMenuItem fileSaveAs;
    JMenuItem fileRead;
    JMenuItem fileWrite;
    JMenuItem filePageSetup;
    JMenu filePrintOptions;
    JMenuItem filePrintSetArea;
    JMenuItem filePrintSetTitles;
    JMenuItem filePrint;
    JMenuItem fileExit;
    JMenu editMenu;
    JMenuItem editCut;
    JMenuItem editCopy;
    JMenuItem editPaste;
    JMenuItem editPasteSpecial;
    JCheckBoxMenuItem editCopyCellFormat;
    JCheckBoxMenuItem editPolygonPoints;
    JMenuItem editSelectAllObjects;
    JMenuItem editSort;
    JMenu editFill;
    JMenuItem editFillDown;
    JMenuItem editFillRight;
    JMenu editClear;
    JMenuItem editClearAll;
    JMenuItem editClearFormats;
    JMenuItem editClearContents;
    JMenuItem editDelete;
    JMenuItem editDeleteSheet;
    JMenuItem editFind;
    JMenuItem editReplace;
    JMenuItem editGoto;
    JMenu viewMenu;
    JMenu viewToolbars;
    JCheckBoxMenuItem viewToolbarsStandard;
    JCheckBoxMenuItem viewToolbarsFormat;
    JCheckBoxMenuItem viewToolbarsDrawing;
    JCheckBoxMenuItem viewFormulaBar;
    JCheckBoxMenuItem viewStatusBar;
    JMenu insertMenu;
    JMenuItem insertCells;
    JMenuItem insertRows;
    JMenuItem insertColumns;
    JMenuItem insertWorksheet;
    JMenuItem insertPageBreak;
    JMenuItem insertName;
    JMenu formatMenu;
    JMenuItem formatCells;
    JMenu formatRow;
    JMenuItem formatRowHeight;
    JMenuItem formatRowHide;
    JMenuItem formatRowUnhide;
    JMenuItem formatRowDefaultHeight;
    JMenu formatColumn;
    JMenuItem formatColumnWidth;
    JMenuItem formatColumnAutoFit;
    JMenuItem formatColumnHide;
    JMenuItem formatColumnUnhide;
    JMenuItem formatColumnDefaultWidth;
    JMenu formatSheet;
    JMenuItem formatSheetProperties;
    JCheckBoxMenuItem formatSheetProtection;
    JCheckBoxMenuItem formatFreezePanes;
    JMenuItem formatConditionFormat;
    JMenuItem formatDefaultFont;
    JMenuItem formatObject;
    JMenuItem formatBringToFront;
    JMenuItem formatSendToBack;
    JMenu toolsMenu;
    JMenuItem toolsRecalc;
    JMenuItem toolsOptions;
    JMenu helpMenu;
    JMenuItem helpContents;
    JMenuItem helpAbout;
    ActionListener actionListener;
    ItemListener itemListener;

    CellMenuBar(LocalDesignerInfo localdesignerinfo, Component component, boolean isApplication)
    {
        localInfo = localdesignerinfo;
        actionListener = (ActionListener)component;
        itemListener = (ItemListener)component;
        localdesignerinfo.resetMenuStrings();
        localdesignerinfo.resetShortcutKeys();
        fileMenu = getNextMenu();
        fileMenu.add(fileNew = getNextMenuItem());
        fileOpen = getNextMenuItem();
        fileClose = getNextMenuItem();
        fileSave = getNextMenuItem();
        fileSaveAs = getNextMenuItem();
        fileRead = getNextMenuItem();
        fileWrite = getNextMenuItem();
        if(isApplication)
        {
            fileMenu.add(fileOpen);
            fileMenu.add(fileClose);
            fileMenu.add(fileSave);
            fileMenu.add(fileSaveAs);
        } else
        {
            fileMenu.add(fileRead);
            fileMenu.add(fileWrite);
        }
        fileMenu.addSeparator();
        fileMenu.add(filePageSetup = getNextMenuItem());
        fileMenu.add(filePrintOptions = getNextMenu());
        filePrintOptions.add(filePrintSetArea = getNextMenuItem());
        filePrintOptions.add(filePrintSetTitles = getNextMenuItem());
        fileMenu.add(filePrint = getNextMenuItem());
        fileMenu.addSeparator();
        fileMenu.add(fileExit = getNextMenuItem());
        add(fileMenu);
        editMenu = getNextMenu();
        editMenu.add(editCut = getNextMenuItem());
        editMenu.add(editCopy = getNextMenuItem());
        editMenu.add(editPaste = getNextMenuItem());
        editMenu.add(editPasteSpecial = getNextMenuItem());
        editMenu.add(editCopyCellFormat = getNextCheckboxMenuItem());
        editMenu.addSeparator();
        editMenu.add(editPolygonPoints = getNextCheckboxMenuItem());
        editMenu.add(editSelectAllObjects = getNextMenuItem());
        editMenu.addSeparator();
        editMenu.add(editSort = getNextMenuItem());
        editMenu.add(editFill = getNextMenu());
        editFill.add(editFillDown = getNextMenuItem());
        editFill.add(editFillRight = getNextMenuItem());
        editMenu.add(editClear = getNextMenu());
        editClear.add(editClearAll = getNextMenuItem());
        editClear.add(editClearFormats = getNextMenuItem());
        editClear.add(editClearContents = getNextMenuItem());
        editMenu.add(editDelete = getNextMenuItem());
        editMenu.add(editDeleteSheet = getNextMenuItem());
        editMenu.addSeparator();
        editMenu.add(editFind = getNextMenuItem());
        editMenu.add(editReplace = getNextMenuItem());
        editMenu.add(editGoto = getNextMenuItem());
        add(editMenu);
        viewMenu = getNextMenu();
        viewMenu.add(viewToolbars = getNextMenu());
        viewToolbars.add(viewToolbarsStandard = getNextCheckboxMenuItem());
        viewToolbars.add(viewToolbarsFormat = getNextCheckboxMenuItem());
        viewToolbars.add(viewToolbarsDrawing = getNextCheckboxMenuItem());
        viewMenu.add(viewFormulaBar = getNextCheckboxMenuItem());
        viewMenu.add(viewStatusBar = getNextCheckboxMenuItem());
        add(viewMenu);
        insertMenu = getNextMenu();
        insertMenu.add(insertCells = getNextMenuItem());
        insertMenu.add(insertRows = getNextMenuItem());
        insertMenu.add(insertColumns = getNextMenuItem());
        insertMenu.add(insertWorksheet = getNextMenuItem());
        insertMenu.addSeparator();
        insertMenu.add(insertPageBreak = getNextMenuItem());
        insertMenu.add(insertName = getNextMenuItem());
        getNextMenu();
        getNextMenuItem();
        getNextMenuItem();
        getNextMenuItem();
        getNextMenuItem();
        getNextMenuItem();
        getNextMenu();
        getNextMenu();
        getNextMenu();
        getNextMenu();
        getNextMenuItem();
        getNextMenuItem();
        add(insertMenu);
        formatMenu = getNextMenu();
        formatMenu.add(formatCells = getNextMenuItem());
        formatMenu.add(formatRow = getNextMenu());
        formatRow.add(formatRowHeight = getNextMenuItem());
        formatRow.add(formatRowHide = getNextMenuItem());
        formatRow.add(formatRowUnhide = getNextMenuItem());
        formatRow.add(formatRowDefaultHeight = getNextMenuItem());
        formatMenu.add(formatColumn = getNextMenu());
        formatColumn.add(formatColumnWidth = getNextMenuItem());
        formatColumn.add(formatColumnAutoFit = getNextMenuItem());
        formatColumn.add(formatColumnHide = getNextMenuItem());
        formatColumn.add(formatColumnUnhide = getNextMenuItem());
        formatColumn.add(formatColumnDefaultWidth = getNextMenuItem());
        formatMenu.add(formatSheet = getNextMenu());
        formatSheet.add(formatSheetProperties = getNextMenuItem());
        formatSheet.add(formatSheetProtection = getNextCheckboxMenuItem());
        formatMenu.addSeparator();
        formatMenu.add(formatConditionFormat = getNextMenuItem());
        formatMenu.add(formatFreezePanes = getNextCheckboxMenuItem());
        formatMenu.add(formatDefaultFont = getNextMenuItem());
        formatMenu.addSeparator();
        formatMenu.add(formatObject = getNextMenuItem());
        formatMenu.add(formatBringToFront = getNextMenuItem());
        formatMenu.add(formatSendToBack = getNextMenuItem());
        add(formatMenu);
        toolsMenu = getNextMenu();
        toolsMenu.add(toolsRecalc = getNextMenuItem());
        toolsMenu.add(toolsOptions = getNextMenuItem());
        add(toolsMenu);
        helpMenu = getNextMenu();
        getNextMenuItem();
        helpMenu.add(helpAbout = getNextMenuItem());
        add(helpMenu);
        fileNew.setAccelerator(getNextShortcut());
        if(isApplication)
        {
            fileOpen.setAccelerator(getNextShortcut());
            fileSave.setAccelerator(getNextShortcut());
        } else
        {
            fileRead.setAccelerator(getNextShortcut());
            fileWrite.setAccelerator(getNextShortcut());
        }
        filePrint.setAccelerator(getNextShortcut());
        editCut.setAccelerator(getNextShortcut());
        editCopy.setAccelerator(getNextShortcut());
        editPaste.setAccelerator(getNextShortcut());
        editFillDown.setAccelerator(getNextShortcut());
        editFillRight.setAccelerator(getNextShortcut());
        editFind.setAccelerator(getNextShortcut());
        editReplace.setAccelerator(getNextShortcut());
        editGoto.setAccelerator(getNextShortcut());
        formatCells.setAccelerator(getNextShortcut());
    }

    JMenu getNextMenu()
    {
        JMenu menu = new JMenu(localInfo.getNextMenuString());
        menu.addActionListener(actionListener);
        return menu;
    }

    JMenuItem getNextMenuItem()
    {
        JMenuItem menuitem = new JMenuItem(localInfo.getNextMenuString());
        menuitem.addActionListener(actionListener);
        return menuitem;
    }

    JCheckBoxMenuItem getNextCheckboxMenuItem()
    {
        JCheckBoxMenuItem checkboxmenuitem = new JCheckBoxMenuItem(localInfo.getNextMenuString());
        checkboxmenuitem.addActionListener(actionListener);
        return checkboxmenuitem;
    }

    KeyStroke getNextShortcut()
    {
        return KeyStroke.getKeyStroke(localInfo.getNextShortcutKey(), 2);
    }
}
