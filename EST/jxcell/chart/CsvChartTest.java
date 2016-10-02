import com.jxcell.ChartShape;
import com.jxcell.RangeRef;
import com.jxcell.View;

public class CsvChartTest
{
    public static void main(String args[])
    {
        View m_view;
        try
        {
            m_view =new View();
            m_view.read(".\\book.csv");
            ChartShape chart = m_view.addChart((short)1, (short)9,(short)9, (short)22);
            chart.initData(new RangeRef(0,1,4,7),true);     //Sheet1!$A$2:$E$8
            m_view.write(".\\columnChart.xls");
            chart.setChartType(ChartShape.TypeBar);
            m_view.write(".\\barChart.xls");
            chart.setChartType(ChartShape.TypePie);
            m_view.write(".\\pieChart.xls");
            chart.setChartType(ChartShape.TypeLine);
            m_view.write(".\\lineChart.xls");
            chart.setChartType(ChartShape.TypeArea);
            m_view.write(".\\areaChart.xls");
            chart.setChartType(ChartShape.TypePie);
            m_view.write(".\\pieChart.xls");
            chart.setChartType(ChartShape.TypeDoughnut);
            m_view.write(".\\doughnutChart.xls");
            chart.setChartType(ChartShape.TypeScatter);
            m_view.write(".\\scatterChart.xls");
            chart.setChartType(ChartShape.TypeBubble);
            m_view.write(".\\bubbleChart.xls");
//            Designer.newDesigner(m_view);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
