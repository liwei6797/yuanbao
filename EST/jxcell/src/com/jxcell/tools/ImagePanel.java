package com.jxcell.tools;

import java.awt.*;

public class ImagePanel extends BasePanel
{

    private Image image;
    private short imageStyle;
    private boolean isColor;
    private Color color;

    public ImagePanel(Image image1)
    {
        image = image1;
        imageStyle = 0;
        isColor = false;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        if(image != null)
        {
            if(imageStyle == 0)
                g.drawImage(image, 3, 3, this);
            else
                g.drawImage(image, 1, 1, dimension.width - 2, dimension.height - 2, this);
        }
        if(isColor)
        {
            g.setColor(color);
            g.fillRect((dimension.width - 16) / 2, dimension.height - 5, 16, 4);
        }
    }

    public void setStyle(short style)
    {
        if(imageStyle != style)
        {
            imageStyle = style;
            repaint();
        }
    }

    public boolean getColor()
    {
        return isColor;
    }

    public void setColor(Color color1)
    {
        color = color1;
        isColor = true;
    }
}
