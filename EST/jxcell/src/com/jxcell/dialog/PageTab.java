package com.jxcell.dialog;

import com.jxcell.ss.PrintInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.tools.TextFieldSpin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


class PageTab extends TabBase
{

    private JRadioButton rbPortrait;
    private JRadioButton rbPrintLandscape;
    private JRadioButton rbAdjustTo;
    private JRadioButton rbFitToPage;
    private TextFieldSpin spinPercent;
    private JLabel lbPercent;
    private TextFieldSpin spinFitWidth;
    private TextFieldSpin spinFitHeight;
    private JLabel lbWide;
    private JComboBox paperCmb;
    private JRadioButton rbPageAuto;
    private JRadioButton rbPageStart;
    private TextFieldSpin spinPageNum;

    PageTab(TabDialog tabDialog)
    {
        super(tabDialog);
        GridManager gridManager = new GridManager();
        com.jxcell.tools.FramePanel frmPanel = new FramePanel("Orientation");
        gridManager.insertHW(this, this, frmPanel, 0, 0);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(rbPortrait = getJRadioButton("Portrait;t", false));
        buttongroup.add(rbPrintLandscape = getJRadioButton("Landscape;L", false));
        JPanel jpanel = new JPanel();
        gridManager.insert(this, jpanel, new JLabel("portrait"), 0, 0);
        gridManager.insert(this, jpanel, rbPortrait, 1, 0);
        gridManager.insert(this, frmPanel, jpanel, 0, 0);
        jpanel = new JPanel();
        gridManager.insert(this, jpanel, new JLabel("landscape"), 2, 0);
        gridManager.insert(this, jpanel, rbPrintLandscape, 3, 0);
        gridManager.insert(this, frmPanel, jpanel, 1, 0);
        frmPanel = new FramePanel("Scaling");
        gridManager.insertHW(this, this, frmPanel, 0, 1);
        buttongroup = new ButtonGroup();
        buttongroup.add(rbAdjustTo = getJRadioButton("Adjust To: ;A", false));
        buttongroup.add(rbFitToPage = getJRadioButton("Fit To: ;F", false));
        jpanel = new JPanel();
        gridManager.insert(this, jpanel, rbAdjustTo, 0, 0);
        gridManager.insert(this, jpanel, spinPercent = new TextFieldSpin(100D, 10D, 400D, 5D), 1, 0);
        gridManager.insert(this, jpanel, lbPercent = new JLabel("% normal size"), 2, 0);
        lbPercent.setForeground(rbAdjustTo.getForeground());
        gridManager.insert(this, frmPanel, jpanel, 0, 0);
        jpanel = new JPanel();
        gridManager.insert(this, jpanel, rbFitToPage, 0, 0);
        gridManager.insert(this, jpanel, spinFitWidth = new TextFieldSpin(1.0D, 1.0D, 32767D, 1.0D), 1, 0);
        gridManager.insert(this, jpanel, lbWide = new JLabel("page(s) wide by"), 2, 0);
        gridManager.insert(this, jpanel, spinFitHeight = new TextFieldSpin(1.0D, 1.0D, 32767D, 1.0D), 3, 0);
        lbWide.setForeground(rbFitToPage.getForeground());
        gridManager.insert(this, frmPanel, jpanel, 0, 1);
        jpanel = new JPanel();
        gridManager.insert(this, jpanel, new JLabel("Paper Size:"), 0, 0, 1, 1, 0, 0, 0, 0, new Insets(5, 0, 0, 0), 2, 10);
        gridManager.insert(this, jpanel, paperCmb = getJComboBox(false, "Letter;Letter Small;Tabloid;Ledger;Legal;Statement;Executive;A3;A4;A4 Small;A5;B4;B5;Folio;Quarto;10\" x 14\";11\" x 17\";Note;Envelope #9;Envelope #10;Envelope #11;Envelope #12;Envelope #14;C Sheet;D Sheet;E Sheet;Envelope DL;Envelope C5;Envelope C3;Envelope C4;Envelope C6;Envelope C65;Envelope B4;Envelope B5;Envelope B6;Envelope Italy;Envelope Monarch;Envelope 6 3/4;US Std Fanfold;German Std Fanfold;German Legal Fanfold"), 1, 0, 1, 1, 1, 1, 0, 0, new Insets(5, 0, 0, 0), 2, 10);
        gridManager.insertHW(this, this, jpanel, 0, 2);
        frmPanel = new FramePanel("Page Numbering");
        gridManager.insertHW(this, this, frmPanel, 0, 3);
        buttongroup = new ButtonGroup();
        buttongroup.add(rbPageAuto = getJRadioButton("Automatic;u", false));
        buttongroup.add(rbPageStart = getJRadioButton("Start with page number: ;S", false));
        gridManager.insert(this, frmPanel, rbPageAuto, 0, 0);
        jpanel = new JPanel();
        gridManager.insert(this, jpanel, rbPageStart, 0, 0);
        gridManager.insert(this, jpanel, spinPageNum = new TextFieldSpin(1.0D, 1.0D, 32767D, 1.0D), 1, 0);
        gridManager.insert(this, frmPanel, jpanel, 0, 1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == rbAdjustTo || actionevent.getSource() == rbFitToPage || actionevent.getSource() == rbPageAuto || actionevent.getSource() == rbPageStart)
            fresh();
        super.actionPerformed(actionevent);
    }

    private void fresh()
    {
        if(rbAdjustTo.isSelected())
        {
            spinPercent.setEnabled(true);
            lbPercent.setEnabled(true);
        } else
        {
            spinPercent.setEnabled(false);
            lbPercent.setEnabled(false);
        }
        if(rbFitToPage.isSelected())
        {
            spinFitWidth.setEnabled(true);
            spinFitHeight.setEnabled(true);
            lbWide.setEnabled(true);
        } else
        {
            spinFitWidth.setEnabled(false);
            spinFitHeight.setEnabled(false);
            lbWide.setEnabled(false);
        }
        spinPageNum.setEnabled(!rbPageAuto.isSelected());
    }

    public void updateControls()
    {
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        if(printInfo.isLandscape())
            rbPrintLandscape.setSelected(true);
        else
            rbPortrait.setSelected(true);
        if(printInfo.isFitToPage())
            rbFitToPage.setSelected(true);
        else
            rbAdjustTo.setSelected(true);
        spinFitWidth.setValue(printInfo.getFitToWidth());
        spinFitHeight.setValue(printInfo.getFitToHeight());
        spinPercent.setValue(printInfo.getScale());
        if(printInfo.isAutoPageNumbering())
            rbPageAuto.setSelected(true);
        else
            rbPageStart.setSelected(true);
        spinPageNum.setValue(printInfo.getFirstPageNumber());
        paperCmb.setSelectedIndex(printInfo.getPaperSize() - 1);
        fresh();
    }

    protected void setOptions()
        throws Throwable
    {
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        printInfo.setLandscape(rbPrintLandscape.isSelected());
        if(rbFitToPage.isSelected())
        {
            printInfo.setFitToPage(true);
            int width = (int)spinFitWidth.getValue();
            if(width < 1)
                showError((short)2);
            printInfo.setFitWidth(width);
            int height = (int)spinFitHeight.getValue();
            if(height < 1)
                showError((short)2);
            printInfo.setFitHeight(height);
        } else
        {
            printInfo.setFitToPage(false);
            printInfo.setScale((int)spinPercent.getValue());
        }
        if(rbPageAuto.isSelected())
        {
            printInfo.setAutoPageNumbering(true);
        } else
        {
            printInfo.setAutoPageNumbering(false);
            printInfo.setStartPageNumber((int)spinPageNum.getValue());
        }
        printInfo.setPaperSize((short)(paperCmb.getSelectedIndex() + 1));
    }
}
