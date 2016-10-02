package com.jxcell.dialog;

import java.awt.*;

class LinePanel extends BasePanel
{

    private short panelStyle;
    private short lineStyle;
    private Color lineColor;

    LinePanel(short style, Color color, int width, int height)
    {
        super(width, height);
        panelStyle = 0;
        lineStyle = style;
        lineColor = color;
    }

    Color getLineColor()
    {
        return lineColor;
    }

    short getLineStyle()
    {
        return lineStyle;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        g.setColor(lineColor);
        if(panelStyle == 0)
        {
            if(lineStyle != 0)
            {
                g.drawRect(4, 4, dimension.width - 9, dimension.height - 9);
                if(lineStyle != 1)
                {
                    if(lineStyle != 2)
                        g.drawRect(6, 6, dimension.width - 13, dimension.height - 13);
                    if(lineStyle == 6)
                        g.setColor(Color.white);
                    g.drawRect(5, 5, dimension.width - 11, dimension.height - 11);
                    return;
                }
            } else
            {
                FontMetrics fontmetrics = g.getFontMetrics();
                int ascent = fontmetrics.getAscent();
                int len1 = fontmetrics.stringWidth("None");
                g.drawString("None", (dimension.width - len1) / 2, ((dimension.height - ascent) / 2 + ascent) - 1);
            }
            return;
        }
        if(panelStyle == 1 && lineStyle != 0)
        {
            int x = dimension.width - 5;
            int y = dimension.height / 2 - 1;
            int y1 = y + 1;
            int y2 = y1 + 1;
            g.drawLine(5, y, x, y);
            if(lineStyle != 1)
            {
                if(lineStyle != 2)
                    g.drawLine(5, y2, x, y2);
                if(lineStyle == 6)
                    g.setColor(getBackground());
                g.drawLine(5, y1, x, y1);
            }
        }
    }

    void setLineColor(Color color)
    {
        if(lineColor != color)
        {
            lineColor = color;
            repaint();
        }
    }

    void setLineStyle(short style)
    {
        if(lineStyle != style)
        {
            lineStyle = style;
            repaint();
        }
    }

    void setPanelStyle(short style)
    {
        if(panelStyle != style)
        {
            panelStyle = style;
            repaint();
        }
    }
}
