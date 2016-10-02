package com.jxcell.tools;

import com.jxcell.paint.DC;
import com.jxcell.BookAdapter;

import java.awt.*;


public class PatternPanel extends SamplePanel
{

    private BookAdapter adapter;
    private int fg;
    private int bg;

    public PatternPanel(BookAdapter adapter)
    {
        super(8, 6, 16, 4);
        fg = 4;
        bg = 5;
        this.adapter = adapter;
    }

    public PatternPanel(BookAdapter adapter, boolean f)
    {
        super(6, 3, 17, 16, 16, 4, 4, null, null);
        fg = 4;
        bg = 5;
        this.adapter = adapter;
    }

    protected int intest(int x, int y)
    {
        int i = super.intest(x, y);
        if(i + 2 <= 49)
            return i;
        else
            return -1;
    }

    public void draw(Graphics g, int i, int x, int y, int width, int height)
    {
        if(i < 48)
        {
            DC dc1 = adapter.getDC(this, g, adapter, false);
            dc1.setPen(0);
            dc1.setFill(i + 2, fg, bg);
            dc1.rectangle(x, y, width, height);
            adapter.release(dc1);
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
