package com.jxcell.dialog;

import com.jxcell.CellFormat;
import com.jxcell.tools.ColorPanel;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;
import com.jxcell.tools.ListPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;


class FontTab extends TabBase
{

    private ListPanel lstfont;
    private ListPanel lststyle;
    private ListPanel lstsize;
    private ColorPanel colorPanel;
    private JCheckBox chkstrikeout;
    private JCheckBox chkunderline;

    FontTab(TabDialog tabDialog)
    {
        super(tabDialog);
        lstfont = getListPanel(5);
        lstfont.setaddtofirst(true);
        lststyle = getListPanel(4, "Regular;Bold;Italic;Bold Italic");
        lstsize = getListPanel(4);
        chkstrikeout = getJCheckBox("Strikeout;S", false);
        chkunderline = getJCheckBox("Underline;U", false);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Effects And Color");
        colorPanel = getColorPanel();
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for(int i = 0; i < fonts.length; i++)
            lstfont.addElement(fonts[i]);

        for(int j = 8; j <= 72; j += 2)
            lstsize.addElement(Integer.toString(j));

        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, this, new JLabel("Font:"), 0, 0);
        gridManager.insert(this, this, lstfont, 0, 1, 2, 1);
        gridManager.insertHW(this, this, new JLabel("Style:"), 0, 2);
        gridManager.insertHW(this, this, lststyle, 0, 3);
        gridManager.insertWithInsetHW(this, this, new JLabel("Size:"), 1, 2, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, this, lstsize, 1, 3, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetBC(this, this, framepanel, 2, 0, 1, 4, gridManager.getInsets(2));
        gridManager.insert(this, framepanel, chkstrikeout, 0, 0);
        gridManager.insert(this, framepanel, chkunderline, 0, 1);
        gridManager.insertHW(this, framepanel, colorPanel, 0, 2);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        apply();
    }

    public void updateControls()
    {
        com.jxcell.CellFormat cellformat = getCellFormat();
        int i = lstfont.getListSize();
        for(int j = 0; j < i; j++)
        {
            if(!lstfont.getElementAt(j).equals(cellformat.getFontName()))
                continue;
            lstfont.setSelectedIndex(j);
            lstfont.ensureIndexIsVisible(j);
            break;
        }

        if(cellformat.isFontBold())
        {
            if(cellformat.isFontItalic())
                lststyle.setSelectedIndex(3);
            else
                lststyle.setSelectedIndex(1);
        } else
        if(cellformat.isFontItalic())
            lststyle.setSelectedIndex(2);
        else
            lststyle.setSelectedIndex(0);
        i = lstsize.getListSize();
        for(int k = 0; k < i; k++)
        {
            if(!lstsize.getElementAt(k).equals(Integer.toString((int)cellformat.getFontSize())))
                continue;
            lstsize.setSelectedIndex(k);
            lstsize.ensureIndexIsVisible(k);
            break;
        }

        chkstrikeout.setSelected(cellformat.isFontStrikeout());
        chkunderline.setSelected(cellformat.getFontUnderline()!=0);
        colorPanel.setSelected(cellformat.getFontColor());
    }

    void set(CellFormat cellformat)
        throws Throwable
    {
        String fontname = lstfont.getSelectedValue();
        if(fontname != null)
            cellformat.setFontName(fontname);
        int i = lststyle.getSelectedIndex();
        cellformat.setFontBold((i & -3) == 1);
        cellformat.setFontItalic(i >= 2);
        String size = lstsize.getSelectedValue();
        if(size != null)
            cellformat.setFontSize(Integer.parseInt(size));
        cellformat.setFontStrikeout(chkstrikeout.isSelected());
        cellformat.setFontUnderline((short) (chkunderline.isSelected()?1:0));
        cellformat.setFontColor(colorPanel.getRGB());
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        apply();
    }
}
