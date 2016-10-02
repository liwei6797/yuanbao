package com.jxcell.tools;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class FramePanel extends JPanel
{

    private TitledBorder titledBorder;
    private Color textColor;
    private Color inactiveTextColor;

    public FramePanel(String title)
    {
        titledBorder = new TitledBorder(title);
        textColor = titledBorder.getTitleColor();
        inactiveTextColor = SystemColor.textInactiveText;
        setBorder(titledBorder);
    }

    public Insets getInsets()
    {
        return new Insets(20, 10, 10, 10);
    }

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        titledBorder.setTitleColor(enabled ? textColor : inactiveTextColor);
        repaint();
    }

    public void setTitle(String title)
    {
        titledBorder.setTitle(title);
        repaint();
    }
}
