package com.jxcell.tools;

import com.jxcell.View;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.io.IOException;

public class ToolbarDraw extends Toolbar
{

    public static final short eNumButtons = 9;
    public static final short eNumControls = 0;
    public static final short eArc = 0;
    public static final short eLine = 1;
    public static final short eOval = 2;
    public static final short ePoly = 3;
    public static final short eRect = 4;
    public static final short eButton = 5;
    public static final short eCheck = 6;
    public static final short eList = 7;
    public static final short ePoints = 8;

    public ToolbarDraw(View view)
        throws IOException
    {
        super(view, (short)5, (short)0);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/line.gif")),(short)0);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/oval.gif")),(short)1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/rect.gif")),(short)2);
        addSpaces(1);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/chart.gif")),(short)3);
        addButton( Toolkit.getDefaultToolkit().createImage(getClass().getResource("/com/jxcell/resources/pic.gif")),(short)4);
        setPreferredSize(new Dimension(182, 28));
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        super.mouseEntered(mouseevent);
        showStatusString(11 + actbtnindex);
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        ImagePanel imagepanel = (ImagePanel)mouseevent.getSource();
        if(mouseDown)
            setButtonState(actbtnindex, !imagepanel.isSticky(), actbtnindex != 7);
        super.mouseReleased(mouseevent);
    }
}
