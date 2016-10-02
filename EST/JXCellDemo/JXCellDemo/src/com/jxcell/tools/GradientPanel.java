package com.jxcell.tools;

import com.jxcell.paint.DC;
import com.jxcell.BookAdapter;

import java.awt.*;


public class GradientPanel extends SamplePanel
{

    private BookAdapter m_Adapter;
    private int fg;
    private int bg;

    public GradientPanel(BookAdapter adapter)
    {
        super(5, 3, 30, 4);
        fg = 4;
        bg = 5;
        m_Adapter = adapter;
    }

    protected int intest(int x, int y)
    {
        int i = super.intest(x, y);
        if(i + 51 <= 64)
            return i;
        else
            return -1;
    }

    public void draw(Graphics g, int pattern, int x, int y, int width, int height)
    {
        if((pattern += 51) <= 64)
        {
            DC dc = m_Adapter.getDC(this, g, m_Adapter, false);
            dc.setNullPen();
            dc.setFill(pattern, fg, bg);
            dc.rectangle(x, y, width, height);
            m_Adapter.release(dc);
        }
    }

    public void setColor(int fg, int bg)
    {
        this.fg = fg;
        this.bg = bg;
        repaint();
    }

    public void setfgColor(int fg)
    {
        setColor(fg, bg);
    }

    public void setbgColor(int bg)
    {
        setColor(fg, bg);
    }
}
