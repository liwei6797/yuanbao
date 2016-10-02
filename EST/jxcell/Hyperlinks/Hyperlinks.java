import com.jxcell.*;

import java.io.IOException;
import com.jxcell.designer.Designer;

public class Hyperlinks
{
    public Hyperlinks()
    {
        View m_view = new View();
         try
         {
             //Creating Hyperlink to another cell
             m_view.addHyperlink(1,1,1,1,"C3", HyperLink.kRange,"This is Workbook Link!");

             //Creating Hyperlink for Website
             m_view.addHyperlink(2,1,2,1,"http://www.jxcell.net", HyperLink.kURLAbs,"This is Web Url Link!");

             //Creating Hyperlink for e-mail
             m_view.addHyperlink(3,1,3,1,"mailto:support@jxcell.com", HyperLink.kURLAbs,"Send Mail!");

             //Creating Hyperlink for Opening Files
             m_view.addHyperlink(4,1,4,1,"c:\\", HyperLink.kURLAbs,"This is File Link!");

             m_view.write(".\\hyperlink.xls");
             Designer.newDesigner(m_view);
      }
         catch (CellException e)
         {
             e.printStackTrace();
         } catch (IOException e)
         {
             e.printStackTrace();
         }
    }

    public static void main(String args[])
    {
        new Hyperlinks();
    }
}