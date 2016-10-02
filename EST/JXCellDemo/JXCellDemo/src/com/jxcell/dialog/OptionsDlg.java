package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;


public class OptionsDlg extends TabDialog
{

    public static final int kAllPages = 15;
    public static final int kAutoFillPage = 4;
    public static final int kCalculationPage = 2;
    public static final int kColorPalettePage = 8;
    public static final int kGeneralPage = 1;
    private static int index;

    public OptionsDlg(View view)
        throws CellException
    {
        this(view, 15);
    }

    public OptionsDlg(View view, int option)
        throws CellException
    {
        super(view, "Options", true);
        checkOptions(1, option, 15);
        if((option & 1) != 0)
            insertTabPanel("General;General options and settings", new GeneralTab(this));
        if((option & 2) != 0)
            insertTabPanel("Calculation;Set calculation options", new CalculationTab(this));
        if((option & 4) != 0)
            insertTabPanel("Auto Fill;Set auto fill lists", new AutoFillTab(this));
        if((option & 8) != 0)
            insertTabPanel("Color Palette;Change the color palette", new ColorPaletteTab(this));
    }

    protected int getSelectedTabIndex()
    {
        return index;
    }

    protected void setSelectTabIndex(int i)
    {
        index = i;
    }
}
