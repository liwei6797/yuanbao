package com.jxcell.dialog;

import com.jxcell.*;
import com.jxcell.tools.GridManager;

import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.awt.event.ItemEvent;

public class ChartDataRangeDlg extends Dialog
        implements SelectionChangedListener
{

    private JCheckBox chkRows;
    private JCheckBox chkColumns;
    private GRChart grchart;
    private JTextField textFieldDataRange;
    private JComboBox CBchartType;

    public ChartDataRangeDlg(View view, GRChart floatElement)
    {
        super(view, "Chart Data", false);
        m_view.addSelectionChangedListener(this);
        grchart = floatElement;
        setTitle("Chart Data");
        ButtonGroup checkboxgroup = new ButtonGroup();
        chkRows = new JCheckBox("Rows", false);
        chkColumns = new JCheckBox("Columns", true);
        CBchartType = new JComboBox();
        CBchartType.addItem("column");
        CBchartType.addItem("bar");
        CBchartType.addItem("step");
        CBchartType.addItem("area");
        CBchartType.addItem("line");
        CBchartType.addItem("pie");
        CBchartType.addItem("doughnut");
        CBchartType.addItem("scatter");
        CBchartType.addItem("bubble");
        checkboxgroup.add(chkRows);
        checkboxgroup.add(chkColumns);
        textFieldDataRange = new JTextField();
        Label label = new Label("Series In");
        Label label1 = new Label("Chart type");
        com.jxcell.tools.FramePanel framepanel = new com.jxcell.tools.FramePanel("Data Range");
        initDialog();

        GridManager gridManager = new GridManager();

        ChartDataRangeDlg defrowheightdlg = this;
        Object obj = framepanel;
        gridManager.insert(defrowheightdlg, getContentPane(), ((Component) (obj)), 0, 0, 2, 2, 0, 0, 0, 0, gridManager.insets[0], 1, 10);
        defrowheightdlg = this;
        obj = framepanel;
        Object obj1 = textFieldDataRange;
        gridManager.insert(defrowheightdlg, ((Container) (obj)), ((Component) (obj1)), 0, 2, 3, 1, 1, 1, 0, 0, gridManager.insets[0], 2, 17);
        defrowheightdlg = this;
        obj = framepanel;
        obj1 = label;
        gridManager.insert(defrowheightdlg, ((Container) (obj)), ((Component) (obj1)), 0, 3, 1, 1, 1, 1, 0, 0, gridManager.insets[0], 2, 17);
        defrowheightdlg = this;
        obj = framepanel;
        obj1 = chkRows;
        gridManager.insert(defrowheightdlg, ((Container) (obj)), ((Component) (obj1)), 1, 3, 1, 1, 1, 1, 0, 0, gridManager.insets[0], 0, 17);
        defrowheightdlg = this;
        obj = framepanel;
        obj1 = chkColumns;
        gridManager.insert(defrowheightdlg, ((Container) (obj)), ((Component) (obj1)), 2, 3, 1, 1, 1, 1, 0, 0, gridManager.insets[0], 0, 17);

        defrowheightdlg = this;
        obj = framepanel;
        obj1 = label1;
        gridManager.insert(defrowheightdlg, ((Container) (obj)), ((Component) (obj1)), 0, 4, 1, 1, 1, 1, 0, 0, gridManager.insets[0], 2, 17);
        defrowheightdlg = this;
        obj = framepanel;
        obj1 = CBchartType;
        gridManager.insert(defrowheightdlg, ((Container) (obj)), ((Component) (obj1)), 1, 4, 1, 1, 1, 1, 0, 0, gridManager.insets[0], 0, 17);

        defrowheightdlg = this;
        obj = btOK;
        gridManager.insert(defrowheightdlg, getContentPane(), ((Component) (obj)), 2, 0, 1, 1, 0, 0, 20, 0, new Insets(5, 10, 0, 0), 2, 11);
        defrowheightdlg = this;
        obj = btCancel;
        gridManager.insert(defrowheightdlg, getContentPane(), ((Component) (obj)), 2, 1, 1, 1, 0, 0, 20, 0, new Insets(5, 10, 0, 0), 2, 11);
    }

    private void initDialog()
    {
        try
        {
            String rangeStr = grchart.getLinkRange();
            textFieldDataRange.setText(rangeStr);
            int type = grchart.getChartType();
            if(type == GRChart.eChartColumn)
                CBchartType.setSelectedIndex(0);
            else if(type == GRChart.eChartBar)
                CBchartType.setSelectedIndex(1);
            else if(type == GRChart.eChartStep)
                CBchartType.setSelectedIndex(2);
            else if(type == GRChart.eChartArea)
                CBchartType.setSelectedIndex(3);
            else if(type == GRChart.eChartLine)
                CBchartType.setSelectedIndex(4);
            else if(type == GRChart.eChartPie)
                CBchartType.setSelectedIndex(5);
            else if(type == GRChart.eChartDoughnut)
                CBchartType.setSelectedIndex(6);
            else if(type == GRChart.eChartScatter)
                CBchartType.setSelectedIndex(7);
            else if(type == GRChart.eChartBubble)
                CBchartType.setSelectedIndex(8);
            if(grchart.isSeriesInRows())
              chkRows.setSelected(true);
            else
              chkColumns.setSelected(true);
        }
        catch (Exception e)
        {}
    }

    protected void setdefault()
    {
    }

    protected void okClicked()
    {
        update();
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        Object obj = itemevent.getSource();
        if(obj == chkRows)
        {
        }
    }

    public void update()
    {
        String txtRange = textFieldDataRange.getText();
        try
        {
            int select = CBchartType.getSelectedIndex();
            if(select == 0)
                grchart.setChartType(GRChart.eChartColumn);
            else if(select == 1)
                grchart.setChartType(GRChart.eChartBar);
            else if(select == 2)
                grchart.setChartType(GRChart.eChartStep);
            else if(select == 3)
                grchart.setChartType(GRChart.eChartArea);
            else if(select == 4)
                grchart.setChartType(GRChart.eChartLine);
            else if(select == 5)
                grchart.setChartType(GRChart.eChartPie);
            else if(select == 6)
                grchart.setChartType(GRChart.eChartDoughnut);
            else if(select == 7)
                grchart.setChartType(GRChart.eChartScatter);
            else if(select == 8)
                grchart.setChartType(GRChart.eChartBubble);
            if(chkRows.isSelected())
                grchart.setLinkRange(txtRange,true);
            else
                grchart.setLinkRange(txtRange,false);
        }
        catch (CellException e)
        {
            e.printStackTrace();
        }
    }

    public void selectionChanged(SelectionChangedEvent evt)
    {
        try
        {
            resetSample();
        }
        catch(CellException cellexception)
        {
        }
    }

    void resetSample()
        throws CellException
    {
        if(m_view == null)
            return;
        String selectionStr = m_view.getSheetName(0) + "!" + m_view.formatRCNr(m_view.getSelStartRow(), m_view.getSelStartCol(),true);
        selectionStr = selectionStr + ":" + m_view.formatRCNr(m_view.getSelEndRow(), m_view.getSelEndCol(),true);
        textFieldDataRange.setText(selectionStr);
    }
}
