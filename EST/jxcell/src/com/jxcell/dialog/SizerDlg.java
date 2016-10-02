package com.jxcell.dialog;

import com.jxcell.View;

import java.text.NumberFormat;


abstract class SizerDlg extends Dialog
{

    private double currentsize;
    private int currentunit;
    static boolean ISMETRIC;

    SizerDlg(View view, String title, boolean model)
    {
        super(view, title, model);
    }

    String getUnitStr()
    {
        double unit = getUnit(currentunit);
        return NumberFormat.getInstance().format(unit);
    }

    int getcurrentunit()
    {
        return currentunit;
    }

    private double getUnit(int unit)
    {
        return convertValue(currentsize, (short)currentunit, (short)unit);
    }

    public double convertValue(double number, short inputUint, short dstUnit)
    {
        if(inputUint == dstUnit)
            return round(number);
        switch(dstUnit)
        {
        default:
            break;

        case 1:
            switch(inputUint)
            {
            case 2:
                return round(number * 2.54D);

            case 3:
                return round((number / 1440D) * 2.54D);

            case 4:
                return round((number / 72D) * 2.54D);
            }
            break;

        case 2:
            switch(inputUint)
            {
            case 1:
                return round(number / 2.54D);

            case 3:
                return round(number / 1440D);

            case 4:
                return round(number / 72D);
            }
            break;

        case 3:
            switch(inputUint)
            {
            case 1:
                return round((number / 2.54D) * 1440D + 0.5D);

            case 2:
                return round(number * 1440D + 0.5D);

            case 4:
                return round(number * 20D + 0.5D);
            }
            break;

        case 4:
            switch(inputUint)
            {
            case 1:
                return round((number / 2.54D) * 72D);

            case 2:
                return round(number * 72D);

            case 3:
                return round(number / 20D);
            }
            break;
        }
        return round(number);
    }

    private double round(double d)
    {
        return d;
    }

    void setUnitAndSize(double size, int unit)
    {
        currentsize = size;
        currentunit = unit;
    }

    void setUnitAndSize(String size, int unit)
        throws Exception
    {
        try
        {
            setUnitAndSize((new Double(size)).doubleValue(), unit);
        }
        catch(Throwable _ex)
        {
            throw new Exception("You have entered an invalid value.");
        }
    }

    double getUnitAndSize1(int unit)
    {
        double size = getUnit(unit);
        if(unit == 0)
            return size * 256D + 0.5D;
        else
            return getUnitAndSize(unit);
    }

    double getUnitAndSize(int unit)
    {
        double size = getUnit(unit);
        if(unit == 1)
        {
            ISMETRIC = true;
            return (size / 2.54D) * 1440D + 0.5D;
        }
        if(unit == 2)
        {
            ISMETRIC = false;
            return size * 1440D + 0.5D;
        } else
        {
            return 0.0D;
        }
    }

    String getUnitStr(int unit)
    {
        double size = getUnit(unit);
        return NumberFormat.getInstance().format(size);
    }

    static
    {
        ISMETRIC = true;
    }
}
