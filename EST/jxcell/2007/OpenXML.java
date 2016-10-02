import com.jxcell.*;
import com.jxcell.designer.Designer;

import java.io.IOException;

public class OpenXML
{
    public static void main(String args[])
    {
        View m_view;
        try
        {
            m_view =new View();
            m_view.readXLSX(".\\format.xlsx");
            m_view.writeXLSX(".\\out.xlsx");
            Designer.newDesigner(m_view);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    }
}
