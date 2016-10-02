package com.jxcell.tools;

import com.jxcell.paint.Palette;
import com.jxcell.BookAdapter;

import java.awt.*;


public class ColorPanel extends SamplePanel
{

    private BookAdapter m_Adapter;
    private Palette m_Palette;
    private int m_nSelected;
    private int m_nselectedindex;

    public ColorPanel(BookAdapter adapter)
    {
        super(8, 7, 14, 14, 4, 4, "Automatic", null);
        m_nSelected = 0;
        init(adapter);
    }

    public ColorPanel(BookAdapter adapter, String autostr, int sel, String ustr, int index)
    {
        this(autostr, sel, ustr, index);
        init(adapter);
    }

    public ColorPanel(Palette palette1)
    {
        this(null, 0, null, 0);
        m_Palette = palette1;
    }

    public ColorPanel(String autostr, int sel, String ustr, int index)
    {
        super(8, 7, 14, 14, 4, 4, autostr, ustr);
        m_nSelected = 0;
        m_nSelected = sel;
        m_nselectedindex = index;
    }

    public Color getColor(int index)
    {
        index = getselectedindex(index);
        if(m_Adapter != null && index >= 0 && index < 73)
            return m_Adapter.getColor(index);
        else
            return null;
    }

    public Palette getPalette()
    {
        return m_Palette;
    }

    private int getselectedindex(int index)
    {
        if(super.m_StrAutomatic != null && index == 0)
            return m_nSelected;
        if(super.m_StrU != null && index >= getSelectedIndex() - 1)
            return m_nselectedindex;
        if(super.m_StrAutomatic != null)
            index--;
        if(index >= 0)
            return 8 + index;
        else
            return -1;
    }

    public int getBlue()
    {
        int rgb = getRGB();
        return rgb & 0xff;
    }

    public Color getSelectedColor()
    {
        return getColor(getSelectedColorIndex());
    }

    public int getGreen()
    {
        int rgb = getRGB();
        return rgb >> 8 & 0xff;
    }

    public int getColorIndex()
    {
        return getselectedindex(getSelectedColorIndex());
    }

    public int getRGB()
    {
        return m_Palette.getRGB(getColorIndex());
    }

    public int getRed()
    {
        int l = getRGB();
        return l >> 16 & 0xff;
    }

    public void draw(Graphics g1, int palindex, int x, int y, int width, int height)
    {
        Color color;
        if(m_Adapter == null)
            color = new Color(m_Palette.getRGB(palindex + 8));
        else
            color = m_Adapter.getColor(palindex + 8);
        if(!isEnabled())
            color = color.darker();
        g1.setColor(color);
        g1.fillRect(x, y, width, height);
    }

    public void init(BookAdapter adapter)
    {
        this.m_Adapter = adapter;
        m_Palette = this.m_Adapter.getPalette();
    }

    public void setselectedIndex(int index)
    {
        m_nSelected = index;
        if(index == getColorIndex())
            setSelected1(index);
    }

    public void setdefColor(int index)
    {
        m_Palette.setDefaultRGB(index);
        repaint();
    }

    public void resetDefColors()
    {
        m_Palette.setDefaultRGBs();
        repaint();
    }

    public void setRGB(int index, int r, int g, int b)
    {
        m_Palette.setRGB(index, r, g, b);
        repaint();
    }

    public void setSelected(Color color)
    {
        int index;
        index = m_Palette.getNearestPaletteIndex((index = color.getRed()) << 16 | color.getGreen() << 8 | color.getBlue(), 8) - 8;
        if(super.m_StrAutomatic != null)
            index++;
        super.setSelectedIndex(index);
    }

    public void setSelected1(int index)
    {
        if(super.m_StrAutomatic != null && m_nSelected == index)
        {
            super.setSelectedIndex(0);
            return;
        }
        if(super.m_StrU != null && m_nselectedindex == index)
        {
            super.setSelectedIndex(getSelectedIndex() - 1);
            return;
        }
        if(index >= 8 && index <= 63)
        {
            index -= 8;
            if(super.m_StrAutomatic != null)
                index++;
            super.setSelectedIndex(index);
            return;
        }
        if(m_Adapter != null)
            setSelected(m_Adapter.getColor(index));
    }

    public void setSelected(int index)
    {
        int index1 = m_Palette.getNearestPaletteIndex(index, 8) - 8;
        if(super.m_StrAutomatic != null)
            index1++;
        super.setSelectedIndex(index1);
    }
}
