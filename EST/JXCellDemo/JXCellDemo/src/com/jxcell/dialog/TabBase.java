package com.jxcell.dialog;

import com.jxcell.CellException;
import com.jxcell.View;
import com.jxcell.mvc.PrintInfo;
import com.jxcell.paint.Palette;
import com.jxcell.ss.*;
import com.jxcell.tools.*;
import com.jxcell.util.CharBuffer;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;


public abstract class TabBase extends JPanel
        implements ActionListener, FocusListener, KeyListener, DocumentListener, ListSelectionListener, ChangeListener
{

    View m_view;
    private CharBuffer charBuffer;
    public TabDialog dlgBase;
    public JComponent m_comp;
    private boolean m_bSelected;

    public TabBase(TabDialog dlgbase)
    {
        dlgBase = dlgbase;
        m_view = dlgbase.m_view;
        charBuffer = new CharBuffer();
        setOpaque(false);
    }

    protected int getColorVal(Color color)
    {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return r << 16 | g << 8 | b;
    }

    protected Color getColor(int rgb)
    {
        return new Color(rgb >> 16 & 0xff, rgb >> 8 & 0xff, rgb & 0xff);
    }

    Book getWorkBook()
    {
        return getView().getBook();
    }

    BookViewInfo getBookViewInfo()
    {
        return getView().getBookViewInfo();
    }

    CharBuffer clearText()
    {
        charBuffer.clear();
        return charBuffer;
    }

    com.jxcell.CellFormat getCellFormat()
    {
        return dlgBase.getCellFormat();
    }

    Palette getPalette()
    {
        return getView().getPalette();
    }

    PrintInfo getPrintInfo()
    {
        return getSheet().getPrintInfo();
    }

    TArea getRange(int i)
    {
        return (TArea) getSelection().getAreaConst(i);
    }

    Selection getSelection()
    {
        return getView().getSelection();
    }

    int getNrRanges()
    {
        return getSelection().getAreaCount();
    }

    protected void setOptions()
        throws Throwable
    {
    }

    Sheet getSheet()
    {
        return getView().getSheet();
    }

    SheetViewInfo getSheetViewInfo()
    {
        return getView().getSheetViewInfo();
    }

    com.jxcell.ss.SSView getView()
    {
        return (com.jxcell.ss.SSView) m_view.getView();
    }

    void showError(short word0)
        throws CellException
    {
        throw new CellException(word0);
    }

    void set(com.jxcell.CellFormat cellformat)
        throws Throwable
    {
    }

    void apply()
    {
        try
        {
            if(isShowing())
            {
                com.jxcell.CellFormat cellformat = getCellFormat();
                set(cellformat);
                setApplyButtonEnabled(true);
                return;
            }
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }

    public void DestroyObj()
    {
        dlgBase = null;
        m_comp = null;
        m_view = null;
        charBuffer = null;
    }


    protected FillPanel getFillPanel()
    {
        return dlgBase.newFillPanel();
    }

    protected FillPanel getFillPanel(int option)
    {
        return dlgBase.newFillPanel(option);
    }

    protected JButton getJButton(String lable)
    {
        return dlgBase.newJButton(lable);
    }

    protected JCheckBox getJCheckBox(String lable, boolean selected)
    {
        return dlgBase.newJCheckBox(lable, selected);
    }

    protected JCheckBox[] getJCheckBoxs(String lable)
    {
        return dlgBase.newJCheckBoxs(lable);
    }

    protected ColorPanel getColorPanel()
    {
        return dlgBase.newColorPanel();
    }

    protected ColorPanel getColorPanel(int index)
    {
        return dlgBase.newColorPanel(index);
    }

    protected ColorPanel getColorPanel(Palette palette)
    {
        return new ColorPanel(palette);
    }

    protected ColorPanel getColorPanel(String autoStr, int sel, String ustr, int index)
    {
        return dlgBase.newColorPanel(autoStr, sel, ustr, index);
    }

    protected JComboBox getJComboBox(boolean bEdit)
    {
        return dlgBase.newJComboBox(bEdit);
    }

    protected JComboBox getJComboBox(boolean bEdit, String text)
    {
        return dlgBase.newJComboBox(bEdit, text);
    }

    protected JLabel[] getJLabels(String lable)
    {
        return dlgBase.newJLabels(lable);
    }

    protected JLabel getImageLabel(Image image)
    {
        return new JLabel(new ImageIcon(image));
    }

    protected com.jxcell.tools.LinePanel getLinePanel()
    {
        return dlgBase.newLinePanel();
    }

    protected JRadioButton getJRadioButton(String lable, boolean selected)
    {
        return dlgBase.newJRadioButton(lable, selected);
    }

    protected JRadioButton[] getJRadioButtons(String lable)
    {
        return dlgBase.newJRadioButtons(lable);
    }

    protected ListPanel getListPanel(int rowcount)
    {
        return dlgBase.newListPanel(rowcount);
    }

    protected ListPanel getListPanel(int rowcount, int width)
    {
        return dlgBase.newListPanel(rowcount, width);
    }

    protected ListPanel getListPanel(int rowcount, int width, String text)
    {
        return dlgBase.newListPanel(rowcount, width, text);
    }

    protected ListPanel getListPanel(int rowcount, String text)
    {
        return dlgBase.newListPanel(rowcount, text);
    }

    protected JSlider getSlider(int min, int max, int value)
    {
        return dlgBase.newJSlider(min, max, value);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        setApplyButtonEnabled(true);
    }

    public void changedUpdate(DocumentEvent documentevent)
    {
    }

    public void setApplyButtonEnabled(boolean enabled)
    {
        dlgBase.btApply.setEnabled(m_bSelected = enabled);
    }

    public void setEnabled()
    {
        setApplyButtonEnabled(true);
    }

    public void focusGained(FocusEvent focusevent)
    {
        dlgBase.focusGained(focusevent);
    }

    public void focusLost(FocusEvent focusevent)
    {
        dlgBase.focusLost(focusevent);
    }

    public JComponent getFocusComponent()
    {
        return dlgBase.btOK;
    }

    public Insets getInsets()
    {
        super.getInsets();
        return new Insets(10, 10, 10, 10);
    }

    protected void getLock()
    {
        dlgBase.getLock();
    }

    protected short getRadioIndex(JRadioButton ajradiobutton[])
    {
        return dlgBase.getRadioIndex(ajradiobutton);
    }

    public abstract void updateControls()
            throws Throwable;

    public void insertUpdate(DocumentEvent documentevent)
    {
        setEnabled();
    }

    public boolean getSelected()
    {
        return m_bSelected;
    }

    public void keyPressed(KeyEvent keyevent)
    {
        dlgBase.keyPressed(keyevent);
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    protected void releaseLock()
    {
        dlgBase.releaseLock();
    }

    public void removeUpdate(DocumentEvent documentevent)
    {
        setEnabled();
    }

    public boolean setOptions(com.jxcell.mvc.UndoableEdit undoableEdit)
    {
        try
        {
            try
            {
                setOptions();
                return true;
            }
            catch(Throwable throwable)
            {
                dlgBase.undo(undoableEdit);
                showMessage(throwable);
            }
            return false;
        }
        finally
        {
            m_comp = null;
        }
    }

    protected void showMessage(Throwable throwable)
    {
        if(throwable instanceof IllegalArgumentException)
        {
            String mag = throwable.getMessage();
            if(mag == null || mag.equals(""))
                mag = "errIllegalArgument";
            dlgBase.showMessage(mag);
        } else
        {
            dlgBase.showMessage(throwable);
        }
        if(m_comp == null)
        {
            dlgBase.requestFocus();
        } else
        {
            m_comp.requestFocus();
        }
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        setApplyButtonEnabled(true);
    }

    protected double getTextfieldValue(JTextField jtextfield)
        throws IllegalArgumentException
    {
        try
        {
            m_comp = jtextfield;
            return Double.valueOf(jtextfield.getText()).doubleValue();
        }
        catch(Throwable _ex)
        {
            throw new IllegalArgumentException("errIllegalArgument");
        }
    }

    protected int getnumint(JTextField jtextfield)
        throws IllegalArgumentException
    {
        return (int)getTextfieldValue(jtextfield);
    }

    protected String[] splitString(String strs)
    {
        return dlgBase.splitString(strs);
    }

    public void valueChanged(ListSelectionEvent listselectionevent)
    {
        setApplyButtonEnabled(true);
    }


    private void makeChildrenTransparentRecursively()
    {
        Stack stack = new Stack();
        stack.push(this);
        while(stack.size() > 0)
        {
            JComponent jcomponent = (JComponent)stack.pop();
            int i = jcomponent.getComponentCount();
            int j = 0;
            while(j < i)
            {
                Component component = jcomponent.getComponent(j);
                if((component instanceof JComponent) && !(component instanceof JTextComponent) && !(component instanceof JList) && !(component instanceof JComboBox))
                {
                    ((JComponent)component).setOpaque(false);
                    stack.push(component);
                }
                j++;
            }
        }
    }

    protected void paintComponent(Graphics g)
    {
        makeChildrenTransparentRecursively();
        super.paintChildren(g);
    }

}
