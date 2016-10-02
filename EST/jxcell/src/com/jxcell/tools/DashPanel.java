package com.jxcell.tools;

import com.jxcell.paint.DC;
import com.jxcell.BookAdapter;

import java.awt.*;


public class DashPanel extends SamplePanel
{

    private int m_color;
    private BookAdapter adapter;

    public DashPanel(BookAdapter adapter)
    {
        super(1, 7, 50, 16, 4, 4, null, null);
        m_color = 4;
        this.adapter = adapter;
    }

    public void draw(Graphics g, int palindex, int x, int y, int width, int height)
    {
        getSize();
        DC dc = adapter.getDC(this, g, adapter, false);
        dc.setPen(palindex + 3, 20, m_color);
        dc.moveTo(x + 5, y + height / 2);
        dc.lineTo((x + width) - 5, y + height / 2);
        adapter.release(dc);
    }

    public void setColor(int color)
    {
        m_color = color;
        repaint();
    }
}
