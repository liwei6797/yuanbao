package com.jxcell.dialog;


import javax.swing.*;
import java.awt.*;

abstract class BasePanel extends JPanel
{

    private Dimension dimension;
    private short m_nColorStyle;
    private boolean m_bDrawBackGround;

    BasePanel(int width, int height)
    {
        dimension = new Dimension(width, height);
        m_nColorStyle = 0;
    }

    public Dimension getMinimumSize()
    {
        return dimension;
    }

    public Dimension getPreferredSize()
    {
        return dimension;
    }

    boolean isDrawBackGround()
    {
        return m_bDrawBackGround;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        int x = dimension.width - 1;
        int y = dimension.height - 1;
        java.awt.Color color;
        java.awt.Color color1;
        switch(m_nColorStyle)
        {
        case 1:
            color = SystemColor.controlLtHighlight;
            color1 = SystemColor.controlShadow;
            break;

        case 2:
            color = SystemColor.controlShadow;
            color1 = SystemColor.controlLtHighlight;
            break;

        case 0:
            color = color1 = getBackground();
            break;

        default:
            color = color1 = SystemColor.windowText;
            break;
        }
        g.setColor(color);
        g.drawLine(0, 0, x, 0);
        g.drawLine(0, 0, 0, y);
        g.setColor(color1);
        g.drawLine(0, y, x, y);
        g.drawLine(x, 0, x, y);
    }

    void setColorStyle(short style)
    {
        if(m_nColorStyle != style)
        {
            m_nColorStyle = style;
            repaint();
        }
    }

    void setDrawBackGround(boolean drawbk)
    {
        m_bDrawBackGround = drawbk;
    }
}
