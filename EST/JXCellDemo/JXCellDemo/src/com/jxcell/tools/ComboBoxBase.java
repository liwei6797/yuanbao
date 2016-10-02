package com.jxcell.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class ComboBoxBase extends JComboBox
{
    public class ComboBoxPanel extends JPanel
        implements ListCellRenderer
    {

        ComboBoxBase cmbBase;
        boolean cellHasFocus;
        boolean isSelected;

        public ComboBoxPanel(ComboBoxBase comboBox2)
        {
            cellHasFocus = false;
            isSelected = false;
            cmbBase = comboBox2;
        }

        public Component getListCellRendererComponent(JList jlist, Object obj, int index, boolean isSelected, boolean cellHasFocus)
        {
            this.cellHasFocus = cellHasFocus;
            this.isSelected = isSelected;
            if(index >= 0)
                return cmbBase.getSamplePanel();
            else
                return this;
        }

        public void paint(Graphics g)
        {
            super.paint(g);
            Dimension dimension = getSize();
            cmbBase.getSamplePanel().draw(g, 0, 0, dimension.width, dimension.height);
        }
    }

    SamplePanel samplePanel;
    JComboBox cmb;

    public ComboBoxBase()
    {
        cmb = this;
        setUI(new ComboBoxUI(this));
        ComboBoxPanel comboBoxPanel1 = new ComboBoxPanel(this);
        setRenderer(comboBoxPanel1);
    }

    public void addActionListener(ActionListener actionlistener)
    {
        samplePanel.addActionListener(this, actionlistener, "chooserChanged");
    }

    public SamplePanel getSamplePanel()
    {
        return samplePanel;
    }

    public int getSelectedIndex()
    {
        if(samplePanel == null)
            return -1;
        else
            return samplePanel.getSelectedColorIndex();
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        samplePanel.removeActionListener(actionlistener);
    }

    public void addSamplePanel(SamplePanel panel1)
    {
        samplePanel = panel1;
        addItem(panel1);
        Dimension dimension = new Dimension(samplePanel.getUnitWidth() + 16, 16);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    public void setSelectedIndex(int i)
    {
        if(samplePanel != null)
            samplePanel.setSelectedIndex(i);
    }
}
