package com.jxcell.dialog;

import java.awt.*;


class StatePanel extends LinePanel
{

    private short state;
    private Color backColor;
    private Color undefColor;

    StatePanel(Color color, Color color1, int width, int height)
    {
        super((short)0, color1, width, height);
        state = -1;
        setBackColor(color);
        setPanelStyle((short)1);
        setColorStyle((short)3);
    }

    short getState()
    {
        return state;
    }

    Color getUndefinedColor()
    {
        return undefColor;
    }

    void setBackColor(Color color)
    {
        if(backColor != color)
        {
            backColor = color;
            undefColor = new Color(color.getRed() + 192 & 0xff, color.getGreen() + 192 & 0xff, color.getBlue() + 192 & 0xff);
            repaint();
        }
    }

    void setState(boolean outline, short style, Color color)
    {
        if(!outline)
        {
            state = (short)(style != 0 ? 1 : 0);
            setBackground(backColor);
            setLineStyle(state != 1 ? 0 : style);
            setLineColor(color);
        } else
        {
            state = 2;
            setBackground(undefColor);
            setLineStyle((short)0);
        }
    }
}
