package com.jxcell.tools;

import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;
import java.awt.event.*;


public class ComboPopup extends BasicComboPopup
{
    class ListMouseAdapter extends java.awt.event.MouseAdapter
    {

        private final ComboPopup comboPopup;

        protected ListMouseAdapter(ComboPopup comboPopup1)
        {
            comboPopup = comboPopup1;
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
            comboPopup.comb.getSamplePanel().mouseClicked(mouseevent);
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
            comboPopup.comb.getSamplePanel().mouseEntered(mouseevent);
        }

        public void mouseExited(MouseEvent mouseevent)
        {
            comboPopup.comb.getSamplePanel().mouseExited(mouseevent);
        }

        public void mousePressed(MouseEvent mouseevent)
        {
            comboPopup.comb.getSamplePanel().mousePressed(mouseevent);
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            comboPopup.comb.getSamplePanel().mouseReleased(mouseevent);
            comboPopup.hide();
        }
    }

    class ListMouseMotionAdapter extends MouseMotionAdapter
    {

        private final ComboPopup cmbPopup;

        protected ListMouseMotionAdapter(ComboPopup comboPopup1)
        {
            cmbPopup = comboPopup1;
        }

        public void mouseDragged(MouseEvent mouseevent)
        {
            cmbPopup.comb.getSamplePanel().mouseDragged(mouseevent);
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
            cmbPopup.comb.getSamplePanel().mouseMoved(mouseevent);
        }
    }

    ComboBoxBase comb;

    public ComboPopup(ComboBoxBase comboBox)
    {
        super(comboBox);
        comb = comboBox;
    }

    protected MouseListener createListMouseListener()
    {
        return new ListMouseAdapter(this);
    }

    protected MouseMotionListener createListMouseMotionListener()
    {
        return new ListMouseMotionAdapter(this);
    }

    public void show()
    {
        Dimension dimension = comboBox.getSize();
        dimension.setSize(comb.getSamplePanel().getPreferredSize().width, getPopupHeightForRowCount(comboBox.getMaximumRowCount()));
        Rectangle rectangle = computePopupBounds(0, comboBox.getBounds().height, dimension.width, dimension.height);
        scroller.setMaximumSize(rectangle.getSize());
        scroller.setPreferredSize(rectangle.getSize());
        scroller.setMinimumSize(rectangle.getSize());
        list.invalidate();
        int index = comboBox.getSelectedIndex();
        if(index == -1)
            list.clearSelection();
        else
            list.setSelectedIndex(index);
        list.ensureIndexIsVisible(list.getSelectedIndex());
        setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());
        show(comboBox, rectangle.x, rectangle.y);
    }
}
