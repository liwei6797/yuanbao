package com.jxcell.tools;

import com.jxcell.paint.DC;
import com.jxcell.paint.Pen;
import com.jxcell.BookAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class LinePanel extends JPanel
    implements ActionListener
{
    class LineFramePanel extends FramePanel
    {

        LinePanel linePanel;
        BookAdapter m_Adapter;

        LineFramePanel(LinePanel panel2, BookAdapter adapter)
        {
            super("Sample");
            linePanel = panel2;
            m_Adapter = adapter;
            Dimension dimension = new Dimension(100, 60);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
        }

        public void paint(Graphics g)
        {
            super.paint(g);
            DC dc = m_Adapter.getDC(this, g, m_Adapter, false);
            dc.setPen(linePanel.getPen());
            Dimension dimension = getSize();
            int i = dimension.height / 2 + 4;
            dc.moveTo(5, i);
            dc.lineTo(dimension.width - 5, i);
            m_Adapter.release(dc);
        }
    }

    ButtonGroup buttongroup;
    JRadioButton rbAutomatic;
    JRadioButton rbTrans;
    JRadioButton rbSolid;
    JRadioButton rbDash;
    FramePanel framePanalColor;
    JLabel lblWeight;
    ColorPanel colorPanel;
    DashComboBox dashcombobox;
    TextFieldSpin textfieldspin;
    LineFramePanel lineframepanel;
    Pen pen;
    Pen pen1;
    ActionEvent event;
    Vector m_Vector;

    public LinePanel(BookAdapter adapter)
    {
        pen = new Pen();
        pen1 = new Pen();
        init(adapter, pen, pen1, 255);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == rbAutomatic)
            setPen(pen1);
        else
        if(obj == rbTrans)
            pen.setStyle(255);
        else
        if(obj == rbSolid)
            pen.setStyle(1);
        else
        if(obj == rbDash)
            pen.setStyle(3 + dashcombobox.getSelectedIndex());
        else
        if(obj == dashcombobox)
            pen.setStyle(3 + dashcombobox.getSelectedIndex());
        else
        if(obj == colorPanel)
        {
            pen.setColor(colorPanel.getColorIndex());
            dashcombobox.setColor(pen.getColor());
        } else
        if(obj == textfieldspin)
            pen.setWeight((int)(20D * textfieldspin.getValue()));
        else
            return;
        setEnabled();
        postevent();
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
        if(pen.isAuto())
        {
            framePanalColor.setEnabled(false);
            colorPanel.setEnabled(false);
            lblWeight.setEnabled(false);
            textfieldspin.setEnabled(false);
            rbAutomatic.setSelected(true);
            dashcombobox.setEnabled(false);
        } else
        if(pen.isNull())
        {
            framePanalColor.setEnabled(false);
            colorPanel.setEnabled(false);
            lblWeight.setEnabled(false);
            textfieldspin.setEnabled(false);
            rbTrans.setSelected(true);
            dashcombobox.setEnabled(false);
        } else
        if(pen.isSolid())
        {
            framePanalColor.setEnabled(true);
            colorPanel.setEnabled(true);
            lblWeight.setEnabled(true);
            textfieldspin.setEnabled(true);
            dashcombobox.setEnabled(false);
            rbSolid.setSelected(true);
        } else
        if(pen.isDashed())
        {
            framePanalColor.setEnabled(true);
            colorPanel.setEnabled(true);
            lblWeight.setEnabled(true);
            textfieldspin.setEnabled(true);
            rbDash.setSelected(true);
            dashcombobox.setEnabled(true);
        }
        if(lineframepanel != null)
            lineframepanel.repaint();
    }

    void postevent()
    {
        if(m_Vector != null && m_Vector.size() > 0)
        {
            for(int i = 0; i < m_Vector.size(); i++)
                if(m_Vector.elementAt(i) != null)
                    ((ActionListener)m_Vector.elementAt(i)).actionPerformed(event);
        }
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
            buttongroup.add(rbAutomatic);
            rbAutomatic.addActionListener(this);
            gridbaglayout.setConstraints(rbAutomatic, gridbagconstraints);
            jpanel.add(rbAutomatic);
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
            buttongroup.add(rbDash);
            rbDash.addActionListener(this);
            gridbaglayout.setConstraints(rbDash, gridbagconstraints);
            jpanel.add(rbDash);
            dashcombobox.addActionListener(this);
            gridbagconstraints.fill = 2;
            gridbagconstraints.gridx = 1;
            gridbaglayout.setConstraints(dashcombobox, gridbagconstraints);
            jpanel.add(dashcombobox);
            gridbagconstraints.gridy++;
            gridbagconstraints.gridx = 0;
            gridbagconstraints.fill = 0;
        }
        if((option & 0x20) != 0)
        {
            gridbaglayout.setConstraints(lblWeight, gridbagconstraints);
            jpanel.add(lblWeight);
            textfieldspin.addActionListener(this);
            gridbagconstraints.gridx = 1;
            gridbagconstraints.fill = 2;
            gridbaglayout.setConstraints(textfieldspin, gridbagconstraints);
            jpanel.add(textfieldspin);
            gridbagconstraints.gridy++;
            gridbagconstraints.gridx = 0;
        }
        return jpanel;
    }

    JPanel getPanel2(int option)
    {
        if((option & 0x10) == 0)
        {
            return null;
        } else
        {
            colorPanel.addActionListener(this);
            GridBagConstraints gridbagconstraints = new GridBagConstraints();
            GridBagLayout gridbaglayout = new GridBagLayout();
            framePanalColor.setLayout(gridbaglayout);
            gridbagconstraints.anchor = 18;
            gridbagconstraints.fill = 0;
            gridbagconstraints.gridwidth = 1;
            gridbagconstraints.gridheight = 1;
            gridbagconstraints.gridx = 0;
            gridbagconstraints.gridy = 0;
            gridbaglayout.setConstraints(colorPanel, gridbagconstraints);
            framePanalColor.add(colorPanel);
            return framePanalColor;
        }
    }

    public Pen getPen()
    {
        return pen;
    }

    void init(BookAdapter adapter, Pen pen1, Pen ax2, int option)
    {
        buttongroup = new ButtonGroup();
        rbAutomatic = new JRadioButton("Automatic");
        rbAutomatic.setMnemonic('A');
        rbTrans = new JRadioButton("Transparent");
        rbTrans.setMnemonic('T');
        rbSolid = new JRadioButton("Solid");
        rbSolid.setMnemonic('S');
        rbDash = new JRadioButton("Dash: ");
        rbDash.setMnemonic('D');
        framePanalColor = new FramePanel("Color");
        lblWeight = new JLabel("Weight: ");
        colorPanel = new ColorPanel((BookAdapter)adapter);
        dashcombobox = new DashComboBox(adapter);
        textfieldspin = new TextFieldSpin(0.0D, 0.0D, 20D, 0.25D);
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        GridBagLayout gridbaglayout = new GridBagLayout();
        setLayout(gridbaglayout);
        gridbagconstraints.anchor = 18;
        gridbagconstraints.fill = 0;
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.gridheight = 1;
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        JPanel jpanel = getPanel1(option);
        if(jpanel != null)
        {
            gridbaglayout.setConstraints(jpanel, gridbagconstraints);
            add(jpanel);
        }
        jpanel = getPanel2(option);
        if(jpanel != null)
        {
            gridbagconstraints.gridx = 1;
            gridbagconstraints.insets.left = 5;
            gridbaglayout.setConstraints(jpanel, gridbagconstraints);
            add(jpanel);
            gridbagconstraints.insets.left = 0;
        }
        if((option & 0x40) != 0)
        {
            lineframepanel = new LineFramePanel(this, adapter);
            gridbagconstraints.gridx = 0;
            gridbagconstraints.gridy = 1;
            gridbagconstraints.gridwidth = 2;
            gridbagconstraints.fill = 1;
            gridbaglayout.setConstraints(lineframepanel, gridbagconstraints);
            add(lineframepanel);
        }
        colorPanel.setselectedIndex(0);
        setPen(pen1);
        copyPen(ax2);
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        m_Vector.removeElement(actionlistener);
    }

    public void copyPen(Pen pen1)
    {
        if(pen1 != this.pen1)
        {
            this.pen1.copy(pen1);
            colorPanel.setselectedIndex(this.pen1.getColor());
            if(pen.isAuto())
            {
                setPen(this.pen1);
                return;
            }
            colorPanel.setSelected1(pen.getColor());
        }
    }

    public void setPen(Pen pen1)
    {
        if(pen1 != pen)
            pen.copy(pen1);
        if(pen1.getStyle() == 2 || !pen1.isDashed())
            dashcombobox.setSelectedIndex(0);
        else
            dashcombobox.setSelectedIndex(pen1.getStyle() - 3);
        colorPanel.setSelected1(pen.getColor());
        dashcombobox.setColor(pen.getColor());
        textfieldspin.setValue((double)pen.getWeight() / 20D);
        setEnabled();
    }
}
