package com.jxcell.tools;

import    java.util.EventListener;

public interface ToolbarListener
    extends EventListener
{

    public abstract void toolbarButtonClicked(ToolbarEvent toolbarevent);

    public abstract void toolbarItemChanged(ToolbarEvent toolbarevent);
}
