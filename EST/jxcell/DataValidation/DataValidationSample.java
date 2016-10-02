import com.jxcell.DataValidation;
import com.jxcell.View;

public class DataValidationSample
{

    public static void main(String args[])
    {
        View m_view = new View();
        try
        {
            m_view.setText(0, 1, "Apple");
            m_view.setText(0, 2, "Orange");
            m_view.setText(0, 3, "Banana");

            DataValidation dataValidation = m_view.CreateDataValidation();
            dataValidation.setType(DataValidation.eUser);
            dataValidation.setFormula1("\"dddd\0gggg\0hhh\"");
            m_view.setSelection("A1:A5");
            m_view.setDataValidation(dataValidation);

            dataValidation = m_view.CreateDataValidation();
            dataValidation.setType(DataValidation.eUser);
            dataValidation.setFormula1("$B$1:$D$1");
            m_view.setSelection("B1:D5");
            m_view.setDataValidation(dataValidation);

            m_view.write(".\\datavalidation.xls");
            com.jxcell.designer.Designer.newDesigner(m_view);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}