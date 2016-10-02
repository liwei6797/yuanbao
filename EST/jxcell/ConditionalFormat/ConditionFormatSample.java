import com.jxcell.CellFormat;
import com.jxcell.ConditionFormat;
import com.jxcell.View;

public class ConditionFormatSample
{

    public static void main(String args[])
    {
        View m_view = new View();
        try
        {
            m_view.getLock();
            ConditionFormat condfmt[]=new ConditionFormat[3];
            condfmt[0] = m_view.CreateConditionFormat();
            condfmt[1] = m_view.CreateConditionFormat();
            condfmt[2] = m_view.CreateConditionFormat();

            // Condition #1
            CellFormat cf=condfmt[0].getCellFormat();
            condfmt[0].setType(ConditionFormat.TypeFormula);
            condfmt[0].setFormula1("and(iseven(row()), $D1 > 1000)", 0, 0);
            cf.setFontColor(0x00ff00);
            cf.setPattern((short)1);
            cf.setPatternFG(0x99ccff);
            condfmt[0].setCellFormat(cf);

            // Condition #2
            condfmt[1].setType(ConditionFormat.TypeFormula);
            condfmt[1].setFormula1("iseven($A1)", 0, 0);
            cf.setFontColor(0xffffff);
            condfmt[1].setCellFormat(cf);

            // Condition #3
            condfmt[2].setType(ConditionFormat.TypeCell);
            condfmt[2].setFormula1("500", 0, 0);
            condfmt[2].setOperator(ConditionFormat.OperatorGreaterThan);
            cf=condfmt[2].getCellFormat();
            cf.setFontColor(0xff0000);
            condfmt[2].setCellFormat(cf);

            // Select the range and apply conditional formatting
            m_view.setSelection(0, 0, 39, 3);
            m_view.setConditionalFormats(condfmt);

            m_view.write("./conditionFormats.xls");
            com.jxcell.designer.Designer.newDesigner(m_view);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            m_view.releaseLock();
        }
    }

}