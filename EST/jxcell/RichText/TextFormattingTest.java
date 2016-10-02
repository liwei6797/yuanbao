import com.jxcell.CellFormat;
import com.jxcell.View;
import com.jxcell.designer.Designer;

import java.awt.*;

public class TextFormattingTest
{

    public static void main(String args[])
    {
        View m_view = new View();
        try
        {
            //set data
            String text = "Hello, you are welcome!";
            m_view.setText(0,0,text);

            m_view.setColWidth(0, 36*256);
            m_view.setRowHeight(0, 120*20);

            //text orientation
            CellFormat rangeStyle = m_view.getCellFormat();
            rangeStyle.setOrientation((short)45);
            m_view.setCellFormat(rangeStyle);

            //multi text selection format
            m_view.setTextSelection(0, 6);
            CellFormat cfmt = m_view.getCellFormat();
            cfmt.setFontItalic(true);
            cfmt.setFontColor(Color.BLUE.getRGB());
            m_view.setCellFormat(cfmt);

            m_view.setTextSelection(7, 10);
            cfmt = m_view.getCellFormat();
            cfmt.setFontBold(true);
            cfmt.setFontSize(16);
            m_view.setCellFormat(cfmt);

            m_view.setTextSelection(11, 13);
            cfmt = m_view.getCellFormat();
            cfmt.setFontUnderline(CellFormat.UnderlineSingle);
            cfmt.setFontColor(Color.GREEN.getRGB());
            m_view.setCellFormat(cfmt);

            m_view.setTextSelection(14, text.length()-1);
            cfmt = m_view.getCellFormat();
            cfmt.setFontSize(14);
            m_view.setCellFormat(cfmt);

            m_view.setSelection(0, 5, 65535, 5);
            cfmt = m_view.getCellFormat();
            cfmt.setMergeCells(true);
            m_view.setCellFormat(cfmt);

            m_view.write("./TextFormatting.xls");

            Designer.newDesigner(m_view);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
