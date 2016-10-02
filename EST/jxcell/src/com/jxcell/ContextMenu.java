package com.jxcell;

import com.jxcell.dialog.DeleteDlg;
import com.jxcell.dialog.FormatCellsDlg;
import com.jxcell.dialog.InsertDlg;
import com.jxcell.dialog.PasteSpecialDlg;
import com.jxcell.mvc.UndoableEdit;
import com.jxcell.ss.UndoableGlobalEdit;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ContextMenu
    implements PopupMenuListener, ActionListener
{

    private View m_view;
    private JPopupMenu m_popup;

    public ContextMenu(View view)
    {
        this.m_view = view;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String cmd = actionevent.getActionCommand();
        try
        {
            if(cmd.equals("Cut"))
            {
                addUndoableEdit();
                m_view.editCut();
                return;
            }
            if(cmd.equals("Copy"))
            {
                m_view.editCopy();
                return;
            }
            if(cmd.equals("Paste"))
            {
                addUndoableEdit();
                m_view.editPaste();
                return;
            }
            if(cmd.equals("Paste Special..."))
            {
                addUndoableEdit();
                PasteSpecialDlg pastespecialdlg = new PasteSpecialDlg(m_view);
                pastespecialdlg.show();
                return;
            }
            if(cmd.equals("Insert..."))
            {
                addUndoableEdit();
                InsertDlg insertdlg = new InsertDlg(m_view);
                insertdlg.show();
                return;
            }
            if(cmd.equals("Delete..."))
            {
                addUndoableEdit();
                DeleteDlg deletedlg = new DeleteDlg(m_view);
                deletedlg.show();
                return;
            }
            if(cmd.equals("Clear Contents"))
            {
                addUndoableEdit();
                m_view.editClear((short)2);
                return;
            }
            if(cmd.equals("Format Cells..."))
            {
                addUndoableEdit();
                FormatCellsDlg formatCellsDlg = new FormatCellsDlg(m_view, 127);
                formatCellsDlg.show();
                return;
            }
        }
        catch (CellException e)
        {
            e.printStackTrace();
        }
    }

    private void addUndoableEdit()
            throws CellException
    {
        if(m_view.getController().wantsUndoableEdit())
            m_view.getController().fireUndoableEdit(getUndoableEdit());
    }

    private UndoableEdit getUndoableEdit()
        throws CellException
    {
        return new UndoableGlobalEdit((com.jxcell.ss.SSView) m_view.getController().getView(), "");
    }
  
    public void destroy()
    {
        if(m_popup != null)
        {
            m_popup.removeAll();
            m_popup.setInvoker(null);
            m_popup = null;
        }
        m_view = null;
    }

    private JMenuItem getMenuItem(String text)
    {
        JMenuItem jmenuitem = new JMenuItem(text);
        jmenuitem.addActionListener(this);
        return jmenuitem;
    }

    private void createPopupMenu()
    {
        m_popup = new JPopupMenu();
        m_popup.setBorder(BorderFactory.createBevelBorder(0));
        m_popup.add(getMenuItem("Cut"));
        m_popup.add(getMenuItem("Copy"));
        m_popup.add(getMenuItem("Paste"));
        m_popup.add(getMenuItem("Paste Special..."));
        m_popup.add(new javax.swing.JPopupMenu.Separator());
        m_popup.add(getMenuItem("Insert..."));
        m_popup.add(getMenuItem("Delete..."));
        m_popup.add(getMenuItem("Clear Contents"));
        m_popup.add(new javax.swing.JPopupMenu.Separator());
        m_popup.add(getMenuItem("Format Cells..."));
    }

    public void showPopupMenu(int x, int y)
    {
        if(m_popup == null)
            createPopupMenu();
        m_popup.show(m_view, x, y);
        m_popup.addPopupMenuListener(this);
    }

    public void popupMenuCanceled(PopupMenuEvent e)
    {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    {
        clearPopup();
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    {
    }

    public void clearPopup()
    {
        if(m_popup != null)
        {
            m_popup.removePopupMenuListener(this);
            m_popup = null;
        }
    }
}
