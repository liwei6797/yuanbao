import com.jxcell.View;
import com.jxcell.CellException;

import java.io.IOException;

public class PdfPrint
{

    public static void main(String args[])
    {
       View m_view = new View();
        try
        {
            m_view.read(".\\book.csv");
            m_view.setPrintScale(100);     // set print scale value --- default is 100%
            m_view.setPrintHeader("Your header");  //set header --- default is &A
            m_view.setPrintFooter("Your footer");  //set footer  --- default is Page &P
            m_view.setPrintGridLines(true);  //show grid line
//            m_view.filePrint(false);     // print without print dialog
            m_view.exportPDF(".\\out.pdf");   //export to pdf file
        }
        catch (CellException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
