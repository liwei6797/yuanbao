package com.jxcell.tools;

import com.jxcell.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ToolbarFormat extends Toolbar
{

    public static final short eNumButtons = 13;
    public static final short eNumControls = 2;
    public static final short eBold = 0;
    public static final short eItalic = 1;
    public static final short eUnderline = 2;
    public static final short eColor = 3;
    public static final short eLeft = 4;
    public static final short eCenter = 5;
    public static final short eRight = 6;
    public static final short eAcross = 7;
    public static final short eFixed = 8;
    public static final short eCurrency = 9;
    public static final short ePercent = 10;
    public static final short eFraction = 11;
    public static final short eDateTime = 12;
    public static final short eFontName = 0;
    public static final short eFontSize = 1;

    public ToolbarFormat(View view)
        throws IOException
    {
        super(view, (short)14, (short)2);
        setFont(new Font("SansSerif", 0, 11));
        JComboBox choice = new JComboBox();
        JComboBox choice1 = new JComboBox();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontNames[] = ge.getAvailableFontFamilyNames();

        for(int i = 0; i < fontNames.length; i++)
            choice.addItem(fontNames[i]);
        String size[] = new String[]{"8", "9",
        "10", "11", "12", "14", "16", "18", "20", "22", "24", "26",
        "28", "36", "48", "72"};
        for(short i = 0; i <= 15; i++)
            choice1.addItem(size[i]);

        addControl(choice, (short)0);
        addSpaces(1);
        addControl(choice1, (short)1);
        addSpaces(1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/bold.gif")),(short)0);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/italic.gif")),(short)1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/under.gif")),(short)2);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/fontcolor.gif")), (short)3, Color.RED);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/fillcolor.gif")), (short)13, Color.YELLOW);
        addSpaces(1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/left.gif")),(short)4);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/center.gif")),(short)5);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/right.gif")),(short)6);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/merge.gif")),(short)7);
        addSpaces(1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/fixed.gif")),(short)8);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/currency.gif")),(short)9);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/percent.gif")),(short)10);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/fraction.gif")),(short)11);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/datetime.gif")),(short)12);
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        super.mouseEntered(mouseevent);
        if(mouseevent.getSource() instanceof ImagePanel)
        {
            showStatusString(18 + actbtnindex);
        } else
        {
            showStatusString(32 + actctrlindex + 1);
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if((mouseevent.getSource() instanceof ImagePanel) && mouseDown)
        {
            ImagePanel imagepanel = (ImagePanel)mouseevent.getSource();
            if(actbtnindex != 3 && actbtnindex <= 7 && mouseDown)
                setButtonState(actbtnindex, !imagepanel.isSticky(), false);
            super.mouseReleased(mouseevent);
        }
    }
}
