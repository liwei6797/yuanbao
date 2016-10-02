package com.jxcell.tools;

import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel
{

    private short border;
    private boolean isSticky;
    private boolean isSpacer;
    public static final short BORDER_NONE = 0;
    public static final short BORDER_RAISED = 1;
    public static final short BORDER_LOWERED = 2;
    public static final short BORDER_SOLID = 3;

    public BasePanel()
    {
        setLayout(null);
        border = 0;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        int width = dimension.width - 1;
        int height = dimension.height - 1;
        Color color;
        Color color1;
        if(border == 1)
        {
            color = SystemColor.controlLtHighlight;
            color1 = SystemColor.controlShadow;
        } else
        if(border == 2)
        {
            color = SystemColor.controlShadow;
            color1 = SystemColor.controlLtHighlight;
        } else
        if(border == 0)
            color = color1 = getBackground();
        else
            color = color1 = Color.black;
        if(!isSpacer)
        {
            g.setColor(color);
            g.drawLine(0, 0, width, 0);
            g.drawLine(0, 0, 0, height);
            g.setColor(color1);
            g.drawLine(0, height, width, height);
            g.drawLine(width, 0, width, height);
        } else
        {
            g.setColor(color);
            g.drawLine(dimension.width / 2 - 1, 0, dimension.width / 2 - 1, height);
            g.setColor(color1);
            g.drawLine(dimension.width / 2, 0, dimension.width / 2, height);
        }
    }

    public void setBorder(short border)
    {
        if(this.border != border)
        {
            this.border = border;
            repaint();
        }
    }

    public void setIsSpacer(boolean isSpacer)
    {
        if(this.isSpacer = isSpacer)
            setBorder((short)2);
    }

    public boolean isSticky()
    {
        return isSticky;
    }

    public void setSticky(boolean isSticky)
    {
        this.isSticky = isSticky;
    }

    public void requestFocus()
    {
    }
}
