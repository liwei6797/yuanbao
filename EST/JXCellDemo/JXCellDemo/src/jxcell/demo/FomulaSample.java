package jxcell.demo;

import com.jxcell.CellException;
import com.jxcell.View;

import java.io.IOException;

public class FomulaSample
{
    public FomulaSample()
    {}

    public static void main(String args[])
    {
        View m_view = new View();
        int rowIndex = 0;

        try
        {
            //Sets the number of worksheets in this workbook
            m_view.setNumSheets(2);
            // set sheet names
            m_view.setSheetName(0,"sheet1");
            m_view.setSheetName(1,"sheet2");
            // select the first sheet
            m_view.setSheet(0);

            //set column width,units equal to 1/256th of the character 0's width in the default font
            m_view.setColWidth(0,35*256);
            m_view.setColWidth(1,15*256);
            m_view.setColWidth(2,15*256);

            m_view.setTextAsValue(rowIndex++, 0, "Examples of typical formulas usage:");
            m_view.setTextAsValue(++rowIndex, 0, "Some data:");

            m_view.setTextAsValue(rowIndex, 1, "3");   // enter number as text
            m_view.setNumber(rowIndex, 2, 4.1);
            m_view.setTextAsValue(++rowIndex, 1, "5.2");
            m_view.setNumber(rowIndex, 2, 6);
            m_view.setTextAsValue(++rowIndex, 1, "7");
            m_view.setNumber(rowIndex++, 2, 8.3);

            // Named ranges.
            String namedRange = "Range1";
            m_view.setDefinedName(namedRange, "$B$3:$C$4" );

            // Floats without first digit.
            m_view.setText( ++rowIndex, 0 , "Float number without first digit:");
            //Sets the formula,The formula string should not have a leading equal sign (=)
            m_view.setFormula(rowIndex, 1 , ".5/23+.1-2");

            // Function using named range.
            m_view.setText(++rowIndex, 0, "Named range:");
            m_view.setFormula(rowIndex, 1, "SUM(" + namedRange + ")");

            // 3D sheet references.
            m_view.setText(++rowIndex, 0, "3d sheet reference:");
            m_view.setFormula(rowIndex, 1, "sheet2!$C$2");

            // 3D area sheet references.
            m_view.setText(++rowIndex, 0, "3d area sheet reference:");
            m_view.setFormula(rowIndex, 1, "AVERAGE(sheet2!A2:C2)");

            // Function's miss argument.
            m_view.setText(++rowIndex, 0, "Function's miss arguments:");
            m_view.setFormula(rowIndex, 1, "Count(1,  ,  ,,,2, 23,,,,,, 34,,,54,,,,  ,)");

            // Functions are case-insensitive.
            m_view.setText(++rowIndex, 0, "Functions are case-insensitive:");
            m_view.setFormula(rowIndex, 1, "cOs( 1 )");

            // Functions.
            m_view.setText(++rowIndex, 0, "Supported functions:");

            String nextFunction = null;
            m_view.setText(++rowIndex, 0, "Results");
            m_view.setText(rowIndex++, 1, "Formulas");

            nextFunction = "NOW()+123";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "SECOND(12)/23";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "MINUTE(24)-1343/35";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "(HOUR(56)-23/35)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "WEEKDAY(5)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "YEAR(23)-WEEKDAY(5)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "MONTH(3)-2342/235345";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "((DAY(1)))";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "TIME(1,2,3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "DATE(1,2,3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "RAND()";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "TEXT(\"text\", \"$d\")";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "VAR(1,2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "MOD(1,2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "NOT(FALSE)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "OR(FALSE)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "AND(TRUE)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "FALSE()";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "TRUE()";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "VALUE(3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "LEN(\"hello\")";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "MID(\"hello\",1,1)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "ROUND(1,2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "SIGN(-2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "INT(3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "ABS(-3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "LN(2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "EXP(4)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "SQRT(2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "PI()";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "COS(4)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "SIN(3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "MAX(1,2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "MIN(1,2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "AVERAGE(1,2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "SUM(1,3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "IF(1,2,3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "COUNT(1,2,3)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            nextFunction = "SUBTOTAL(1,sheet2!A2:C2)";
            m_view.setFormula(rowIndex, 0, nextFunction);
            m_view.setText(rowIndex++, 1, nextFunction);

            // Paranthless checks.
            m_view.setText(++rowIndex, 0, "Paranthless:");
            m_view.setFormula(rowIndex, 1, "((12+2343+34545))");

            // Unary operators.
            m_view.setText(++rowIndex, 0, "Unary operators:");
            m_view.setFormula(rowIndex, 1, "B5%");
            m_view.setFormula(rowIndex, 2, "+++B5");

            // Operand tokens, bool.
            m_view.setText(++rowIndex, 0, "Bool values:");
            m_view.setFormula(rowIndex, 1, "TRUE");
            m_view.setFormula(rowIndex, 2, "FALSE");

            // Operand tokens, int.
            m_view.setText(++rowIndex, 0, "Integer values:");
            m_view.setFormula(rowIndex, 1, "1");
            m_view.setFormula(rowIndex, 2, "20");

            // Operand tokens, num.
            m_view.setText(++rowIndex, 0, "Float values:");
            m_view.setFormula(rowIndex, 1, ".4");
            m_view.setFormula(rowIndex, 2, "2235.5132");

            // Operand tokens, str.
            m_view.setText(++rowIndex, 0, "String values:");
            m_view.setFormula(rowIndex, 1, "\"hello world!\"");

            // Operand tokens, error.
            m_view.setText(++rowIndex, 0, "Error values:");
            m_view.setFormula(rowIndex, 1, "#NULL!");
            m_view.setFormula(rowIndex, 2, "#DIV/0!");

            // Binary operators.
            m_view.setText(++rowIndex, 0, "Binary operators:");
            m_view.setFormula(rowIndex, 1, "(1)-(2)+(3/2+34)/2+12232-32-4");

            // Another sheet.
            m_view.setSheet(1);
            rowIndex = 0;
            m_view.setText(rowIndex++, 0, "Some data on another sheet:");
            m_view.setNumber(rowIndex, 0, 33);
            m_view.setNumber(rowIndex, 1, 44.1);
            m_view.setNumber(rowIndex, 2, 55.2);
            m_view.setNumber(++rowIndex, 0, 66);
            m_view.setNumber(rowIndex, 1, 77);
            m_view.setNumber(rowIndex, 2, 88.3);

            m_view.write(".\\FormulaSample.xls");
//            Designer.newDesigner(m_view);
        }
        catch (CellException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
