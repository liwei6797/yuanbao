package com.jxcell.dialog;

import com.jxcell.ss.PrintInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;


class SheetTab extends TabBase
{

    private JCheckBox cbPrintGridLines;
    private JCheckBox cbPrintNoColor;
    private JCheckBox cbPrintRowHeading;
    private JCheckBox cbPrintColHeading;
    private JRadioButton rbPrintLeftToRight;
    private JRadioButton rbPrintRightToLeft;

    SheetTab(TabDialog tabDialog)
    {
        super(tabDialog);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(rbPrintLeftToRight = getJRadioButton("Top To Bottom;T", false));
        buttongroup.add(rbPrintRightToLeft = getJRadioButton("Left To Right;L", false));
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Print Options");
        com.jxcell.tools.FramePanel framepanel1 = new FramePanel("Page Order");
        cbPrintGridLines = getJCheckBox("Grid Lines;G", false);
        cbPrintNoColor = getJCheckBox("No Color;C", false);
        cbPrintRowHeading = getJCheckBox("Row Heading;w", false);
        cbPrintColHeading = getJCheckBox("Column Heading;m", false);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, false, true);
        gridManager.insert(this, this, framepanel1, 1, 0, 1, 1, 100, 100, 0, 0, gridManager.getInsets(2), 0, 18);
        gridManager.insert(this, framepanel, cbPrintGridLines, 0, 0);
        gridManager.insert(this, framepanel, cbPrintNoColor, 0, 1);
        gridManager.insert(this, framepanel, cbPrintRowHeading, 0, 2);
        gridManager.insert(this, framepanel, cbPrintColHeading, 0, 3);
        gridManager.insert(this, framepanel1, rbPrintLeftToRight, 0, 0);
        gridManager.insert(this, framepanel1, rbPrintRightToLeft, 0, 1);
    }

    public void updateControls()
    {
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        cbPrintGridLines.setSelected(printInfo.isShowGridLines());
        cbPrintNoColor.setSelected(printInfo.isNoColor());
        cbPrintRowHeading.setSelected(printInfo.isShowRowHeading());
        cbPrintColHeading.setSelected(printInfo.isShowColHeading());
        rbPrintLeftToRight.setSelected(!printInfo.isLeftToRight());
        rbPrintRightToLeft.setSelected(printInfo.isLeftToRight());
    }

    protected void setOptions()
        throws Throwable
    {
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        printInfo.setShowGridLines(cbPrintGridLines.isSelected());
        printInfo.setNoColor(cbPrintNoColor.isSelected());
        printInfo.setShowRowHeading(cbPrintRowHeading.isSelected());
        printInfo.setShowColHeading(cbPrintColHeading.isSelected());
        printInfo.setLeftToRight(rbPrintRightToLeft.isSelected());
    }
}
