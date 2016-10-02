
import com.jxcell.*;
import com.jxcell.RangeRef;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class SpreadsheetConverter
{
    class GRObj
    {
        int row;
        int col;
        int dx;
        int dy;
        int width;
        int height;
        String filename;
        GRObj(int row, int col, int dx, int dy, int width, int height, String filename)
        {
            this.row = row;
            this.col = col;
            this.dx = dx;
            this.dy = dy;
            this.width = width;
            this.height = height;
            this.filename = filename;
        }
    }

    private View m_workbook;
    private int tableWidth;
    private HashMap grobjs = new HashMap();
    private ArrayList namelist = new ArrayList();
    int imagecount = 0;
    int chartcount = 0;

    public SpreadsheetConverter()
    {
    }

    public void write(View View, int sheetIndex, RangeRef rangeArea, Writer writer, String basePath)
        throws Exception
    {
        m_workbook = View;
        m_workbook.recalc();
        writer.write("<html>\r\n");
        writer.write("<head>");
        writer.write("<meta name=\"GENERATOR\" content=\"jxcell live spreadsheet\" />\r\n");
        writer.write("<title>");
        writer.write(View.getSheetName(0));
        writer.write("</title>\r\n");
        writer.write("<style type=\"text/css\"> /* Styles needed by jxcell live spreadsheet */\r\n");
        String tableContent = writeHeaderStyles(View, sheetIndex, rangeArea, writer, basePath);
        writer.write("</style>\r\n");
        writer.write("</head>\r\n");
        writer.write("<body>\r\n");
        writer.write("   <input type=\"hidden\" id=\"sheetName\" name=\"sheetName\" value=\"" + View.getSheetName(View.getSheet()) + "\" />\r\n");
        String ver = "2.0";
        writer.write("   <input type=\"hidden\" id=\"version\" name=\"version\" value=\"" + ver + "\" />\r\n");
        writer.write("<div id=\"panel1\" style='display:block'>\r\n");
        //write table
        writer.write("<table style='border-collapse:collapse;table-layout:fixed;width:" + String.valueOf(tableWidth) + "' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFFFFF\" >\r\n");
        writer.write(tableContent);
        writer.write("</table>\r\n");
        writer.write("</div>\r\n");
        writer.write("</body>\r\n");
        writer.write("</html>\r\n");
        writer.flush();
    }

    private int getRound(double dSource)
    {
        int iRound;
        BigDecimal deSource = new BigDecimal(dSource);
        iRound= deSource.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
        return iRound;
    }

    private void writeShape(View View, ShapeObj shape, String basePath)
            throws Exception
    {
        ShapePos pos = shape.getPos();
        System.out.println("x1:" + pos.getX1() + " x2:" + pos.getX2());
        System.out.println("y1:" + pos.getY1() + " y2:" + pos.getY2());
        int row1 = (int) pos.getY1();
        int col1 = (int) pos.getX1();
        int row2 = (int) pos.getY2();
        int col2 = (int) pos.getX2();
        double dx = 0;
        double dy = 0;

        double objWidth = 0;
        int colWidth = 0;
        for(int col = col1; col < col2; col++)
        {
            colWidth = View.getColWidth(row1);
            objWidth += colWidth*7D/256D;
        }
        colWidth = View.getColWidth(col1);
        objWidth -= (dx = colWidth*7D/256D*(pos.getX1()-col1));
        colWidth = View.getColWidth(col2);
        objWidth += colWidth*7D/256D*(pos.getX2()-col2);

        double objHeight = 0;
        int rowHeight = 0;
        for(int row = row1; row < row2; row++)
        {
            rowHeight = View.getRowHeight(row1);
            objHeight += rowHeight/15D;
        }
        rowHeight = View.getRowHeight(row1);
        objHeight -= (dy = rowHeight/15D*(pos.getY1()-row1));
        rowHeight = View.getRowHeight(row2);
        objHeight += rowHeight/15D*(pos.getY2()-row2);
        System.out.println("objWidth:" + objWidth + " objHeight:" + objHeight);
        String filename = "";
        if(shape instanceof PictureShape)
        {
            PictureShape pic = (PictureShape) shape;
            String ext = ".png";
            int type = pic.getPictureType();
            if(type == -1)
                ext = ".gif";
            else if(type == 5)
                ext = ".jpg";
            else if(type == 6)
                ext = ".png";
            else if(type == 7)
                ext = ".bmp";

            byte[] data = pic.getPictureData();
            filename = "pic" + imagecount++ + ext;
            String path = basePath + java.io.File.separator + filename;
            java.io.FileOutputStream picStream = new java.io.FileOutputStream(path);
            picStream.write(data);
            picStream.close();;
        }
        if(shape instanceof ChartShape)
        {
            ChartShape chart = (ChartShape) shape;
            filename = "chart" + chartcount++ + ".png";;
            try
            {
                java.io.FileOutputStream fo = new java.io.FileOutputStream(basePath + java.io.File.separator + filename);
                chart.writeChartAsPNG(View, fo);
                fo.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        String name = String.valueOf(row1)+String.valueOf(col1);
        grobjs.put(name, new GRObj(row1,col1,(int)dx,(int)dy,(int)objWidth,(int)objHeight,filename));
        namelist.add(name);
    }

    private String writeHeaderStyles(View view, int sheetIndex, RangeRef rangeArea, Writer writer0, String basePath)
            throws Exception
    {
        m_workbook.setSheet(sheetIndex);
        int pics = view.getPictureCount();
        for(int i=0; i<pics; i++)
        {
            PictureShape pic = view.getPictureShape(i);
            writeShape(view, pic, basePath);
        }
        int charts = view.getChartCount();
        for(int i=0; i< charts; i++)
        {
            ChartShape chart = view.getChart(i);
            writeShape(view, chart, basePath);
        }
        int row1 = rangeArea.getRow1();
        int col1 = rangeArea.getCol1();
        int row2 = rangeArea.getRow2();
        int col2 = rangeArea.getCol2();
        StringWriter writer = new StringWriter();
        int formatIndex = 0;
        CustomFormats customFormats = new CustomFormats();
        for(int colnum = col1; colnum <= col2; colnum++)
        {
            view.setActiveCell(row1, colnum);
            view.setSelection(row1, colnum, row1, colnum);
            int colWidth = view.getColWidth(colnum);
            writer.write("<col width=\"");
            tableWidth += getRound(colWidth*7D/256D);
            writer.write(String.valueOf(getRound(colWidth*7D/256D)));        //column width
            writer.write("\" />\r\n");
        }
        for(int row = row1; row <= row2; row++)
        {
            writer.write("<tr style='height:");
            writer.write(String.valueOf(view.getRowHeight(row)/20));    //row height
            writer.write("pt'>");
            for(int colnum = col1; colnum <= col2; colnum++)
            {
                view.setActiveCell(row, colnum);
                view.setSelection(row, colnum, row, colnum);
                RangeRef selectRange = view.getSelection();
                int rowspan = 0;
                int colspan = 0;
                if(selectRange.getRow1() != selectRange.getRow2())
                {
                    rowspan = selectRange.getRow2() - selectRange.getRow1() + 1;
                }
                if(selectRange.getCol1() != selectRange.getCol2())
                {
                    colspan = selectRange.getCol2() - selectRange.getCol1() + 1;
                }
                if(selectRange.getRow1() != row || selectRange.getCol1() != colnum)
                    continue;
                writer.write("<td ");
                writer.write(" class='");
                CellFormat rangeStyle = view.getCellFormat();
                CustomFormat customFormat = new CustomFormat(rangeStyle);
                int index;
                if( (index = customFormats.indexOf(customFormat)) != -1)
                {
                    customFormat = customFormats.getCustomFormat(index);
                }
                else
                {
                    customFormat.setName("xx" + (formatIndex++));
                    customFormats.addCustomFormat(customFormat);
                }
                writer.write(customFormat.getName());
                writer.write("'");
                if(rowspan > 0)
                {
                    writer.write(" rowspan=\"");
                    writer.write(String.valueOf(rowspan));
                    writer.write("\"");
                }
                if(colspan > 0)
                {
                    writer.write(" colspan=\"");
                    writer.write(String.valueOf(colspan));
                    writer.write("\"");
                }
                writer.write(" >\r\n");

                String name = String.valueOf(row)+String.valueOf(colnum);
                if(namelist.indexOf(name) != -1)
                {
                    GRObj grobj = (GRObj)grobjs.get(name);
                    writer.write(" <span style='position:absolute;z-index:1;");
                    writer.write("margin-left:" + grobj.dx +
                            "px;margin-top:" + grobj.dy +
                            "pt;width:" + grobj.width +
                            "px;height:" + grobj.height +
                            "px'>\r\n");
                    writer.write("<img width=" +
                            grobj.width +
                            " height=" +
                            grobj.height +
                            "  src=\"./" + grobj.filename + "\">\r\n");
                    writer.write(" </span>");
                    namelist.remove(name);
                }
                com.jxcell.HyperLink linkref = view.getHyperlink(row, colnum);
                if(linkref != null)
                {
                    String url = linkref.getLinkString();
                    writer.write("<a href=\"" + url + "\">" + encodeHTML(view.getFormattedText(row, colnum)) + "</a>");
                }
                else
                    writer.write(encodeHTML(view.getFormattedText(row, colnum)));

                writer.write("</td>\r\n");
            }
            writer.write("</tr>\r\n");
        }
        writer0.write(customFormats.toString());
        return writer.toString();
    }

    public String formatColNr(int col)
    {
        String colStr = "";
        colStr += ((char)(65 + col % 26));
        while((col = col / 26 - 1) >= 0)
            colStr = (char)(65 + col % 26) + colStr;
        return colStr;
    }

    public static String encodeHTML(String text)
    {
        StringBuffer stringbuffer = new StringBuffer();
        boolean isSlash = false;
        char tmpChar = ' ';
        if(text == null)
            return "";
        for(int i = 0; i < text.length(); i++)
        {
            char m_char = text.charAt(i);
            if(!isSlash && m_char == '\\')
            {
                isSlash = true;
            } else
            {
                if(isSlash)
                {
                    stringbuffer.append(m_char);
                    isSlash = false;
                } else
                {
                    switch(m_char)
                    {
                    case 60:
                        stringbuffer.append("&lt;");
                        break;

                    case 62:
                        stringbuffer.append("&gt;");
                        break;

                    case 32:
                        if(tmpChar == ' ')
                            stringbuffer.append("&nbsp;");
                        else
                            stringbuffer.append(' ');
                        break;

                    case 10:
                        stringbuffer.append("<br>");
                        if(i + 1 < text.length() && text.charAt(i + 1) == '\r')
                            i++;
                        break;

                    case 13:
                        stringbuffer.append("<br>");
                        if(i + 1 < text.length() && text.charAt(i + 1) == '\n')
                            i++;
                        break;

                    case 9:
                        stringbuffer.append("&nbsp;");

                    default:
                        stringbuffer.append(m_char);
                        break;
                    }
                }
                tmpChar = m_char;
            }
        }
        return stringbuffer.toString();
    }

    public static void main(String args[])
    {
        SpreadsheetConverter htmlWriter = new SpreadsheetConverter();

        View  m_view = new View();
        try
        {
            m_view.read(".\\1.xls");

            System.out.println("getLastRow:" + m_view.getLastRow());
            System.out.println("getLastCol:" + m_view.getLastCol());
            m_view.setSelection(0,0,m_view.getLastRow(),m_view.getLastCol());

            String currentDir = System.getProperty("user.dir");

            String fileSeparator = System.getProperty("file.separator");

            String filename = currentDir + fileSeparator + "htmlout.html";

            StringWriter writer = new StringWriter();

            // Write to HTML format using the selected range on all sheets

            int i = m_view.getNumSheets();
            RangeRef range = m_view.getSelection(0);
            htmlWriter.write(m_view, 0, range, writer, ".");
            writer.close();

            java.io.FileWriter filewriter = new java.io.FileWriter(filename);
            filewriter.write(writer.toString());
            filewriter.close();
        }
            catch (CellException e1) { }
            catch (Exception e2){ }
    }
}
