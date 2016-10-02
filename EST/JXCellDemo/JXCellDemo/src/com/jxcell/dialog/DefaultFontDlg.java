package com.jxcell.dialog;

import com.jxcell.View;
import com.jxcell.paint.Font;
import com.jxcell.ss.Book;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.ListPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;


public class DefaultFontDlg extends Dialog
{

    private JLabel lblfont;
    private JLabel lblsize;
    private ListPanel lstfont;
    private ListPanel lstsize;

    public DefaultFontDlg(View view)
    {
        super(view, "Default Font", true);
        lstfont = newListPanel(8);
        lstfont.setaddtofirst(true);
        lstsize = newListPanel(8, 60);
        lblfont = new JLabel("AaBbYyZz", 0);
        lblfont.setOpaque(true);
        lblsize = new JLabel("AaBbYyZz", 0);
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for(int i = 0; i < fonts.length; i++)
            lstfont.addElement(fonts[i]);

        for(int i = 8; i <= 72; i += 2)
            lstsize.addElement(Integer.toString(i));

        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Sample");
        java.awt.Container container = getContentPane();
        GridManager gridManager = new GridManager();
        gridManager.insertHW(this, container, new JLabel("Font:"), 0, 0);
        gridManager.insert(this, container, lstfont, 0, 1, 1, 1);
        gridManager.insertWithInsetHW(this, container, new JLabel("Size:"), 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetBC(this, container, lstsize, 1, 1, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetBC(this, container, framepanel, 0, 3, 2, 1, gridManager.getInsets(1));
        gridManager.insert(this, framepanel, lblfont, 0, 0, 1, 1);
        gridManager.insert(this, framepanel, lblsize, 0, 0, 1, 1);
        gridManager.insertHN(this, container, this.btOK, 2, 0);
        gridManager.insertHN(this, container, this.btCancel, 2, 1);
        pack();
    }

    protected void setdefault()
    {
        Font font1 = getWorkBook().getDefaultFont();
        lblfont.setFont(new java.awt.Font(font1.m_name, 0, font1.m_nHeight / 20));
        lblsize.setFont(new java.awt.Font(font1.m_name, 0, 48));
        java.awt.Font font = lblfont.getFont();
        int size = lstfont.getListSize();
        for(int i = 0; i < size; i++)
        {
            if(!lstfont.getElementAt(i).equals(font.getName()))
                continue;
            lstfont.setSelectedIndex(i);
            lstfont.ensureIndexIsVisible(i);
            break;
        }

        size = lstsize.getListSize();
        for(int i = 0; i < size; i++)
            if(lstsize.getElementAt(i).equals(Integer.toString(font.getSize())))
            {
                lstsize.setSelectedIndex(i);
                lstsize.ensureIndexIsVisible(i);
                return;
            }
    }

    protected void okClicked()
        throws Throwable
    {
        java.awt.Font font = lblfont.getFont();
        ((Book)getWorkBook()).setDefaultFont(font.getName(), font.getSize() * 20);
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        String fontname = lstfont.getSelectedValue();
        String fontsize = lstsize.getSelectedValue();
        if(fontname != null && fontsize != null)
        {
            lblfont.setFont(new java.awt.Font(fontname, 0, Integer.parseInt(fontsize)));
            lblfont.repaint();
        }
    }
}
