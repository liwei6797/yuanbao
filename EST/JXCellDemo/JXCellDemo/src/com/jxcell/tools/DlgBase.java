package com.jxcell.tools;

import com.jxcell.BookAdapter;
import com.jxcell.Dialog;
import com.jxcell.View;
import com.jxcell.mvc.UndoAdapter;
import com.jxcell.mvc.UndoableEdit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import java.util.Vector;


public abstract class DlgBase extends Dialog
{

    private Vector vector;

    protected DlgBase(JComponent component, String title, boolean model)
    {
        super(component, "", model);
        setTitle(title);
    }

    public FillPanel newFillPanel()
    {
        return new FillPanel(getAdapter());
    }

    public FillPanel newFillPanel(int option)
    {
        return new FillPanel(getAdapter(), option);
    }

    public JButton newJButton(String text)
    {
        String strs[] = splitString(text);
        JButton jbutton = new JButton(strs.length <= 0 ? "" : strs[0]);
        if(strs.length > 1)
            jbutton.setMnemonic(strs[1].charAt(0));
        return jbutton;
    }

    public JCheckBox newJCheckBox(String text, boolean bcheck)
    {
        String strs[] = splitString(text);
        JCheckBox jcheckbox = new JCheckBox(strs.length <= 0 ? "" : strs[0], bcheck);
        if(strs.length > 1)
            jcheckbox.setMnemonic(strs[1].charAt(0));
        return jcheckbox;
    }

    public JCheckBox[] newJCheckBoxs(String text)
    {
        String strs[] = splitStringEx(text);
        JCheckBox ajcheckbox[] = new JCheckBox[strs.length];
        for(int l = 0; l < ajcheckbox.length; l++)
            ajcheckbox[l] = newJCheckBox(strs[l], false);

        return ajcheckbox;
    }

    public ColorPanel newColorPanel()
    {
        return new ColorPanel(getAdapter());
    }

    public ColorPanel newColorPanel(int index)
    {
        ColorPanel colorPanel = newColorPanel();
        colorPanel.setselectedIndex(index);
        return colorPanel;
    }

    public ColorPanel newColorPanel(String autostr, int sel, String ustr, int index)
    {
        return new ColorPanel(getAdapter(), autostr, sel, ustr, index);
    }

    public JComboBox newJComboBox(boolean bEdit)
    {
        JComboBox jcombobox = new JComboBox();
        jcombobox.setEditable(bEdit);
        return jcombobox;
    }

    public JComboBox newJComboBox(boolean bEdit, String text)
    {
        JComboBox jcombobox = newJComboBox(bEdit);
        String strs[] = splitString(text);
        for(int l = 0; l < strs.length; l++)
            jcombobox.addItem(strs[l]);

        return jcombobox;
    }

    protected GridManager getGridManager()
    {
        return new GridManager();
    }

    public JLabel[] newJLabels(String text)
    {
        String strs[] = splitString(text);
        JLabel labels[] = new JLabel[strs.length];
        for(int l = 0; l < labels.length; l++)
            labels[l] = new JLabel(strs[l]);

        return labels;
    }

    public LinePanel newLinePanel()
    {
        return new LinePanel(getAdapter());
    }

    public JRadioButton newJRadioButton(String text, boolean selected)
    {
        String strs[] = splitString(text);
        JRadioButton jradiobutton = new JRadioButton(strs.length <= 0 ? "" : strs[0], selected);
        if(strs.length > 1)
            jradiobutton.setMnemonic(strs[1].charAt(0));
        return jradiobutton;
    }

    public JRadioButton[] newJRadioButtons(String text)
    {
        String strs[] = splitStringEx(text);
        JRadioButton ajradiobutton[] = new JRadioButton[strs.length];
        ButtonGroup buttongroup = new ButtonGroup();
        for(int i = 0; i < ajradiobutton.length; i++)
            buttongroup.add(ajradiobutton[i] = newJRadioButton(strs[i], false));

        return ajradiobutton;
    }

    public ListPanel newListPanel(int visibleRowCount)
    {
        ListPanel listPanel = new ListPanel();
        listPanel.getList().setVisibleRowCount(visibleRowCount);
        return listPanel;
    }

    public ListPanel newListPanel(int visibleRowCount, int width)
    {
        ListPanel listPanel = newListPanel(visibleRowCount);
        listPanel.getList().setFixedCellWidth(width);
        return listPanel;
    }

    public ListPanel newListPanel(int visibleRowCount, int width, String text)
    {
        ListPanel listPanel = newListPanel(visibleRowCount, text);
        listPanel.getList().setFixedCellWidth(width);
        return listPanel;
    }

    public ListPanel newListPanel(int visibleRowCount, String text)
    {
        ListPanel listPanel = newListPanel(visibleRowCount);
        String strs[] = splitString(text);
        for(int i = 0; i < strs.length; i++)
            listPanel.addElement(strs[i]);

        return listPanel;
    }

    public JSlider newJSlider(int min, int max, int value)
    {
        JSlider jslider = new JSlider(min, max, value);
        jslider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        return jslider;
    }

    public BookAdapter getAdapter()
    {
        return (BookAdapter)((com.jxcell.View)super.m_component).getAdapter();
    }

    public abstract void getLock();

    protected abstract com.jxcell.View getView();

    public short getRadioIndex(JRadioButton radioButtons[])
    {
        for(short i = 0; i < radioButtons.length; i++)
            if(radioButtons[i].isSelected())
                return i;

        return -1;
    }

    protected abstract void setdefault()
            throws Throwable;

    protected void onOK()
    {
        View viewPanel = getView();
        if(viewPanel != null)
            viewPanel.getLock();
        try
        {
            okClicked();
            dispose();
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
            getFocusComponent().requestFocus();
        }
        finally
        {
            if(viewPanel != null)
                viewPanel.releaseLock();
        }
    }

    public abstract void releaseLock();

    protected void okClicked()
        throws Throwable
    {
    }

    public void show()
    {
        View view1 = getView();
        if(view1 != null)
        {
            view1.getLock();
            try
            {
                setdefault();
            }
            catch(Throwable e)
            {
                e.printStackTrace();
            }
            finally
            {
                view1.releaseLock();
            }
            super.show();
        } else
        {
            try
            {
                setdefault();
            }
            catch (Throwable throwable)
            {
            }
            super.show();
        }
    }

    public void showMessage(Throwable throwable)
    {
        Toolkit.getDefaultToolkit().beep();
        String msg = throwable.getMessage();
        if(msg == null || msg.equals(""))
            msg = "( " + throwable + " )";
        JOptionPane.showMessageDialog(this, msg, "Jxcell Workbook", 0);
    }

    public void showMessage(String msg)
    {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(this, msg, "Jxcell Workbook", 1);
        requestFocus();
    }

    public String[] splitString(String texts)
    {
        if(texts != null)
        {
            StringTokenizer stringtokenizer = new StringTokenizer(texts, ";");
            int len = stringtokenizer.countTokens();
            String strs[] = new String[len];
            for(int i1 = 0; i1 < len; i1++)
                strs[i1] = stringtokenizer.nextToken();

            return strs;
        } else
        {
            return new String[0];
        }
    }

    protected String[] splitStringEx(String text)
    {
        if(text != null)
        {
            StringTokenizer stringtokenizer = new StringTokenizer(text, "'");
            int len = stringtokenizer.countTokens();
            String strs[] = new String[len];
            for(int i1 = 0; i1 < len; i1++)
                strs[i1] = stringtokenizer.nextToken();

            return strs;
        } else
        {
            return new String[0];
        }
    }

    protected boolean wantsUndoableEdit()
    {
        View view = getView();
        return view.wantsUndoableEdit();
    }

    protected void addEdit(UndoableEdit undoableEdit)
    {
        View view = getView();
        view.addEdit(undoableEdit);
    }

    protected void fireUndoableEdit(com.jxcell.ss.UndoableEdit undoableedit)
    {
        View view = getView();
        view.addEdit(undoableedit);
    }

    public void windowClosed(WindowEvent windowevent)
    {
        super.windowClosed(windowevent);
        getGridManager().removeListener(this);
    }
}
