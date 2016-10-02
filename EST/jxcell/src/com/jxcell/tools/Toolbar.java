package com.jxcell.tools;

import com.jxcell.View;
import com.jxcell.designer.DesignerPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

public class Toolbar extends JPanel
    implements MouseListener, ItemListener
{
    private JComponent controls[];
    private ImagePanel toolbuttons[];
    private Vector listeners;
    private ImagePanel activebutton;
    short actbtnindex;
    short actctrlindex;
    private View view;
    boolean mouseDown;

    public Toolbar(View view1, short len, short len1)
    {
        view = view1;
        toolbuttons = new ImagePanel[len];
        controls = new JComponent[len1];
        setPreferredSize(new Dimension(28, 28));
        setLayout(new FlowLayout(0, 0, 0));
    }

    public void addButton(Image image, short index, Color color)
        throws IOException
    {
        ImagePanel imagepanel = new ImagePanel(image);
        imagepanel.setColor(color);
        toolbuttons[index] = imagepanel;
        imagepanel.setPreferredSize(new Dimension(22, 22));
        add(imagepanel);
        imagepanel.addMouseListener(this);
    }

    public void addButton(Image image, short index)
        throws IOException
    {
        ImagePanel imagepanel = new ImagePanel(image);
        toolbuttons[index] = imagepanel;
        imagepanel.setPreferredSize(new Dimension(22, 22));
        add(imagepanel);
        imagepanel.addMouseListener(this);
    }

    public void addControl(JComponent component, short index)
    {
        controls[index] = component;
        add(component);
        component.addMouseListener(this);
        if(component instanceof JComboBox)
            ((JComboBox)component).addItemListener(this);
    }

    public void addSpaces(int i)
        throws IOException
    {
        for(int j = 1; j <= i; j++)
        {
            ImagePanel imagepanel = new ImagePanel(null);
            imagepanel.setPreferredSize(new Dimension(14, 22));
            imagepanel.setIsSpacer(true);
            add(imagepanel);
        }

    }

    public void paint(Graphics g)
    {
        super.paint(g);
    }

    public Insets getInsets()
    {
        Insets insets = super.getInsets();
        return new Insets(insets.top + 3, insets.left + 5, insets.bottom + 3, insets.right + 5);
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(mouseevent.getSource() instanceof ImagePanel)
        {
            ImagePanel imagepanel = (ImagePanel)mouseevent.getSource();
            if(imagepanel == activebutton)
            {
                imagepanel.setBorder((short)2);
                mouseDown = true;
            }
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if((mouseevent.getSource() instanceof ImagePanel) && mouseDown)
        {
            ImagePanel imagepanel = (ImagePanel)mouseevent.getSource();
            if(imagepanel == activebutton)
            {
                if(!imagepanel.isSticky())
                    imagepanel.setBorder((short)1);
                for(short word0 = 0; word0 < toolbuttons.length; word0++)
                {
                    if(imagepanel != toolbuttons[word0])
                        continue;
                    notifyButtonClicked(word0, imagepanel.isSticky());
                    break;
                }

            }
            mouseDown = false;
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        if(mouseevent.getSource() instanceof ImagePanel)
        {
            ImagePanel imagepanel = activebutton = (ImagePanel)mouseevent.getSource();
            if(!imagepanel.isSticky())
                imagepanel.setBorder((short)1);
            for(short word0 = 0; word0 < toolbuttons.length; word0++)
                if(imagepanel == toolbuttons[word0])
                {
                    actbtnindex = word0;
                    return;
                }

            return;
        }
        Component component = (Component)mouseevent.getSource();
        for(short word1 = 0; word1 < controls.length; word1++)
            if(component == controls[word1])
            {
                actctrlindex = word1;
                return;
            }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        if(mouseevent.getSource() instanceof ImagePanel)
        {
            activebutton = null;
            ImagePanel imagepanel = (ImagePanel)mouseevent.getSource();
            if(!imagepanel.isSticky())
                imagepanel.setBorder((short)0);
            mouseDown = false;
        }
        showStatusString(32);
    }

    public void setButtonState(int index, boolean isSticky, boolean state)
    {
        for(short index1 = 0; index1 < toolbuttons.length; index1++)
            if(isSticky && toolbuttons[index1].isSticky() && index1 != index && state)
            {
                toolbuttons[index1].setSticky(false);
                toolbuttons[index1].setBorder((short)0);
            } else
            if(index1 == index)
            {
                toolbuttons[index1].setSticky(isSticky);
                toolbuttons[index1].setBorder(isSticky ? 2 : ((short) (toolbuttons[index1] != activebutton ? 0 : 1)));
            }

    }

    public void compareAndSelect(short index, String name)
    {
        JComboBox choice = (JComboBox)controls[index];
        for(int i = 0; i < choice.getItemCount(); i++)
        {
            if(!choice.getItemAt(i).equals(name))
                continue;
            if(choice.getSelectedIndex() != i)
            {
                choice.removeItemListener(this);
                choice.setSelectedIndex(i);
                choice.addItemListener(this);
                return;
            }
            break;
        }

    }

    public void showStatusString(int i)
    {
        DesignerPanel designerpanel = (DesignerPanel)view.getParent();
        designerpanel.sbar.setText(i != 31 || designerpanel.defaultStatus == null ? LocalStatusInfo.getStatusString(i) : designerpanel.defaultStatus);
    }

    public int getActiveButtonIndex()
    {
        return actbtnindex;
    }

    public ImagePanel getButton(int i)
    {
        return toolbuttons[i];
    }

    public JComponent getControl(int i)
    {
        return controls[i];
    }
  
    public void itemStateChanged(ItemEvent itemevent)
    {
        JComboBox choice = (JComboBox)itemevent.getSource();
        for(short index = 0; index < controls.length; index++)
        {
            if(choice != controls[index])
                continue;
            String item = (String)choice.getSelectedItem();
            if(item != null)
            {
                notifyItemChanged(index, item);
                return;
            }
            break;
        }
    }

    public void addToolbarListener(ToolbarListener toolbarlistener)
    {
        if(listeners == null)
            listeners = new Vector(1);
        listeners.addElement(toolbarlistener);
    }

    public void removeToolbarListener(ToolbarListener toolbarlistener)
    {
        listeners.removeElement(toolbarlistener);
    }

    private void notifyButtonClicked(short index, boolean sticky)
    {
        if(listeners != null)
        {
            ToolbarEvent toolbarevent = new ToolbarEvent(this, index, sticky);
            for(int i = 0; i < listeners.size(); i++)
                ((ToolbarListener)listeners.elementAt(i)).toolbarButtonClicked(toolbarevent);
        }
    }

    private void notifyItemChanged(short index, String sticky)
    {
        if(listeners != null)
        {
            ToolbarEvent toolbarevent = new ToolbarEvent(this, index, sticky);
            for(int i = 0; i < listeners.size(); i++)
                ((ToolbarListener)listeners.elementAt(i)).toolbarItemChanged(toolbarevent);
        }
    }

    public void requestFocus()
    {
    }
}
