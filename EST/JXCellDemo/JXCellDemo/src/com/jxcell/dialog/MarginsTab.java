package com.jxcell.dialog;

import com.jxcell.ss.PrintInfo;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;


class MarginsTab extends TabBase
{

    private JTextField lableTop;
    private JTextField lableBottom;
    private JTextField lableLeft;
    private JTextField lableRight;
    private JTextField lableHeader;
    private JTextField lableFooter;
    private JCheckBox chkhorz;
    private JCheckBox chkvert;
    private JComboBox chcunits;

    MarginsTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Margins");
        com.jxcell.tools.FramePanel framepanel1 = new FramePanel("Center");
        lableTop = new JTextField("", 6);
        lableBottom = new JTextField("", 6);
        lableLeft = new JTextField("", 6);
        lableRight = new JTextField("", 6);
        lableHeader = new JTextField("", 6);
        lableFooter = new JTextField("", 6);
        chkhorz = getJCheckBox("Center Horizontally;H", false);
        chkvert = getJCheckBox("Center Vertically;V", false);
        chcunits = getJComboBox(false, "Centimeters;Inches");
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanel, 0, 0, true, false);
        gridManager.insert(this, this, framepanel1, 0, 1, 1, 1, 100, 100, 0, 0, gridManager.getInsets(1), 0, 18);
        gridManager.insertHW(this, framepanel, new JLabel("Top:"), 0, 0);
        gridManager.insertHW(this, framepanel, lableTop, 0, 1);
        gridManager.insertHW(this, framepanel, new JLabel("Bottom:"), 0, 2);
        gridManager.insertHW(this, framepanel, lableBottom, 0, 3);
        gridManager.insertWithInsetHW(this, framepanel, new JLabel("Left:"), 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, lableLeft, 1, 1, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, new JLabel("Right:"), 1, 2, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, lableRight, 1, 3, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, new JLabel("Header:"), 2, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, lableHeader, 2, 1, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, new JLabel("Footer:"), 2, 2, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, lableFooter, 2, 3, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, framepanel, new JLabel("Units:", 4), 0, 4, 1, 1, gridManager.getInsets(1));
        gridManager.insertWithInsetHW(this, framepanel, chcunits, 1, 4, 2, 1, gridManager.getInsets(5));
        gridManager.insert(this, framepanel1, chkhorz, 0, 0);
        gridManager.insert(this, framepanel1, chkvert, 1, 0);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == chcunits)
            setMetric(chcunits.getSelectedIndex() == 0);
        super.actionPerformed(actionevent);
    }

    public JComponent getFocusComponent()
    {
        return lableTop;
    }

    public void updateControls()
    {
        setMetric(SizerDlg.ISMETRIC);
        chcunits.setSelectedIndex(SizerDlg.ISMETRIC ? 0 : 1);
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        chkhorz.setSelected(printInfo.isHCenter());
        chkvert.setSelected(printInfo.isVCenter());
    }

    protected void setOptions()
        throws Throwable
    {
        SizerDlg.ISMETRIC = chcunits.getSelectedIndex() == 0;
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        double d1 = SizerDlg.ISMETRIC ? 2.54D : 1.0D;
        printInfo.setTopMargin(getTextfieldValue(lableTop) / d1);
        printInfo.setBottomMargin(getTextfieldValue(lableBottom) / d1);
        printInfo.setLeftMargin(getTextfieldValue(lableLeft) / d1);
        printInfo.setRightMargin(getTextfieldValue(lableRight) / d1);
        printInfo.setHeaderMargin(getTextfieldValue(lableHeader) / d1);
        printInfo.setFooterMargin(getTextfieldValue(lableFooter) / d1);
        printInfo.setHCenter(chkhorz.isSelected());
        printInfo.setVCenter(chkvert.isSelected());
    }

    private void setMetric(boolean bMetric)
    {
        double res = bMetric ? 2.54D : 1.0D;
        PrintInfo printInfo = (PrintInfo) getPrintInfo();
        lableTop.setText(Double.toString(printInfo.getTopPageMargin() * res));
        lableBottom.setText(Double.toString(printInfo.getBottomPageMargin() * res));
        lableLeft.setText(Double.toString(printInfo.getLeftPageMargin() * res));
        lableRight.setText(Double.toString(printInfo.getRightPageMargin() * res));
        lableHeader.setText(Double.toString(printInfo.getHeaderPageMargin() * res));
        lableFooter.setText(Double.toString(printInfo.getFooterPageMargin() * res));
    }
}
