
import com.jxcell.CellFormat;


public class CustomFormat
{
    private CellFormat format;
    private String name;
    private boolean isEditFormat;
    private boolean isErrFormat;

    public CustomFormat()
    {
        isEditFormat = false;
        isErrFormat = false;
    }

    public CustomFormat(CellFormat format1)
    {
        format = format1;
    }

    public boolean Equals(CustomFormat format1)
    {
        if(isInputFormat() && format1.isInputFormat())
            return true;
        else if(isInputFormat() && !format1.isInputFormat())
           return false;
        else if(!isInputFormat() && format1.isInputFormat())
           return false;
        if(isErrFormat() && format1.isErrFormat())
            return true;
        else if(isErrFormat() && !format1.isErrFormat())
           return false;
        else if(!isErrFormat() && format1.isErrFormat())
           return false;
       return Equals(format1.format);
    }

    public boolean Equals(CellFormat format1)
    {
       return format.getFontName().equals(format1.getFontName())
               && format.getFontColor() == format1.getFontColor()
               && format.getFontSize() == format1.getFontSize()
               && format.getPattern() == format1.getPattern()
               && format.getPatternFG() == format1.getPatternFG()
               && format.getLeftBorder() == format1.getLeftBorder()
               && format.getLeftBorderColor() == format1.getLeftBorderColor()
               && format.getTopBorder() == format1.getTopBorder()
               && format.getTopBorderColor() == format1.getTopBorderColor()
               && format.getBottomBorder() == format1.getBottomBorder()
               && format.getBottomBorderColor() == format1.getBottomBorderColor()
               && format.getRightBorder() == format1.getRightBorder()
               && format.getRightBorderColor() == format1.getRightBorderColor()
               && format.getHorizontalAlignment() == format1.getHorizontalAlignment()
               && format.getVerticalAlignment() == format1.getVerticalAlignment();
    }

    public String toString()
    {
        if(isInputFormat())
        {
            return "*." + name + "{ background : #CCFFFF; color : windowtext; font-family : ËÎÌו; \r\n" +
                    "font-size : 10.50pt; font-style : normal; font-weight : 400; \r\n" +
                    "text-align : left; vertical-align : middle }\r\n";
        }
        if(isErrFormat())
        {
            return "*." + name + "{ background : #00FF00; color : #FF0000; font-family : ËÎÌו; \r\n" +
                    "font-size : 10.50pt; font-style : normal; font-weight : 400; \r\n" +
                    "text-align : left; vertical-align : middle }\r\n";
        }
        String formatStr = "";
        String fontstyle = "normal";
        String fontweight = "normal";

        if(format.isFontItalic())
            fontstyle = "italic";
        if(format.isFontBold())
            fontweight = "bold";

        int patterFGColor = format.getPattern() == 0 ? 0xffffff : format.getPatternFG();
        formatStr = "*." + name + " { " + border() +
                " color : #" +  writeColor(format.getFontColor()) + "; background : #" + writeColor(patterFGColor);
        formatStr = formatStr + "; font-family : " + format.getFontName() + "; font-size : " + format.getFontSize()/20D + "pt; \r\n";
        formatStr = formatStr + "font-style : "+ fontstyle + "; font-weight : " + fontweight + "; padding-left : 1px;\r\n";
        formatStr = formatStr + "padding-right : 1px; padding-top : 1px; ";
        formatStr = formatStr + align() + "}\r\n";
       return formatStr;
    }

    private String align()
    {
        short hAlign = format.getHorizontalAlignment();
        short vAlign = format.getVerticalAlignment();
        String align = "";
        switch(hAlign)
        {
            case 1:
                align += "text-align : left; ";
                break;
            case 2:
                align += "text-align : center; ";
                break;
            case 3:
                align += "text-align : right; ";
                break;
            default:
                align += "text-align : justify; ";
        }
        switch(vAlign)
        {
            case 0:
                align += "vertical-align : top ";
                break;
            case 1:
                align += "vertical-align : middle ";
                break;
            case 2:
                align += "vertical-align : bottom ";
                break;
            default:
                align += "vertical-align : baseline ";
        }
        return align;
    }

    private String border()
    {
        short leftborder = format.getLeftBorder();
        int leftborderColor = format.getLeftBorderColor();
        short topborder = format.getTopBorder();
        int topborderColor = format.getTopBorderColor();
        short bottomborder = format.getBottomBorder();
        int bottomborderColor = format.getBottomBorderColor();
        short rightborder = format.getRightBorder();
        int rightborderColor = format.getRightBorderColor();

        String border = "";
        if(leftborder == topborder && bottomborder == rightborder && leftborder == bottomborder && leftborder == 0)
          return "";
        if(leftborder == topborder && bottomborder == rightborder && leftborder == bottomborder
                && leftborderColor == topborderColor && bottomborderColor == rightborderColor && leftborderColor == bottomborderColor)
        {
            return "border : " + getLineWidth(topborder,topborderColor) + ";";
        }
        if(bottomborder != 0)
        {
           border = border + " border-bottom : " + getLineWidth(bottomborder,bottomborderColor) + ";";
        }
        if(rightborder != 0)
        {
           border = border + " border-right : "+ getLineWidth(rightborder,rightborderColor) + ";";
        }
        if(topborder != 0)
        {
           border = border + " border-top : " + getLineWidth(topborder,topborderColor) + ";";
        }
        if(leftborder != 0)
        {
           border = border + " border-left : " + getLineWidth(leftborder,leftborderColor) + ";";
        }
        return border;
    }

    private String getLineWidth(short border,int rgb)
    {
        if(border == 1 )
          return "0.5pt solid #" + writeColor(rgb);
        else if(border == 2 )
          return "1.0pt solid #" + writeColor(rgb);
        else if(border == 3 )
          return "0.5pt dashed #" + writeColor(rgb);
        else if(border == 4 )
          return "0.5pt dotted #" + writeColor(rgb);
        else if(border == 5)
          return "1.5pt solid #" + writeColor(rgb);
        else if(border == 6)
          return "2.0pt double #" + writeColor(rgb);
        else if(border == 8)
          return "1.0pt dashed #" + writeColor(rgb);
        else if(border == 10)
          return "1.0pt dot-dash #" + writeColor(rgb);
        else if(border == 12)
          return "1.0pt dot-dot-dash #" + writeColor(rgb);
        else if(border == 13)
          return "1.0pt dot-dash-slanted #" + writeColor(rgb);
        return String.valueOf(border);
    }

    private String writeColor(int rgb)
    {
        String colorstr = "";
        colorstr += writeHex(rgb >> 16 & 0xff);
        colorstr += writeHex(rgb >> 8 & 0xff);
        colorstr += writeHex(rgb & 0xff);
        return colorstr;
    }

    private String writeHex(int i)
    {
        String hexStr = "";
        hexStr += hexChar(i);
        while((i >>>= 4) != 0)
            hexStr = hexChar(i) + hexStr;
        if(hexStr.length() == 1)
            hexStr = "0" + hexStr;
        return hexStr;
    }

    private char hexChar(int k)
    {
        k %= 16;
        if(k < 0)
            k = -k;
        if(k < 10)
            return (char)(48 + k);
        else
            return (char)(65 + (k - 10));
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isInputFormat()
    {
        return isEditFormat;
    }

    public void setInputFormat()
    {
        isEditFormat = true;
    }

    public boolean isErrFormat()
    {
        return isErrFormat;
    }

    public void setErrFormat(boolean errFormat)
    {
        isErrFormat = errFormat;
    }
}
