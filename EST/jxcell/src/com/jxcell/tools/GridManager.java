package com.jxcell.tools;

import com.jxcell.dialog.TabBase;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.EventListener;


public class GridManager 
{


    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    public Insets insets[] = {
        new Insets(0, 0, 0, 0), new Insets(10, 0, 0, 0), new Insets(0, 10, 0, 0), new Insets(0, 0, 10, 0), new Insets(0, 0, 0, 10), new Insets(10, 10, 0, 0), new Insets(5, 10, 0, 0), new Insets(0, 5, 0, 0), new Insets(0, 10, 10, 0), new Insets(0, 5, 10, 0),
        new Insets(5, 0, 0, 0), new Insets(5, 5, 5, 5), new Insets(10, 5, 0, 0)
    };

    public GridManager()
    {
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
    }

    public void insert(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy, int gridwidth, int gridheight,
            int weightx, int weighty, int ipadx, int ipady, Insets insets, int fill, int anchor)
    {
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.gridwidth = gridwidth;
        gridBagConstraints.gridheight = gridheight;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.ipadx = ipadx;
        gridBagConstraints.ipady = ipady;
        gridBagConstraints.insets = insets;
        gridBagConstraints.fill = fill;
        gridBagConstraints.anchor = anchor;
        gridBagLayout.setConstraints(component, gridBagConstraints);
        container.setLayout(gridBagLayout);
        container.add(component);
        if(addListener(eventlistener, component) && (eventlistener instanceof KeyListener))
            component.addKeyListener((KeyListener)eventlistener);
    }

    public void insert(EventListener eventlistener, Container container, java.awt.Component acomponent[])
    {
        for(int i = 0; i < acomponent.length; i++)
            insert(eventlistener, container, acomponent[i], 0, i);

    }

    public void insert(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy)
    {
        insert(eventlistener, container, component, gridx, gridy, 1, 1, 1, 1, 0, 0, insets[0], GridBagConstraints.NONE, GridBagConstraints.WEST);
    }

    public void insertHW(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy)
    {
        insert(eventlistener, container, component, gridx, gridy, 1, 1, 1, 1, 0, 0, insets[0], GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void insertHN(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy)
    {
        insert(eventlistener, container, component, gridx, gridy, 1, 1, 0, 0, 0, 0, insets[6], GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
    }

    public void insertWithInset(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy, int gridwidth, int gridheight,
            Insets insets)
    {
        insert(eventlistener, container, component, gridx, gridy, gridwidth, gridheight, 1, 1, 0, 0, insets, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }

    public void insertWithInsetHW(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy, int gridwidth, int gridheight,
            Insets insets)
    {
        insert(eventlistener, container, component, gridx, gridy, gridwidth, gridheight, 1, 1, 0, 0, insets, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }

    public void insertWithInsetBC(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy, int gridwidth, int gridheight,
                        Insets insets)
    {
        insert(eventlistener, container, component, gridx, gridy, gridwidth, gridheight, 0, 0, 0, 0, insets, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

    public void insert(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy, boolean bweightx, boolean bweighty)
    {
        insert(eventlistener, container, component, gridx, gridy, 1, 1, bweightx ? 1 : 0, bweighty ? 1 : 0, 0, 0, insets[0], GridBagConstraints.NONE, GridBagConstraints.NORTHWEST);
    }

    public void insert(EventListener eventlistener, Container container, java.awt.Component component, int gridx, int gridy, int gridwidth, int gridheight)
    {
        insert(eventlistener, container, component, gridx, gridy, gridwidth, gridheight, 0, 0, 0, 0, insets[0], GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

    public Insets getInsets(int index)
    {
        return insets[index];
    }

    protected boolean addListener(EventListener eventlistener, java.awt.Component component)
    {
        if(component instanceof AbstractButton)
            ((AbstractButton)component).addActionListener((ActionListener)eventlistener);
        else
        if(component instanceof JTextField)
        {
            JTextField jtextfield = (JTextField)component;
            jtextfield.addFocusListener((FocusListener)eventlistener);
            jtextfield.getDocument().addDocumentListener((DocumentListener)eventlistener);
        }
        else
        {
            if(component instanceof JLabel)
                return false;
            if(component instanceof FramePanel)
                return false;
            if(component instanceof JComboBox)
                ((JComboBox)component).addActionListener((ActionListener)eventlistener);
            else
            if(component instanceof ListPanel)
                ((ListPanel)component).addListSelectionListener(eventlistener);
            else
            if(component instanceof JTextAreaEX)
            {
                JTextAreaEX textAreaEx = (JTextAreaEX)component;
                textAreaEx.getJTextArea().addFocusListener((FocusListener)eventlistener);
                textAreaEx.addListener(eventlistener);
            } else
            if(component instanceof TextFieldSpin)
                ((TextFieldSpin)component).addActionListener((ActionListener)eventlistener);
            else
            if(component instanceof ColorPanel)
                ((ColorPanel)component).addActionListener((ActionListener)eventlistener);
            else
            if(component instanceof LinePanel)
                ((LinePanel)component).addActionListener((ActionListener)eventlistener);
            else
            if(component instanceof FillPanel)
                ((FillPanel)component).addActionListener((ActionListener)eventlistener);
            else
            if(component instanceof JTabbedPane)
                ((JTabbedPane)component).addChangeListener((ChangeListener)eventlistener);
            else
            if(component instanceof JSlider)
                ((JSlider)component).addChangeListener((ChangeListener)eventlistener);
            else
                return false;
        }
        return true;
    }

    protected EventListener getEventListener(Component component)
    {
        for(; component != null && !(component instanceof TabBase) && !(component instanceof DlgBase); component = component.getParent());
        return (EventListener)component;
    }

    public void removeListener(Container container)
    {
        int len = container.getComponentCount();
        for(int j = len; j-- > 0;)
        {
            java.awt.Component component = container.getComponent(j);
            if(component != null)
            {
                if(component instanceof Container)
                    removeListener((Container)component);
                EventListener eventlistener = getEventListener(component);
                if(removeListener(eventlistener, component) && (eventlistener instanceof KeyListener))
                    component.removeKeyListener((KeyListener)eventlistener);
                container.remove(j);
            }
        }
    }

    protected boolean removeListener(EventListener eventlistener, java.awt.Component component)
    {
        if(component instanceof AbstractButton)
            ((AbstractButton)component).removeActionListener((ActionListener)eventlistener);
        else
        if(component instanceof JTextField)
        {
            JTextField jtextfield = (JTextField)component;
            jtextfield.removeFocusListener((FocusListener)eventlistener);
            jtextfield.getDocument().removeDocumentListener((DocumentListener)eventlistener);
        }
        else
        {
            if(component instanceof JLabel)
                return false;
            if(component instanceof FramePanel)
                return false;
            if(component instanceof JComboBox)
                ((JComboBox)component).removeActionListener((ActionListener)eventlistener);
            else
            if(component instanceof ListPanel)
                ((ListPanel)component).removeListSelectionListener(eventlistener);
            else
            if(component instanceof JTextAreaEX)
            {
                JTextAreaEX textAreaEX = (JTextAreaEX)component;
                textAreaEX.getJTextArea().removeFocusListener((FocusListener)eventlistener);
                textAreaEX.addDocumentListener(eventlistener);
            } else
            if(component instanceof TextFieldSpin)
                ((TextFieldSpin)component).removeActionListener((ActionListener)eventlistener);
            else
            if(component instanceof ColorPanel)
                ((ColorPanel)component).removeActionListener((ActionListener)eventlistener);
            else
            if(component instanceof LinePanel)
                ((LinePanel)component).removeActionListener((ActionListener)eventlistener);
            else
            if(component instanceof FillPanel)
                ((FillPanel)component).removeActionListener((ActionListener)eventlistener);
            else
            if(component instanceof JTabbedPane)
                ((JTabbedPane)component).removeChangeListener((ChangeListener)eventlistener);
            else
            if(component instanceof JSlider)
                ((JSlider)component).removeChangeListener((ChangeListener)eventlistener);
            else
                return false;
        }
        return true;
    }
}
