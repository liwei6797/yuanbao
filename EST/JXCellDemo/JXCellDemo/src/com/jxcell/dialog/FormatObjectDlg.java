package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.GRObject;
import com.jxcell.View;


public class FormatObjectDlg extends TabDialog
{

    public static final int kAllPages = 31;
    public static final int kArrowsPage = 16;
    public static final int kFillPage = 4;
    public static final int kLineStylePage = 8;
    public static final int kNamePage = 1;
    public static final int kOptionsPage = 2;
    private static int m_nTab;

    public FormatObjectDlg(View view, int option)
        throws CellException
    {
        super(view, "Format Object", true);
        checkOptions(1, option, 31);
        GRObject grobject = m_view.getSelectedObject(0);
        if(grobject == null)
            setSelectTabIndex((short)38);
        short objtype = grobject.getType();
        if((option & 2) != 0)
            if(objtype == GRObject.eButton || objtype == GRObject.eCheckBox || objtype == GRObject.eDropDown || objtype == GRObject.ePicture || objtype == GRObject.eText)
                insertTabPanel("Options;Set object specific options", new ObjOptionsTab(this));
            else
            if(option != 31)
                throw new CellException((short)37);
        if((option & 4) != 0)
            if(objtype == GRObject.eArc || objtype == GRObject.eOval || objtype == GRObject.ePolygon || objtype == GRObject.eRectangle)
                insertTabPanel("Fill;Format fill colors and patterns", new ObjFillTab(this));
            else
            if(option != 31)
                throw new CellException((short)37);
        if((option & 8) != 0)
            if(objtype == GRObject.eArc || objtype == GRObject.eLine || objtype == GRObject.eOval || objtype == GRObject.ePolygon || objtype == GRObject.eRectangle)
                insertTabPanel("Line Style;Format line colors and styles", new ObjLineStyleTab(this));
            else
            if(option != 31)
                throw new CellException((short)37);
        if((option & 0x10) != 0)
        {
            if(objtype == GRObject.eLine)
            {
                insertTabPanel("Arrows;Format arrow styles", new ObjArrowsTab(this));
                return;
            }
            if(option != 31)
                throw new CellException((short)37);
        }
        pack();
    }

    protected int getSelectedTabIndex()
    {
        return m_nTab;
    }

    protected void setSelectTabIndex(int i)
    {
        m_nTab = i;
    }
}
