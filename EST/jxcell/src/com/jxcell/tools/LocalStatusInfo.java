package com.jxcell.tools;


public class LocalStatusInfo
{

    private static final String statusStrings_en[] = {
//        "Create a new workbook", "Open an existing workbook", "Save the active workbook", "Print the active workbook", "Cut the selection and put it on the Clipboard", "Copy the selection and put it on the Clipboard", "Insert Clipboard contents", "Copy format from one selection to another", "Show or hide the drawing toolbar", "Draw arc",
        "Create a new workbook", "Open an existing workbook", "Save the active workbook", "Print the active workbook", "Cut the selection and put it on the Clipboard", "Copy the selection and put it on the Clipboard", "Insert Clipboard contents", "Copy format from one selection to another", "Show or hide the drawing toolbar", "undo", "redo",
//        "Draw line", "Draw oval", "Draw polygon", "Draw rectangle", "Draw chart", "Draw picture", "Edit polygon points", "Edit polygon points", "Set to bold font", "Set to italic font",
        "Draw line", "Draw oval", "Draw rectangle", "Draw chart", "Draw picture", "Edit polygon points", "Edit polygon points", "Set to bold font", "Set to italic font",
//        "Underline Text", "Select new text color", "Align to left", "Align to center", "Align to right", "Center text across multiple cells", "Select from fixed and general formats", "Select from currency formats", "Select from percent formats", "Select from fraction formats",
        "Underline Text", "Select new text color", "Align to left", "Align to center", "Align to right", "Merge cells", "Select from fixed and general formats", "Select from currency formats", "Select from percent formats", "Select from fraction formats",
        "Select from date and time formats", "Select new fill color", "Ready", "Change the font for the current selection", "Change the font size for the current selection","Set Zoom Scale"
    };
    private static final String popupStrings_en[] = {
        "Font Color", "Formats"
    };

    public static String getStatusString(int index)
    {
        return statusStrings_en[index];
    }

    public static String getPopupString(int index)
    {
        return popupStrings_en[index];
    }

    public LocalStatusInfo()
    {
    }

}
