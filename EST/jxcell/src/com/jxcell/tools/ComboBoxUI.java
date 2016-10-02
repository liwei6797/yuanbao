package com.jxcell.tools;

import javax.swing.plaf.basic.BasicComboBoxUI;


public class ComboBoxUI extends BasicComboBoxUI
{

    private ComboBoxBase m_Combo;

    public ComboBoxUI(ComboBoxBase comboBox)
    {
        m_Combo = comboBox;
    }

    protected javax.swing.plaf.basic.ComboPopup createPopup()
    {
        ComboPopup comboPopup1 = new ComboPopup(m_Combo);
        comboPopup1.getAccessibleContext().setAccessibleParent(comboBox);
        comboPopup1.addMouseListener(m_Combo.getSamplePanel());
        comboPopup1.addMouseMotionListener(m_Combo.getSamplePanel());
        return comboPopup1;
    }
}
