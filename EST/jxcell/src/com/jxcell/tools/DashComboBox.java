package com.jxcell.tools;

import com.jxcell.BookAdapter;

public class DashComboBox extends ComboBoxBase
{

    private DashPanel dashPanel;

    public DashComboBox(BookAdapter adapter)
    {
        dashPanel = new DashPanel(adapter);
        addSamplePanel(dashPanel);
    }

    public void setColor(int color)
    {
        dashPanel.setColor(color);
        repaint();
    }
}
