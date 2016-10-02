package com.jxcell.tools;

import com.jxcell.BookAdapter;

public class PatternComboBox extends ComboBoxBase
{

    private PatternPanel patternPanel;

    public PatternComboBox(BookAdapter adapter, boolean f)
    {
        if(f)
            patternPanel = new PatternPanel(adapter);
        else
            patternPanel = new PatternPanel(adapter, false);
        addSamplePanel(patternPanel);
    }

    public void setColor(int fg, int bg)
    {
        patternPanel.setColor(fg, bg);
        repaint();
    }

    public void setfgColor(int fg)
    {
        patternPanel.setfgColor(fg);
        repaint();
    }

    public void setbgColor(int bg)
    {
        patternPanel.setbgColor(bg);
        repaint();
    }
}
