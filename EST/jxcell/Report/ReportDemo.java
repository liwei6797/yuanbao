
import com.jxcell.CellException;
import com.jxcell.View;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

public class ReportDemo
{

    public static void main(String args[])
    {
        ReportDemo reportDemo = new ReportDemo();
        try
        {
            reportDemo.generate();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    String[] regions = {"East", "South", "West", "North"};
    String[][] names = {
            {"Nancy", "Robert", "Steven", "Janet", "Andrew", "Jones", "Thomas"},
            {"Michael", "Laura", "Erickson"},
            {"Anne", "Jordan", "Margaret", "Jackson"},
            {"Davolio", "Fuller"}
    };
    private Random r = new Random();

    public ReportDemo()
    {
    }

    public void generate()
            throws IOException
    {
		View m_view = null;

		try
        {
			// get and instance of the View
			m_view = new View();
			// Initialize the workbook to a default empty state
			m_view.initWorkbook();

			//Modify the Excel color palette to contain 3 non-standard colors.
			//Up to 2003 version, Excel is limited to a 56 color palette.

            m_view.setPaletteEntry(31, new Color(242, 242, 242));
			m_view.setPaletteEntry(32, new Color(216, 216, 216));
			m_view.setPaletteEntry(33, new Color(191, 191, 191));

			//set column width in twips.  Twips allow more consistent sizing between printer output and screen display
			m_view.setColWidth(0,5038);
			for(int x=1; x< 17; x++)
            {
				if(x%4==0){
					m_view.setColWidth(x, 3290);
				}else{
					m_view.setColWidth(x, 2852);
				}
			}
			m_view.setColWidth(17, 3290);

            //Create the columnar define names
            m_view.setDefinedName("Jan", "Sheet1!$B1");
            m_view.setDefinedName("Feb", "Sheet1!$C1");
            m_view.setDefinedName("Mar", "Sheet1!$D1");
            m_view.setDefinedName("qOne", "Sheet1!$E1");
            m_view.setDefinedName("Apr", "Sheet1!$F1");
            m_view.setDefinedName("May", "Sheet1!$G1");
            m_view.setDefinedName("Jun", "Sheet1!$H1");
            m_view.setDefinedName("qTwo", "Sheet1!$I1");
            m_view.setDefinedName("Jul", "Sheet1!$J1");
            m_view.setDefinedName("Aug", "Sheet1!$K1");
            m_view.setDefinedName("Sep", "Sheet1!$L1");
            m_view.setDefinedName("qThree", "Sheet1!$M1");
            m_view.setDefinedName("Oct", "Sheet1!$N1");
            m_view.setDefinedName("Nov", "Sheet1!$O1");
            m_view.setDefinedName("Dec", "Sheet1!$P1");
            m_view.setDefinedName("qFour", "Sheet1!$Q1");
            m_view.setDefinedName("Grand", "Sheet1!$R1");

            // creates the header row across the top of the worksheet
            String[] months = {"Jan", "Feb", "Mar", "Q1", "Apr", "May", "Jun", "Q2", "Jul", "Aug", "Sep", "Q3", "Oct", "Nov", "Dec", "Q4", "Total"};
            for(int i=0; i < months.length; i++)
            {
                m_view.setText(0, i + 1, months[i]);
            }
            formatCells(m_view, true, false, true, false, false, 3, 0, 0, 0, 16);

            //sets the workbook to know that detail data is above the summary data for outlines
            m_view.setSummaryRowsBeforeDetail(false);

            int currentRow = 1;

            for(int i=0; i<regions.length; i++)
                currentRow = insertRegion(m_view, currentRow, regions[i], names[i]);

            m_view.setText(currentRow, 0, "Grand Total");
            String formula = "sum(East, North, South, West)";
            //insert the formula that will show the grand total for each column
            for(int x=1; x < 18; x++)
            {
                m_view.setFormula(currentRow, x, formula);
            }
            formatCells(m_view, false, false, true, false, false, 3, currentRow, 0, currentRow, 0);
            formatCells(m_view, true, false, true, true, true, 3, currentRow, 1, currentRow, 16);
            formatCells(m_view, true, false, true, true, true, 3, 0, 17, currentRow, 17);

            //sets the column's outline to know that details are to the left of the summary
            m_view.setSummaryColsBeforeDetail(false);
            //add an outline to columns B through Q.
            m_view.setColOutlineLevel(1,16,1,true);
            //add an outline to columns B through D.
            m_view.setColOutlineLevel(1,3,2,false);
            //add an outline to columns F through H.
            m_view.setColOutlineLevel(5,7,2,false);
            //add an outline to columns J through L.
            m_view.setColOutlineLevel(9,11,2,false);
            //add an outline to columns N through P.
            m_view.setColOutlineLevel(13,15,2,false);

			//writes out the XLS file
//            m_view.write("./out.xls");
            m_view.writeXLSX("./out.xlsx");

            View m_view1 = com.jxcell.designer.Designer.newDesigner(m_view).getView();
            m_view1.setTextAntialiasing(true);
            m_view1.setViewScale(90);
		}
        catch (CellException e)
        {
			e.printStackTrace();
		}
        catch (IOException ioe)
        {
			ioe.printStackTrace();
		}
        finally
        {
		}
	}

    private int insertRegion(View m_view, int row, String div, String[] reps)
            throws CellException
    {
        int currentRow = row;

        //insert the text value that will be the header for the region
        m_view.setText(currentRow, 0, div + " Region");
        formatCells(m_view, false, false, true, false, false, 2, currentRow, 0, currentRow, 16);
        currentRow++;

        for(int i=0;i<reps.length;i++)
        {
            String repName = reps[i];
            insertDataRow(m_view, repName, currentRow++);
        }
        //create a defined name for the division that we have just completed
        m_view.setDefinedName(div, "Sheet1!A$" + (row + 2) + ":Sheet1!A$" + currentRow);

        //add an outline to the region.
        m_view.setRowOutlineLevel((row + 1), (currentRow - 1), 1, false);

        //Set the values for the group summary row using setText and setFormula
        m_view.setText(currentRow, 0, div + " Region Totals");
        for(int i=1; i < 18; i++)
        {
            m_view.setFormula(currentRow, i, "sum(" + div + ")");
        }
        formatCells(m_view, false, false, true, false, false, 2, currentRow, 0, currentRow, 0);
        formatCells(m_view, true, false, true, true, false, 2, currentRow, 1, currentRow, 16);

        currentRow += 2;
        return currentRow;
	}

    private void insertDataRow(View m_view, String repName, int row)
            throws CellException
    {
		//insert a text value into the row number passed in and column 0
		m_view.setText(row, 0, repName);
		formatCells(m_view, false, true, false, false, false, 0, row, 0, row, 0);

		//inserts a number into the row passed in and columns 1, 2 and 3
		m_view.setNumber(row, 1, getRandom());
		m_view.setNumber(row, 2, getRandom());
		m_view.setNumber(row, 3, getRandom());
		formatCells(m_view, true, false, false, true, false, 0, row, 1, row, 3);

		//inserts a formula in the row passed in and column 4.
		//formulas can contain cell references or defined name just like in Excel.
		//Note that you do not use the = in the formula when using setFormula method.
		m_view.setFormula(row, 4, "sum((Jan):(Mar))");
		formatCells(m_view, true, false, true, true, false, 1, row, 4, row, 4);

		m_view.setNumber(row, 5, getRandom());
		m_view.setNumber(row, 6, getRandom());
		m_view.setNumber(row, 7, getRandom());
		formatCells(m_view, true, false, false, true, false, 0, row, 5, row, 7);

		m_view.setFormula(row, 8, "sum((Apr):(Jun))");
		formatCells(m_view, true, false, true, true, false, 1, row, 8, row, 8);

		m_view.setNumber(row, 9, getRandom());
		m_view.setNumber(row, 10, getRandom());
		m_view.setNumber(row, 11, getRandom());
		formatCells(m_view, true, false, false, true, false, 0, row, 8, row, 11);

		m_view.setFormula(row, 12, "sum((Jul):(Sep))");
		formatCells(m_view, true, false, true, true, false, 1, row, 12, row, 12);

		m_view.setNumber(row, 13, getRandom());
		m_view.setNumber(row, 14, getRandom());
		m_view.setNumber(row, 15, getRandom());
		formatCells(m_view, true, false, false, true, false, 0, row, 13, row, 15);

		m_view.setFormula(row, 16, "sum((Oct):(Dec))");
		formatCells(m_view, true, false, true, true, false, 1, row, 16, row, 16);

		m_view.setFormula(row, 17, "sum(qOne, qTwo, qThree, qFour)");
	}

    private void formatCells(View m_view, boolean alignRight , boolean indent, boolean bold, boolean number, boolean useCurrency, int color, int row1, int col1, int row2, int col2)
            throws CellException
    {
		//get the cell format object associated with the range of cells
		com.jxcell.CellFormat cf = m_view.getCellFormat(row1, col1, row2, col2);

		//set the cell(s) to be bold
		if(bold)
            cf.setFontBold(true);

		//apply a cell fill color
		if(color == 1)
        {
            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
			cf.setPatternFG(m_view.getPaletteEntry(31));
		}
        else if(color == 2)
        {
            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
			cf.setPatternFG(m_view.getPaletteEntry(32));
		}
        else if(color == 3)
        {
            cf.setPattern(com.jxcell.CellFormat.PatternSolid);
			cf.setPatternFG(m_view.getPaletteEntry(33));
		}

		//set the cell value to be aligned to the right
		if(alignRight)
            cf.setHorizontalAlignment(com.jxcell.CellFormat.HorizontalAlignmentRight);

		//set the indent
		if(indent)
            cf.setIndent(1);

		//format the number
		if(number)
            if(useCurrency)
                cf.setCustomFormat("$###,###,##0.00;($###,###,###0.00;0");
            else
                cf.setCustomFormat("###,###,##0.00;(###,###,###0.00;0");

		//apply the cell format
		m_view.setCellFormat(cf, row1, col1, row2, col2);
	}

    private double getRandom()
    {
        double f = (r.nextDouble() - .5) * 10000;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double ff = new Double(decimalFormat.format(f));
        if (ff < 0)
            ff = ff * -1;
        return ff;
    }
}