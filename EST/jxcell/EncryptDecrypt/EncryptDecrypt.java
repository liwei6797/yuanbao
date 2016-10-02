import com.jxcell.View;
import com.jxcell.CellException;

import java.io.IOException;

public class EncryptDecrypt
{
    public EncryptDecrypt()
    {
        encrypt();
        decrypt();
    }

    public void encrypt()
    {
        View m_view = new View();
        try
        {
            m_view.setTextAsValue(1,2,"Jan");
            m_view.setTextAsValue(1,3,"Feb");
            m_view.setTextAsValue(1,4,"Mar");
            m_view.setTextAsValue(1,5,"Apr");
            m_view.setTextAsValue(2,1,"Bananas");
            m_view.setTextAsValue(3,1,"Papaya");
            m_view.setTextAsValue(4,1,"Mango");
            m_view.setTextAsValue(5,1,"Lilikoi");
            m_view.setTextAsValue(6,1,"Comfrey");
            m_view.setTextAsValue(7,1,"Total");
            for(int col = 2; col <= 5; col++)
                for(int row = 2; row <= 7; row++)
                    m_view.setFormula(row, col, "RAND()");
            m_view.setFormula(7, 2, "SUM(C3:C7)");
            m_view.setSelection("C8:F8");
            m_view.editCopyRight();

            //set the workbook open password
            m_view.write(".\\encrypt.xls", "hi");
        }
        catch (CellException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void decrypt()
    {
        View m_view = new View();
        try
        {
            //read the encrypted excel file
            m_view.read(".\\encrypt.xls", "hi");

            //write without password protected
            m_view.write(".\\decrypt.xls");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[])
    {
        new EncryptDecrypt();
    }
}
