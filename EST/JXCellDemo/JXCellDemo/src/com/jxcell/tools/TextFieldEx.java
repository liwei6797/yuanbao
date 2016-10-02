package com.jxcell.tools;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class TextFieldEx extends JTextField
{
    class DocumentModel extends PlainDocument
    {

        private TextFieldEx tfEx;

        DocumentModel(TextFieldEx im2)
        {
            tfEx = im2;
        }

        public void insertString(int offs, String str, AttributeSet attributeset)
            throws BadLocationException
        {
            for(int i = 0; i < str.length(); i++)
            {
                char c = str.charAt(i);
                if(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || tfEx.isDouble() && c == '.')
                {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }

            String text = getText(0, getLength());
            text = text.substring(0, offs) + str + text.substring(offs);
            try
            {
                double v1 = 0.0D;
                if(text.length() > 0)
                {
                    v1 = tfEx.isDouble() ? Integer.valueOf(text).doubleValue() : Double.valueOf(text).doubleValue();
                    if(v1 > tfEx.getmax())
                        return;
                    if(v1 < 0.0D)
                    {
                        if(tfEx.getmin() >= 0.0D)
                            return;
                        if(v1 < tfEx.getmin())
                            return;
                    }
                }
                super.insertString(offs, str, attributeset);
                tfEx.setNum(v1);
                return;
            }
            catch(Exception _ex) { }
            if(tfEx.isDouble())
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            text = text.trim();
            if(text.equalsIgnoreCase("."))
            {
                super.insertString(offs, str, attributeset);
                tfEx.setNum(0.0D);
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    double m_min;
    double m_max;
    boolean m_bDouble;

    public TextFieldEx()
    {
        this(false, Double.MIN_VALUE, 2147483647D);
    }

    public TextFieldEx(double min, double max)
    {
        this(false, min, max);
    }

    public TextFieldEx(boolean bDouble, double min, double max)
    {
        m_min = Double.NEGATIVE_INFINITY;
        m_max = Double.POSITIVE_INFINITY;
        m_bDouble = false;
        setHorizontalAlignment(4);
        setPrecision(bDouble);
        setValue(min, max);
        setHorizontalAlignment(4);
    }

    public void setNum(double v1)
    {
    }

    protected Document createDefaultModel()
    {
        return new DocumentModel(this);
    }

    public double getmax()
    {
        return m_max;
    }

    public double getmin()
    {
        return m_min;
    }

    int getLen()
    {
        String smin;
        String smax;
        if(m_bDouble)
        {
            int min = (int)m_min;
            int max = (int)m_max;
            smin = String.valueOf(min);
            smax = String.valueOf(max);
        } else
        {
            smin = String.valueOf(m_min);
            smax = String.valueOf(m_max);
        }
        int v1 = Math.max(smin.length(), smax.length());
        if(v1 > 9)
            return 9;
        else
            return v1;
    }

    public boolean isDouble()
    {
        return m_bDouble;
    }

    public void setPrecision(boolean bDouble)
    {
        m_bDouble = bDouble;
        setColumns(getLen());
    }

    public void setValue(double min, double max)
    {
        m_min = min;
        m_max = max;
        setColumns(getLen());
    }
}
