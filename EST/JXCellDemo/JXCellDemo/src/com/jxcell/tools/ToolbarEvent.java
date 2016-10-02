package com.jxcell.tools;

import java.util.EventObject;

public class ToolbarEvent extends EventObject
{

    private short buttonindex;
    private short controlindex;
    private String itemvalue;
    private boolean sticky;

    ToolbarEvent(Toolbar toolbar, short index, boolean sticky)
    {
        super(toolbar);
        buttonindex = index;
        this.sticky = sticky;
    }

    ToolbarEvent(Toolbar toolbar, short index, String value)
    {
        super(toolbar);
        controlindex = index;
        itemvalue = value;
    }

    public short getButtonIndex()
    {
        return buttonindex;
    }

    public short getControlIndex()
    {
        return controlindex;
    }

    public String getItemValue()
    {
        return itemvalue;
    }

    public boolean isButtonSticky()
    {
        return sticky;
    }
}
