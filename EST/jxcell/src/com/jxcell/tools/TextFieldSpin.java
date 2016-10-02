package com.jxcell.tools;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class TextFieldSpin extends JPanel
    implements ActionListener
{
    public class ArrowButton extends BasicArrowButton
    {

        private final TextFieldSpin tfSpin;

        public ArrowButton(TextFieldSpin fieldSpin, int direction)
        {
            super(direction);
            tfSpin = fieldSpin;
        }

        public Dimension getMinimumSize()
        {
            return getPreferredSize();
        }

        public Dimension getPreferredSize()
        {
            float height = tfSpin.m_FieldEx.getPreferredSize().height;
            return new Dimension((int)((height * 7F) / 8F), (int)((double)(height / 2.0F) + 0.5D));
        }

        public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnable)
        {
            size += 2;
            x--;
            y--;
            super.paintTriangle(g, x, y, size, direction, isEnable);
        }
    }

    public class FieldEx extends TextFieldEx
    {

        private TextFieldSpin tfSpin1;

        public FieldEx(TextFieldSpin fieldSpin)
        {
            tfSpin1 = fieldSpin;
        }

        public void setNum(double v1)
        {
            tfSpin1.setNum(v1);
        }
    }

    FieldEx m_FieldEx;
    ArrowButton m_upArrow;
    ArrowButton m_downArrow;
    double minVal;
    double maxVal;
    double m_nStep;
    double curValue;
    ActionEvent actionEvent;
    Vector values;

    public TextFieldSpin()
    {
        minVal = Double.NEGATIVE_INFINITY;
        maxVal = Double.POSITIVE_INFINITY;
        m_FieldEx = new FieldEx(this);
        setBorder(m_FieldEx.getBorder());
        m_FieldEx.setBorder(null);
        m_upArrow = new ArrowButton(this, 1);
        m_downArrow = new ArrowButton(this, 5);
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        GridBagLayout gridbaglayout = new GridBagLayout();
        setLayout(gridbaglayout);
        gridbagconstraints.anchor = 10;
        gridbagconstraints.fill = 1;
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.weighty = 1.0D;
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.gridheight = 2;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        gridbaglayout.setConstraints(m_FieldEx, gridbagconstraints);
        add(m_FieldEx);
        gridbagconstraints.fill = 1;
        gridbagconstraints.weightx = 0.0D;
        gridbagconstraints.weighty = 0.0D;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 1;
        gridbaglayout.setConstraints(m_upArrow, gridbagconstraints);
        add(m_upArrow);
        gridbagconstraints.gridy = 1;
        gridbaglayout.setConstraints(m_downArrow, gridbagconstraints);
        add(m_downArrow);
        m_upArrow.addActionListener(this);
        m_downArrow.addActionListener(this);
    }

    public TextFieldSpin(double v1, double minVal, double maxVal, double step)
    {
        this();
        setValue(v1);
        setMinVal(minVal);
        setMaxVal(maxVal);
        setStep(step);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == m_upArrow)
        {
            changeValue(m_nStep);
            return;
        }
        if(obj == m_downArrow)
            changeValue(-m_nStep);
    }

    public void addActionListener(ActionListener actionlistener)
    {
        if(values == null)
        {
            values = new Vector(1);
            actionEvent = new ActionEvent(this, 1001, "brushChanged");
        }
        values.addElement(actionlistener);
    }

    void changeValue(double v1)
    {
        double v2 = curValue + v1;
        if(v2 < minVal)
            v2 = minVal;
        else
        if(v2 > maxVal)
            v2 = maxVal;
        if(v2 != curValue)
        {
            setValue(v2);
            postevent();
        }
    }

    void postevent()
    {
        if(values != null && values.size() > 0)
        {
            for(int i = 0; i < values.size(); i++)
                if(values.elementAt(i) != null)
                    ((ActionListener)values.elementAt(i)).actionPerformed(actionEvent);
        }
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    int getnum(double v1)
    {
        int len = numToString(v1).length();
        if(len < numToString(v1 + m_nStep).length())
            len = numToString(v1 + m_nStep).length();
        if(len < numToString(v1 - m_nStep).length())
            len = numToString(v1 - m_nStep).length();
        return len;
    }

    public Dimension getPreferredSize()
    {
        int v1 = getnum(maxVal);
        if(getnum(minVal) > v1)
            v1 = getnum(minVal);
        if(getnum(curValue) > v1)
            v1 = getnum(curValue);
        Dimension dimension = (new JTextField(v1)).getPreferredSize();
        dimension.width += m_upArrow.getPreferredSize().width;
        return dimension;
    }

    String numToString(double v1)
    {
        return String.valueOf(v1);
    }

    public double getValue()
    {
        return curValue;
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        values.removeElement(actionlistener);
    }

    public void setEnabled(boolean enable)
    {
        m_upArrow.setEnabled(enable);
        m_downArrow.setEnabled(enable);
        m_FieldEx.setEnabled(enable);
        super.setEnabled(enable);
    }

    public void setMaxVal(double max)
    {
        maxVal = max;
        m_FieldEx.setValue(minVal, maxVal);
    }

    public void setMinVal(double min)
    {
        minVal = min;
        m_FieldEx.setValue(minVal, maxVal);
    }

    public void setStep(double step)
    {
        m_nStep = step;
    }

    public void setNum(double v1)
    {
        if(v1 < minVal)
            v1 = minVal;
        else
        if(v1 > maxVal)
            v1 = maxVal;
        if(v1 != curValue)
        {
            curValue = v1;
            postevent();
        }
    }

    public void setValue(double val1)
    {
        curValue = val1;
        m_FieldEx.setText(numToString(val1));
    }
}
