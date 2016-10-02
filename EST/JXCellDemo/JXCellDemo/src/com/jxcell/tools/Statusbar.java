package com.jxcell.tools;


import javax.swing.*;
import java.awt.*;

public class Statusbar extends JPanel
{

    private String text;

    public Statusbar()
    {
        setBackground(SystemColor.control);
        setFont(new Font("SansSerif", 0, 12));
        text = LocalStatusInfo.getStatusString(32);
    }

    public void setText(String text)
    {
        if(!text.equals(this.text))
        {
            this.text = text;
            repaint();
        }
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(20, 20);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        FontMetrics fontmetrics = g.getFontMetrics();
        int ascent = fontmetrics.getAscent();
        int width = dimension.width - 1;
        int height = dimension.height - 1;
        g.setColor(SystemColor.controlShadow);
        g.drawLine(0, 2, width, 2);
        g.drawLine(0, 2, 0, height);
        g.setColor(SystemColor.controlLtHighlight);
        g.drawLine(0, height, width, height);
        g.drawLine(width, 2, width, height);
        g.setColor(SystemColor.controlText);
        g.drawString(text, 5, dimension.height / 2 + ascent / 2);
    }
}
