package com.jxcell.designer;


class LocalDesignerInfo
{

    private short nCurMenuString;
    private short nCurShortcutKey;
    private static final String menuStrings_en[] = {
        "File", "New", "Open...", "Close", "Save", "Save As...", "Read...", "Write...", "Page Setup...", "Print Options",
        "Set Print Area", "Set Print Titles", "Print...", "Exit", "Edit", "Cut", "Copy", "Paste", "Paste Special...", "Copy Cell Format",
        "Polygon Points", "Select All Objects", "Sort...", "Fill", "Down", "Right", "Clear", "All", "Formats", "Contents",
        "Delete...", "Delete Sheet", "Find...", "Replace...", "Goto...", "View", "Toolbars", "Standard", "Formatting", "Drawing and Forms",
        "Formula Bar", "Status Bar", "Insert", "Cells...", "Rows", "Columns", "Worksheet", "Page Break", "Name...", "Drawing Object",
        "Arc", "Line", "Oval", "Polygon", "Rectangle", "Forms Object", "Button", "Checkbox", "Dropdown Listbox", "Picture Object",
        "Cancel Insert Object", "Format", "Cells...", "Row", "Height...", "Hide", "Unhide", "Default Height...", "Column", "Width...",
        "AutoFit Selection", "Hide", "Unhide", "Default Width...", "Sheet", "Properties...", "Enable Protection", "Conditional Format", "Freeze Panes", "Default Font...",
        "Object...", "Bring To Front", "Send To Back", "Tools", "Recalc", "Options...", "Help", "Contents and Index", "About Jxcell..."
    };
    private static final int shortcutKeys[] = {
        78, 79, 83, 80, 88, 67, 86, 68, 82, 70, 
        72, 71, 49
    };
    private static final String designerStrings_en[] = {
        "Workbook Designer", "This will clear the entire workbook.  Do you want to continue?",
        "Do you want to save the changes made to the current workbook?", "Invalid selection.",
        "Are you sure you want to delete all selected objects?", "This feature is not available from an Applet.",
        "Help files are available at http://www.jxcell.net", "Locked cells cannot be modified.",
        "Are you sure you want to delete the selected sheet(s)?"
    };

    private static final String selectionStrings_en[] = {
        "Delete Objects", "Delete...", "Remove Page Break", "Page Break" };

    static String getselectionString(int index)
    {
        return selectionStrings_en[index];
    }

    String getMenuString(short index)
    {
        return menuStrings_en[index];
    }

    void resetMenuStrings()
    {
        nCurMenuString = -1;
    }

    String getNextMenuString()
    {
        return getMenuString(++nCurMenuString);
    }

    int getShortcutKey(short index)
    {
        return shortcutKeys[index];
    }

    void resetShortcutKeys()
    {
        nCurShortcutKey = -1;
    }

    int getNextShortcutKey()
    {
        return getShortcutKey(++nCurShortcutKey);
    }

    static String getString(short index)
    {
        return designerStrings_en[index];
    }

    LocalDesignerInfo()
    {
    }
}
