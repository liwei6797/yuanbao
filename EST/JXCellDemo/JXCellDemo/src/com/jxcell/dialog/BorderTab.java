package com.jxcell.dialog;

import com.jxcell.ss.TArea;
import com.jxcell.tools.ColorPanel;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


class BorderTab extends TabBase
    implements MouseListener
{

    private ColorPanel colorPanel;
    private StatePanel statePanel[];
    private LinePanel linePanel[];
    private JLabel lblStates[];
    private short currentStyle;

    BorderTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanelStyle = new FramePanel("Style");
        com.jxcell.tools.FramePanel framepanelColor = new FramePanel("Color");
        com.jxcell.tools.FramePanel framepanelBorder = new FramePanel("Border");
        colorPanel = getColorPanel();
        colorPanel.setSelected1(0);
        lblStates = getJLabels("Outline;Top;Left;Right;Bottom;Horizontal;Vertical");
        statePanel = new StatePanel[7];
        linePanel = new LinePanel[8];
        currentStyle = 1;
        getLock();
        try
        {
            int rgb1 = getSheetViewInfo().getBackColor();
            java.awt.Color color = getColor(getPalette().getRGB(rgb1));
            java.awt.Color color1 = colorPanel.getSelectedColor();
            for(int i = 0; i < statePanel.length; i++)
            {
                statePanel[i] = new StatePanel(color, color1, 40, 20);
                statePanel[i].addMouseListener(this);
            }

            for(short i = 0; i < linePanel.length; i++)
            {
                linePanel[i] = new LinePanel(i, color1, 60, 20);
                linePanel[i].addMouseListener(this);
            }

        }
        finally
        {
            releaseLock();
        }
        linePanel[1].setDrawBackGround(true);
        linePanel[1].setColorStyle((short)2);
        linePanel[3].setVisible(false);
        linePanel[4].setVisible(false);
        linePanel[7].setVisible(false);
        GridManager gridManager = new GridManager();
        gridManager.insert(this, this, framepanelStyle, 0, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
        gridManager.insert(this, this, framepanelColor, 1, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(2), 0, 18);
        gridManager.insert(this, this, framepanelBorder, 2, 0, 1, 1, 1, 1, 0, 0, gridManager.getInsets(2), 0, 18);
        for(int i = 0; i < linePanel.length; i++)
            gridManager.insert(this, framepanelStyle, linePanel[i], 0, i);

        gridManager.insertHW(this, framepanelColor, colorPanel, 0, 0);
        for(int i = 0; i < statePanel.length; i++)
        {
            gridManager.insert(this, framepanelBorder, statePanel[i], 0, i);
            gridManager.insertWithInsetHW(this, framepanelBorder, lblStates[i], 1, i, 1, 1, gridManager.getInsets(7));
        }

    }

    public void actionPerformed(ActionEvent actionevent)
    {
        super.actionPerformed(actionevent);
        if(actionevent.getSource() instanceof ColorPanel)
        {
            for(int i = 0; i < linePanel.length; i++)
                linePanel[i].setLineColor(((ColorPanel)actionevent.getSource()).getSelectedColor());

        }
    }

    public void updateControls()
    {
        com.jxcell.CellFormat cellformat = getCellFormat();
        try
        {
            statePanel[1].setState(cellformat.isUndefined((short)20) || cellformat.isUndefined((short)26), cellformat.getTopBorder(), getColor(cellformat.getTopBorderColor()));
            statePanel[2].setState(cellformat.isUndefined((short)21) || cellformat.isUndefined((short)27), cellformat.getLeftBorder(), getColor(cellformat.getLeftBorderColor()));
            statePanel[3].setState(cellformat.isUndefined((short)23) || cellformat.isUndefined((short)29), cellformat.getRightBorder(), getColor(cellformat.getRightBorderColor()));
            statePanel[4].setState(cellformat.isUndefined((short)22) || cellformat.isUndefined((short)28), cellformat.getBottomBorder(), getColor(cellformat.getBottomBorderColor()));
            statePanel[0].setState(isOutlineUndefined(), cellformat.getTopBorder(), getColor(cellformat.getTopBorderColor()));
            statePanel[5].setState(cellformat.isUndefined((short)25) || cellformat.isUndefined((short)31), cellformat.getHorizontalInsideBorder(), getColor(cellformat.getHorizontalInsideBorderColor()));
            statePanel[6].setState(cellformat.isUndefined((short)24) || cellformat.isUndefined((short)30), cellformat.getVerticalInsideBorder(), getColor(cellformat.getVerticalInsideBorderColor()));
            boolean brows = false;
            boolean bcols = false;
            if(getNrRanges() > 0)
            {
                TArea trange1 = getRange(0);
                brows = trange1.getRow1() != trange1.getRow2();
                bcols = trange1.getCol1() != trange1.getCol2();
            }
            statePanel[5].setEnabled(brows);
            lblStates[5].setEnabled(brows);
            statePanel[6].setEnabled(bcols);
            lblStates[6].setEnabled(bcols);
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }

    private boolean isOutlineUndefined()
    {
        short style = statePanel[1].getLineStyle();
        java.awt.Color color = statePanel[1].getLineColor();
        for(int i = 1; i <= 4; i++)
        {
            StatePanel statePanel1 = statePanel[i];
            if(statePanel1.getState() == 2)
                return true;
            if(statePanel1.getLineStyle() != style)
                return true;
            if(statePanel1.getLineColor() != color)
                return true;
        }

        return false;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        Object obj = mouseevent.getSource();
        if((obj instanceof LinePanel) && !(obj instanceof StatePanel))
        {
            LinePanel linePanel1 = (LinePanel)obj;
            if(!linePanel1.isDrawBackGround())
                linePanel1.setColorStyle((short)1);
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        Object obj = mouseevent.getSource();
        if((obj instanceof LinePanel) && !(obj instanceof StatePanel))
        {
            LinePanel linePanel1 = (LinePanel)obj;
            if(!linePanel1.isDrawBackGround())
                linePanel1.setColorStyle((short)0);
        }
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        Object obj = mouseevent.getSource();
        if((obj instanceof LinePanel) && !(obj instanceof StatePanel))
        {
            LinePanel linePanel1 = (LinePanel)obj;
            for(short i = 0; i < linePanel.length; i++)
                if(linePanel[i] == linePanel1)
                {
                    linePanel[i].setDrawBackGround(true);
                    linePanel[i].setColorStyle((short)2);
                    currentStyle = i;
                } else
                {
                    linePanel[i].setDrawBackGround(false);
                    linePanel[i].setColorStyle((short)0);
                }

        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        try
        {
            Object obj = mouseevent.getSource();
            if(obj instanceof StatePanel)
            {
                StatePanel statePanel1 = (StatePanel)obj;
                java.awt.Color color = colorPanel.getSelectedColor();
                for(short i = 0; i < statePanel.length; i++)
                    if(statePanel1 == statePanel[i])
                    {
                        statePanel[i].setState(false, currentStyle, color);
                        if(i == 0)
                        {
                            for(short word1 = 1; word1 <= 4; word1++)
                            {
                                statePanel[word1].setState(false, currentStyle, color);
                                updateSampleFormats(word1);
                            }

                            return;
                        }
                        if(i <= 4)
                            statePanel[0].setState(isOutlineUndefined(), currentStyle, color);
                        updateSampleFormats(i);
                        return;
                    }

            }
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }

    private void updateSampleFormats(short border)
    {
        try
        {
            com.jxcell.CellFormat cellformat = getCellFormat();
            java.awt.Color color = colorPanel.getSelectedColor();
            short state = statePanel[border].getState();
            switch(border)
            {
                default:
                    break;

                case 1:
                    if(state == 1)
                    {
                        cellformat.setTopBorderColor(getColorVal(color));
                        cellformat.setTopBorder(currentStyle);
                    } else
                    {
                        cellformat.setTopBorder((short)0);
                    }
                    break;

                case 2:
                    if(state == 1)
                    {
                        cellformat.setLeftBorderColor(getColorVal(color));
                        cellformat.setLeftBorder(currentStyle);
                    } else
                    {
                        cellformat.setLeftBorder((short)0);
                    }
                    break;

                case 3:
                    if(state == 1)
                    {
                        cellformat.setRightBorderColor(getColorVal(color));
                        cellformat.setRightBorder(currentStyle);
                    } else
                    {
                        cellformat.setRightBorder((short)0);
                    }
                    break;

                case 4:
                    if(state == 1)
                    {
                        cellformat.setBottomBorderColor(getColorVal(color));
                        cellformat.setBottomBorder(currentStyle);
                    } else
                    {
                        cellformat.setBottomBorder((short)0);
                    }
                    break;

                case 5:
                    if(state == 1)
                    {
                        cellformat.setHorizontalInsideBorderColor(getColorVal(color));
                        cellformat.setHorizontalInsideBorder(currentStyle);
                    } else
                    {
                        cellformat.setHorizontalInsideBorder((short)0);
                    }
                    break;

                case 6:
                    if(state == 1)
                    {
                        cellformat.setVerticalInsideBorderColor(getColorVal(color));
                        cellformat.setVerticalInsideBorder(currentStyle);
                    } else
                    {
                        cellformat.setVerticalInsideBorder((short)0);
                    }
                    break;
            }
            setApplyButtonEnabled(true);
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }
}
