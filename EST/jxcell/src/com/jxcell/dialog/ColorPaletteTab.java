package com.jxcell.dialog;

import com.jxcell.paint.Palette;
import com.jxcell.tools.ColorPanel;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.FramePanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;


class ColorPaletteTab extends TabBase
{

    private ColorPanel innpane;
    private JSlider sclred;
    private JSlider sclgreen;
    private JSlider sclblue;
    private JTextField txtred;
    private JTextField txtgreen;
    private JTextField txtblue;
    private JButton btncolor;
    private JButton btnpalette;

    ColorPaletteTab(TabDialog tabDialog)
    {
        super(tabDialog);
        com.jxcell.tools.FramePanel framepanel = new FramePanel("Palette Colors");
        innpane = getColorPanel();
        sclred = getSlider(0, 255, 0);
        sclgreen = getSlider(0, 255, 0);
        sclblue = getSlider(0, 255, 0);
        txtred = new JTextField("", 4);
        txtred.setEditable(false);
        txtgreen = new JTextField("", 4);
        txtgreen.setEditable(false);
        txtblue = new JTextField("", 4);
        txtblue.setEditable(false);
        btncolor = getJButton("Default Color;C");
        btnpalette = getJButton("Default Palette;P");
        GridManager gridManager = new GridManager();
        gridManager.insertWithInsetBC(this, this, framepanel, 0, 0, 1, 6, gridManager.getInsets(3));
        gridManager.insertHW(this, framepanel, innpane, 0, 0);
        gridManager.insert(this, this, btnpalette, 0, 6, 1, 1, 0, 0, 8, 0, gridManager.getInsets(0), 0, 17);
        gridManager.insert(this, this, btncolor, 1, 6, 2, 1, 0, 0, 0, 0, gridManager.getInsets(2), 0, 17);
        gridManager.insertWithInsetHW(this, this, new JLabel("Red Value:"), 1, 0, 1, 1, gridManager.getInsets(2));
        gridManager.insert(this, this, txtred, 2, 0, 1, 1, 10, 0, 0, 0, gridManager.getInsets(7), 0, 17);
        gridManager.insertWithInsetHW(this, this, sclred, 1, 1, 2, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, this, new JLabel("Green Value:"), 1, 2, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, this, txtgreen, 2, 2, 1, 1, gridManager.getInsets(7));
        gridManager.insertWithInsetHW(this, this, sclgreen, 1, 3, 2, 1, gridManager.getInsets(2));
        gridManager.insertWithInsetHW(this, this, new JLabel("Blue Value:"), 1, 4, 1, 1, gridManager.getInsets(2));
        gridManager.insertWithInset(this, this, txtblue, 2, 4, 1, 1, gridManager.getInsets(7));
        gridManager.insertWithInsetHW(this, this, sclblue, 1, 5, 2, 1, gridManager.getInsets(8));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj == btncolor)
        {
            innpane.setdefColor(innpane.getSelectedColorIndex() + 8);
            setControls();
        } else
        if(obj == btnpalette)
        {
            innpane.resetDefColors();
            setControls();
        } else
        if(obj == innpane)
        {
            setControls();
            return;
        }
        super.actionPerformed(actionevent);
    }

    protected ColorPanel getColorPanel()
    {
        Palette palette1 = new Palette();
        palette1.copy(getPalette());
        return super.getColorPanel(palette1);
    }

    public void updateControls()
    {
        innpane.setSelectedIndex(0);
        setControls();
    }

    protected void setOptions()
        throws Throwable
    {
        getPalette().copy(innpane.getPalette());
    }

    private void changeValue()
    {
        innpane.setRGB(innpane.getSelectedColorIndex() + 8, sclred.getValue(), sclgreen.getValue(), sclblue.getValue());
        setControls();
        setApplyButtonEnabled(true);
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        if(this.dlgBase.getFocusOwner() instanceof JSlider)
            changeValue();
    }

    public void setEnabled()
    {
    }

    private void setControls()
    {
        int r = innpane.getRed();
        int g = innpane.getGreen();
        int b = innpane.getBlue();
        sclred.setValue(r);
        sclgreen.setValue(g);
        sclblue.setValue(b);
        txtred.setText(Integer.toString(r));
        txtgreen.setText(Integer.toString(g));
        txtblue.setText(Integer.toString(b));
    }
}
