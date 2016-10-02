package com.jxcell.tools;

import com.jxcell.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Comparator;

public class ToolbarStd extends Toolbar
{

    public static final short eNumButtons = 9;
    public static final short eNumControls = 0;
    public static final short eNew = 0;
    public static final short eOpen = 1;
    public static final short eSave = 2;
    public static final short ePrint = 3;
    public static final short eCut = 4;
    public static final short eCopy = 5;
    public static final short ePaste = 6;
    public static final short ePaint = 7;
    public static final short eObjects = 8;

    private java.util.List<String> scaleList = new java.util.ArrayList<String>();

    public ToolbarStd(View view)
        throws IOException
    {
        super(view, (short)11, (short)1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/new.gif")),(short)0);
        addButton(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/open.gif")), (short)1);
        addButton(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/save.gif")), (short)2);
        addSpaces(1);
        addButton(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/print.gif")), (short)3);
        addSpaces(1);
        addButton(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/cut.gif")), (short)4);
        addButton(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/copy.gif")), (short)5);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/paste.gif")),(short)6);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/paint.gif")),(short)7);
        addSpaces(1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/undo.gif")),(short)9);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/redo.gif")),(short)10);
        addSpaces(1);
        JComboBox choice = new JComboBox();
        choice.addItem("25%");
        choice.addItem("50%");
        choice.addItem("75%");
        choice.addItem("100%");
        choice.addItem("200%");
        addControl(choice, (short)0);
        addSpaces(1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/objects.gif")),(short)8);
        setPreferredSize(new Dimension(378, 28));
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        super.mouseEntered(mouseevent);
        if(mouseevent.getSource() instanceof ImagePanel)
        {
            showStatusString(actbtnindex);
        } else
        {
            showStatusString(29 + actctrlindex + 3);
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if((mouseevent.getSource() instanceof ImagePanel) && mouseDown)
        {
            ImagePanel imagepanel = (ImagePanel)mouseevent.getSource();
            if(actbtnindex == 7 && mouseDown)
                setButtonState(actbtnindex, !imagepanel.isSticky(), false);
            super.mouseReleased(mouseevent);
        }
    }

    public void updateAndSelect(short index, String name)
    {
        JComboBox choice = (JComboBox)getControl(index);

        if(scaleList.size() == 0)
            for(int i = 0; i < choice.getItemCount(); i++)
                scaleList.add((String) choice.getItemAt(i));

        for(int i = 0; i < scaleList.size(); i++)
        {
            if(!scaleList.get(i).equals(name))
                continue;
            if(choice.getSelectedIndex() != i)
            {
                choice.removeItemListener(this);
                choice.setSelectedIndex(i);
                choice.addItemListener(this);
            }
            return;
        }
        //get the custom scale value from worksheet and added it to the list
        scaleList.add(name);
        java.util.Collections.sort(scaleList, new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return Integer.parseInt(o1.substring(0,o1.indexOf("%"))) - (Integer.parseInt(o2.substring(0,o2.indexOf("%"))));
            }
        });
        choice.removeItemListener(this);
        choice.removeAllItems();
        for(int i = 0; i < scaleList.size(); i++)
            choice.addItem(scaleList.get(i));
        choice.addItemListener(this);
        updateAndSelect(index, name);

    }
}
