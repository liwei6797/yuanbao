package com.jxcell.tools;

import com.jxcell.paint.Brush;
import com.jxcell.paint.DC;
import com.jxcell.BookAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class FillPanel extends JPanel
    implements ActionListener
{
    class FillFramePanel extends FramePanel
    {

        FillPanel m_FillPanel;
        BookAdapter m_Adapter;

        FillFramePanel(FillPanel fillPanel, BookAdapter adapter)
        {
            super("Sample");
            m_FillPanel = fillPanel;
            m_Adapter = adapter;
            Dimension dimension = new Dimension(100, 60);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
        }

        public void paint(Graphics g)
        {
            super.paint(g);
            DC dc = m_Adapter.getDC(this, g, m_Adapter, false);
            dc.setNullPen();
            dc.setFill(m_FillPanel.getBrush());
            Dimension dimension = getSize();
            dc.rectangle(5, 17, dimension.width - 10, dimension.height - 22);
            m_Adapter.release(dc);
        }
    }

    ButtonGroup buttongroup;
    JRadioButton rbAuto;
    JRadioButton rbTrans;
    JRadioButton rbSolid;
    JRadioButton rbPattern;
    JRadioButton rbGradient;
    FramePanel fillColorPanel;
    FramePanel patternColorPanel;
    ColorPanel FGColorPanel;
    ColorPanel BGColorPanel;
    PatternComboBox patternComboBox;
    GradientComboBox gradientComboBox;
    FillFramePanel fillFramePanel;
    Brush m_brush;
    Brush m_brush1;
    ActionEvent event;
    Vector m_Vector;

    public FillPanel(BookAdapter adapter)
    {
        m_brush = new Brush();
        m_brush1 = new Brush();
        init(adapter, m_brush, m_brush1, 239);
    }

    public FillPanel(BookAdapter adapter, int option1)
    {
        m_brush = new Brush();
        m_brush1 = new Brush();
        init(adapter, m_brush, m_brush1, option1);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == rbAuto)
            setSelected(m_brush1);
        else
        if(obj == rbTrans)
            m_brush.setPattern(0);
        else
        if(obj == rbSolid)
            m_brush.setPattern(1);
        else
        if(obj == rbPattern)
            m_brush.setPattern(2 + patternComboBox.getSelectedIndex());
        else
        if(obj == rbGradient)
            m_brush.setPattern(51 + gradientComboBox.getSelectedIndex());
        else
        if(obj == patternComboBox)
            m_brush.setPattern(2 + patternComboBox.getSelectedIndex());
        else
        if(obj == gradientComboBox)
            m_brush.setPattern(51 + gradientComboBox.getSelectedIndex());
        else
        if(obj == FGColorPanel)
        {
            m_brush.setFillColor(FGColorPanel.getColorIndex());
            patternComboBox.setfgColor(m_brush.getFillColor());
            gradientComboBox.setfgColor(m_brush.getFillColor());
        } else
        if(obj == BGColorPanel)
        {
            m_brush.setPatternColor(BGColorPanel.getColorIndex());
            patternComboBox.setbgColor(m_brush.getPatternColor());
            gradientComboBox.setbgColor(m_brush.getPatternColor());
        } else
        {
            return;
        }
        setEnabled();
        PostEvents();
    }

    public void addActionListener(ActionListener actionlistener)
    {
        if(m_Vector == null)
        {
            m_Vector = new Vector(1);
            event = new ActionEvent(this, 1001, "brushChanged");
        }
        m_Vector.addElement(actionlistener);
    }

    void setEnabled()
    {
        if(m_brush.isAuto())
        {
            FGColorPanel.setEnabled(false);
            BGColorPanel.setEnabled(false);
            fillColorPanel.setEnabled(false);
            patternColorPanel.setEnabled(false);
            patternComboBox.setEnabled(false);
            gradientComboBox.setEnabled(false);
            rbAuto.setSelected(true);
        } else
        if(m_brush.isHollow())
        {
            FGColorPanel.setEnabled(false);
            BGColorPanel.setEnabled(false);
            fillColorPanel.setEnabled(false);
            patternColorPanel.setEnabled(false);
            patternComboBox.setEnabled(false);
            gradientComboBox.setEnabled(false);
            rbTrans.setSelected(true);
        } else
        if(m_brush.isSolid())
        {
            FGColorPanel.setEnabled(true);
            BGColorPanel.setEnabled(false);
            fillColorPanel.setEnabled(true);
            patternColorPanel.setEnabled(false);
            patternComboBox.setEnabled(false);
            gradientComboBox.setEnabled(false);
            rbSolid.setSelected(true);
            fillColorPanel.setTitle("Fill Color");
        } else
        if(m_brush.isValidPattern(m_brush.getPattern()))
        {
            FGColorPanel.setEnabled(true);
            BGColorPanel.setEnabled(true);
            fillColorPanel.setEnabled(true);
            patternColorPanel.setEnabled(true);
            patternComboBox.setEnabled(true);
            gradientComboBox.setEnabled(false);
            rbPattern.setSelected(true);
            fillColorPanel.setTitle("Fill Color");
            patternColorPanel.setTitle("Pattern Color");
        } else
        if(m_brush.isGradient())
        {
            FGColorPanel.setEnabled(true);
            BGColorPanel.setEnabled(true);
            fillColorPanel.setEnabled(true);
            patternColorPanel.setEnabled(true);
            patternComboBox.setEnabled(false);
            gradientComboBox.setEnabled(true);
            rbGradient.setSelected(true);
            fillColorPanel.setTitle("Start Color");
            patternColorPanel.setTitle("End Color");
        }
        if(fillFramePanel != null)
            fillFramePanel.repaint();
    }

    void PostEvents()
    {
        if(m_Vector != null && m_Vector.size() > 0)
        {
            for(int j = 0; j < m_Vector.size(); j++)
                if(m_Vector.elementAt(j) != null)
                    ((ActionListener)m_Vector.elementAt(j)).actionPerformed(event);
        }
    }

    public Brush getBrush()
    {
        return m_brush;
    }

    JPanel getPanel1(int option)
    {
        JPanel jpanel = new JPanel();
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        GridBagLayout gridbaglayout = new GridBagLayout();
        jpanel.setLayout(gridbaglayout);
        gridbagconstraints.anchor = 17;
        gridbagconstraints.fill = 0;
        gridbagconstraints.gridwidth = 2;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        if((option & 1) != 0)
        {
            buttongroup.add(rbAuto);
            rbAuto.addActionListener(this);
            gridbaglayout.setConstraints(rbAuto, gridbagconstraints);
            jpanel.add(rbAuto);
            gridbagconstraints.gridy++;
        }
        if((option & 2) != 0)
        {
            buttongroup.add(rbTrans);
            rbTrans.addActionListener(this);
            gridbaglayout.setConstraints(rbTrans, gridbagconstraints);
            jpanel.add(rbTrans);
            gridbagconstraints.gridy++;
        }
        if((option & 4) != 0)
        {
            buttongroup.add(rbSolid);
            rbSolid.addActionListener(this);
            gridbaglayout.setConstraints(rbSolid, gridbagconstraints);
            jpanel.add(rbSolid);
            gridbagconstraints.gridy++;
        }
        gridbagconstraints.gridwidth = 1;
        if((option & 8) != 0)
        {
            buttongroup.add(rbPattern);
            rbPattern.addActionListener(this);
            gridbaglayout.setConstraints(rbPattern, gridbagconstraints);
            jpanel.add(rbPattern);
            patternComboBox.addActionListener(this);
            gridbagconstraints.gridx = 1;
            gridbagconstraints.fill = 2;
            gridbaglayout.setConstraints(patternComboBox, gridbagconstraints);
            jpanel.add(patternComboBox);
            gridbagconstraints.gridy++;
            gridbagconstraints.gridx = 0;
            gridbagconstraints.fill = 0;
        }
        if((option & 0x10) != 0)
        {
            buttongroup.add(rbGradient);
            rbGradient.addActionListener(this);
            gridbaglayout.setConstraints(rbGradient, gridbagconstraints);
            jpanel.add(rbGradient);
            gradientComboBox.addActionListener(this);
            gridbagconstraints.gridx = 1;
            gridbagconstraints.fill = 2;
            gridbaglayout.setConstraints(gradientComboBox, gridbagconstraints);
            jpanel.add(gradientComboBox);
        }
        return jpanel;
    }

    JPanel getPanel2(int option1)
    {
        if((option1 & 0x20) == 0 && (option1 & 0x40) == 0)
            return null;
        JPanel jpanel = new JPanel();
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        GridBagLayout gridbaglayout = new GridBagLayout();
        jpanel.setLayout(gridbaglayout);
        gridbagconstraints.anchor = 18;
        gridbagconstraints.fill = 0;
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        if((option1 & 0x20) != 0)
        {
            FGColorPanel.addActionListener(this);
            GridBagConstraints gridbagconstraints1 = new GridBagConstraints();
            GridBagLayout gridbaglayout1 = new GridBagLayout();
            fillColorPanel.setLayout(gridbaglayout1);
            gridbagconstraints1.anchor = 18;
            gridbagconstraints1.fill = 0;
            gridbagconstraints1.gridwidth = 1;
            gridbagconstraints1.gridheight = 1;
            gridbagconstraints1.gridx = 0;
            gridbagconstraints1.gridy = 0;
            gridbaglayout1.setConstraints(FGColorPanel, gridbagconstraints1);
            fillColorPanel.add(FGColorPanel);
            gridbaglayout.setConstraints(fillColorPanel, gridbagconstraints);
            jpanel.add(fillColorPanel);
            gridbagconstraints.gridx++;
        }
        if((option1 & 0x40) != 0)
        {
            BGColorPanel.addActionListener(this);
            GridBagConstraints gridbagconstraints2 = new GridBagConstraints();
            GridBagLayout gridbaglayout2 = new GridBagLayout();
            patternColorPanel.setLayout(gridbaglayout2);
            gridbagconstraints2.anchor = 18;
            gridbagconstraints2.fill = 0;
            gridbagconstraints2.gridwidth = 1;
            gridbagconstraints2.gridheight = 1;
            gridbagconstraints2.gridx = 0;
            gridbagconstraints2.gridy = 0;
            gridbaglayout2.setConstraints(BGColorPanel, gridbagconstraints2);
            patternColorPanel.add(BGColorPanel);
            gridbaglayout.setConstraints(patternColorPanel, gridbagconstraints);
            jpanel.add(patternColorPanel);
        }
        return jpanel;
    }

    void init(BookAdapter adapter, Brush brush, Brush brush2, int option1)
    {
        buttongroup = new ButtonGroup();
        rbAuto = new JRadioButton("Automatic");
        rbAuto.setMnemonic('A');
        rbTrans = new JRadioButton("Transparent");
        rbTrans.setMnemonic('T');
        rbSolid = new JRadioButton("Solid");
        rbSolid.setMnemonic('S');
        rbPattern = new JRadioButton("Pattern: ");
        rbPattern.setMnemonic('P');
        rbGradient = new JRadioButton("Gradient: ");
        rbGradient.setMnemonic('G');
        gradientComboBox = new GradientComboBox(adapter);
        patternComboBox = new PatternComboBox((BookAdapter)adapter, (option1 & 0x100) != 0);
        fillColorPanel = new FramePanel("Fill Color");
        patternColorPanel = new FramePanel("Pattern Color");
        FGColorPanel = new ColorPanel((BookAdapter)adapter);
        BGColorPanel = new ColorPanel((BookAdapter)adapter);
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        GridBagLayout gridbaglayout = new GridBagLayout();
        setLayout(gridbaglayout);
        gridbagconstraints.anchor = 18;
        gridbagconstraints.fill = 0;
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        JPanel jpanel = getPanel1(option1);
        if(jpanel != null)
        {
            gridbaglayout.setConstraints(jpanel, gridbagconstraints);
            add(jpanel);
        }
        jpanel = getPanel2(option1);
        if(jpanel != null)
        {
            gridbagconstraints.gridx = 1;
            gridbagconstraints.insets.left = 5;
            gridbaglayout.setConstraints(jpanel, gridbagconstraints);
            add(jpanel);
            gridbagconstraints.insets.left = 0;
        }
        if((option1 & 0x80) != 0)
        {
            fillFramePanel = new FillFramePanel(this, adapter);
            gridbagconstraints.gridx = 0;
            gridbagconstraints.gridy = 1;
            gridbagconstraints.gridwidth = 2;
            gridbagconstraints.gridheight = 1;
            gridbagconstraints.fill = 1;
            gridbaglayout.setConstraints(fillFramePanel, gridbagconstraints);
            add(fillFramePanel);
        }
        FGColorPanel.setselectedIndex(1);
        BGColorPanel.setselectedIndex(0);
        setSelected(brush);
        setBrush(brush2);
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        m_Vector.removeElement(actionlistener);
    }

    public void setBrush(Brush brush)
    {
        if(brush != m_brush1)
        {
            m_brush1.copy(brush);
            FGColorPanel.setselectedIndex(m_brush1.getFillColor());
            BGColorPanel.setselectedIndex(m_brush1.getPatternColor());
            if(m_brush.isAuto())
            {
                setSelected(m_brush1);
                return;
            }
            FGColorPanel.setSelected1(m_brush.getFillColor());
            BGColorPanel.setSelected1(m_brush.getPatternColor());
        }
    }

    public void setSelected(Brush brush)
    {
        if(brush != m_brush)
            m_brush.copy(brush);
        FGColorPanel.setSelected1(m_brush.getFillColor());
        BGColorPanel.setSelected1(m_brush.getPatternColor());
        patternComboBox.setColor(m_brush.getFillColor(), m_brush.getPatternColor());
        patternComboBox.setSelectedIndex(m_brush.isPattern(m_brush.getPattern()) ? m_brush.getPattern() - 2 : 0);
        gradientComboBox.setColor(m_brush.getFillColor(), m_brush.getPatternColor());
        gradientComboBox.setSelectedIndex(m_brush.isGradient() ? m_brush.getPattern() - 51 : 0);
        setEnabled();
    }
}
