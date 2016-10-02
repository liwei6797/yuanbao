package com.jxcell.tools;

import com.jxcell.BookAdapter;

public class GradientComboBox extends ComboBoxBase
{

    private GradientPanel gradientPanel;

    public GradientComboBox(BookAdapter adapter)
    {
        gradientPanel = new GradientPanel(adapter);
        addSamplePanel(gradientPanel);
    }

    public void setColor(int fg, int bg)
    {
        gradientPanel.setColor(fg, bg);
        repaint();
    }

    public void setfgColor(int fg)
    {
        gradientPanel.setfgColor(fg);
        repaint();
    }

    public void setbgColor(int bg)
    {
        gradientPanel.setbgColor(bg);
        repaint();
    }
}
