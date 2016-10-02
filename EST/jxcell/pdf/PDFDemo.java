
import com.jxcell.CellException;
import com.jxcell.View;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

public class PDFDemo
{

    public static void main(String args[])
    {
        try
        {
            View view = new View();

            view.readXLSX(".\\report.xlsx");
            view.setPrintGridLines(false);
            view.setPrintScaleFitToPage(true);
            //view.write(".\\out.xls");
            view.exportPDF(".\\out.pdf");
            com.jxcell.designer.Designer designer = com.jxcell.designer.Designer.newDesigner(view);
//            designer.getView().setViewScale(57);
            designer.getView().setShowGridLines(false);
            designer.getView().setShowRowHeading(false);
            designer.getView().setShowColHeading(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}