package com.jxcell.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public abstract class SamplePanel extends JPanel
    implements MouseListener, MouseMotionListener
{

    Rectangle m_Rectangle;
    private Dimension dimension;
    private int m_col;
    private int m_row;
    private int m_nSelectedIndex;
    private int m_width;
    private int m_height;
    private int m_x;
    private int left;
    private int m_y;
    private int top;
    private int m_colWidth;
    private int m_rowheight;
    protected String m_StrAutomatic;
    protected String m_StrU;
    private int m_ncolorindex;
    private int m_nSelectIndex;
    private Rectangle m_RectSelected;
    private Component m_Component;
    ActionEvent event;
    Vector m_ActionListeners;

    public SamplePanel(int col, int row, int sel, int x1)
    {
        this(col, row, sel, sel, x1, x1, null, null);
    }

    public SamplePanel(int col, int row, int sel, int width, int height, int x1, int y1,
                       String autostr, String ustr)
    {
        m_Rectangle = new Rectangle();
        m_RectSelected = new Rectangle();
        m_Component = this;
        m_StrAutomatic = autostr == null || autostr.length() <= 0 ? null : autostr;
        m_StrU = ustr == null || ustr.length() <= 0 ? null : ustr;
        m_col = col;
        m_row = row;
        m_nSelectedIndex = sel;
        m_width = width;
        m_height = height;
        m_x = x1;
        left = x1 / 2;
        m_y = y1;
        top = y1 / 2;
        m_colWidth = width + x1;
        m_rowheight = height + y1;
        dimension = new Dimension(col * width + (col + 1) * x1, row * height + (row + 1) * y1);
        if(m_StrAutomatic != null)
            dimension.height += m_rowheight;
        if(m_StrU != null)
            dimension.height += m_rowheight;
        setOpaque(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public SamplePanel(int col, int row, int width, int height, int x1, int y1, String autostr,
                       String ustr)
    {
        this(col, row, col * row, width, height, x1, y1, autostr, ustr);
    }

    public void addActionListener(ActionListener actionlistener)
    {
        addActionListener(this, actionlistener, "chooserChanged");
    }

    public void addActionListener(ActionListener actionlistener, String command)
    {
        addActionListener(this, actionlistener, command);
    }

    public void addActionListener(Object obj, ActionListener actionlistener, String command)
    {
        if(m_ActionListeners == null)
        {
            m_ActionListeners = new Vector(1);
            event = new ActionEvent(obj, 1001, command);
        }
        m_ActionListeners.addElement(actionlistener);
    }

    private void postEvent()
    {
        if(m_ActionListeners != null && m_ActionListeners.size() > 0)
        {
            for(int i = 0; i < m_ActionListeners.size(); i++)
                if(m_ActionListeners.elementAt(i) != null)
                    ((ActionListener)m_ActionListeners.elementAt(i)).actionPerformed(event);
        }
    }

    private Rectangle getRectangle(int index)
    {
        int i = getSelectedIndex();
        if(index < 0 || index >= i)
            return null;
        if(index == 0 && m_StrAutomatic != null)
        {
            m_RectSelected.setBounds(m_x, m_y, m_col * m_colWidth - m_x, m_height);
        } else
        {
            int height1 = 0;
            if(m_StrAutomatic != null)
                height1 = m_rowheight;
            if(index >= i - 1 && m_StrU != null)
            {
                m_RectSelected.setBounds(m_x, height1 + m_y + m_row * m_colWidth, m_col * m_colWidth - m_x, m_height);
            } else
            {
                if(m_StrAutomatic != null)
                    index--;
                m_RectSelected.setBounds(m_x + (index % m_col) * m_colWidth, height1 + m_y + (index / m_col) * m_rowheight, m_width, m_height);
            }
        }
        return m_RectSelected;
    }

    protected int intest(int x, int y)
    {
        int index = getSelectedIndex();
        for(int i = 0; i < index; i++)
            if(getRectangle(i).contains(x, y))
                return i;
        return -1;
    }

    public Dimension getMinimumSize()
    {
        return dimension;
    }

    protected int getSelectedIndex()
    {
        int i = m_nSelectedIndex;
        if(m_StrAutomatic != null)
            i++;
        if(m_StrU != null)
            i++;
        return i;
    }

    public int getUnitWidth()
    {
        int i = 40;
        if(m_StrAutomatic != null || m_StrU != null)
        {
            FontMetrics fontmetrics = getFontMetrics(getFont());
            if(m_StrAutomatic != null)
                i = Math.max(i, fontmetrics.stringWidth(m_StrAutomatic) + 20);
            if(m_StrU != null)
                i = Math.max(i, fontmetrics.stringWidth(m_StrU) + 20);
        }
        if(m_col == 1)
            i = Math.max(i, dimension.width);
        return i;
    }

    public Dimension getPreferredSize()
    {
        return dimension;
    }

    public int getSelectedColorIndex()
    {
        return m_ncolorindex;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            m_Component = mouseevent.getComponent();
            setSelected(m_nSelectIndex, false);
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            m_Component = mouseevent.getComponent();
            int i1 = intest(mouseevent.getX(), mouseevent.getY());
            if(i1 != -1)
                setSelected(i1, false);
        }
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            m_Component = mouseevent.getComponent();
            if(m_Component == this)
                requestFocus();
            int i = intest(mouseevent.getX(), mouseevent.getY());
            if(i != -1)
            {
                if(i != m_nSelectIndex)
                {
                    int j = m_nSelectIndex;
                    m_nSelectIndex = i;
                    paintUnit(j);
                }
                if(i != m_ncolorindex)
                {
                    int j = m_ncolorindex;
                    m_ncolorindex = i;
                    paintUnit(j);
                }
                paintUnit(i);
            }
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(isEnabled())
        {
            m_Component = mouseevent.getComponent();
            int i = intest(mouseevent.getX(), mouseevent.getY());
            if(i == -1)
            {
                setSelected(m_nSelectIndex, false);
                return;
            }
            if(i != m_nSelectIndex)
            {
                int j = m_nSelectIndex;
                m_nSelectIndex = i;
                paintUnit(j);
            }
            setSelected(i, true);
        }
    }

    public void paint(Graphics g1)
    {
        super.paint(g1);
        Rectangle rectangle = g1.getClipBounds();
        int height1 = 0;
        if(m_StrAutomatic != null)
        {
            height1 = m_rowheight;
            Rectangle rectangle1 = getRectangle(0);
            if(rectangle.intersects(rectangle1))
            {
                draw(g1, m_StrAutomatic, rectangle1, true);
            }
        }
        int palindex = 0;
        int len = 0;
        for(int y = height1 + m_y; len < m_row; y += m_rowheight)
        {
            int i2 = 0;
            int x = m_x;
            while(i2 < m_col)
            {
                m_Rectangle.setBounds(x - 1, y - 1, m_width + 2, m_height + 2);
                if(palindex < m_nSelectedIndex && m_Rectangle.intersects(rectangle))
                    draw(g1, palindex, x, y, m_width, m_height);
                i2++;
                x += m_colWidth;
                palindex++;
            }
            len++;
        }

        if(m_StrU != null)
        {
            Rectangle rectangle2 = getRectangle(getSelectedIndex() - 1);
            if(rectangle.intersects(rectangle2))
            {
                draw(g1, m_StrU, rectangle2, true);
            }
        }
        if(m_ncolorindex >= 0)
        {
            Rectangle rectangle3 = getRectangle(m_ncolorindex);
            if(rectangle3 != null)
            {
                rectangle3.x -= left;
                rectangle3.y -= top;
                rectangle3.width += m_x;
                rectangle3.height += m_y;
                if(rectangle.intersects(rectangle3))
                {
                    g1.setColor(getBackground());
                    g1.draw3DRect(rectangle3.x, rectangle3.y, rectangle3.width - 1, rectangle3.height - 1, m_nSelectIndex != m_ncolorindex);
                }
            }
        }
        if(m_nSelectIndex != m_ncolorindex && m_nSelectIndex >= 0)
        {
            Rectangle rectangle4 = getRectangle(m_nSelectIndex);
            if(rectangle4 != null)
            {
                rectangle4.x -= left;
                rectangle4.y -= top;
                rectangle4.width += m_x;
                rectangle4.height += m_y;
                if(rectangle.intersects(rectangle4))
                {
                    g1.setColor(getBackground());
                    g1.draw3DRect(rectangle4.x, rectangle4.y, rectangle4.width - 1, rectangle4.height - 1, false);
                }
            }
        }
    }

    public abstract void draw(Graphics g1, int palindex, int x, int y, int width, int height);

    private void draw(Graphics g1, String s1, Rectangle rectangle, boolean flag)
    {
        if(flag)
        {
            g1.setColor(SystemColor.controlShadow);
            g1.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        g1.setColor(isEnabled() ? SystemColor.controlText : SystemColor.textInactiveText);
        g1.setFont(getFont());
        FontMetrics fontmetrics = g1.getFontMetrics();
        g1.drawString(s1, rectangle.x + (rectangle.width - fontmetrics.stringWidth(s1)) / 2, (rectangle.y + (rectangle.height + fontmetrics.getHeight()) / 2) - fontmetrics.getDescent());
    }

    public void draw(Graphics g1, int x, int y, int width, int height)
    {
        if(m_StrAutomatic != null && m_ncolorindex == 0)
        {
            m_RectSelected.setBounds(x, y, width, height);
            draw(g1, m_StrAutomatic, m_RectSelected, false);
            return;
        }
        if(m_StrU != null && m_ncolorindex >= getSelectedIndex() - 1)
        {
            m_RectSelected.setBounds(x, y, width, height);
            draw(g1, m_StrU, m_RectSelected, false);
            return;
        }
        int palindex = m_ncolorindex;
        if(m_StrAutomatic != null)
            palindex--;
        if(palindex >= 0 && palindex < m_nSelectedIndex)
            draw(g1, palindex, x, y, width, height);
    }

    public void removeActionListener(ActionListener actionlistener)
    {
        m_ActionListeners.removeElement(actionlistener);
    }

    private void paintUnit(int i)
    {
        if(i >= 0)
        {
            Rectangle rectangle = getRectangle(i);
            if(rectangle != null)
                m_Component.repaint(0L, rectangle.x - left, rectangle.y - top, rectangle.width + m_x, rectangle.height + m_y);
        }
    }

    public void setSelected(int i, boolean event)
    {
        if(i != m_ncolorindex)
        {
            int j1 = m_ncolorindex;
            m_ncolorindex = i;
            if(j1 >= 0)
                paintUnit(j1);
            paintUnit(i);
        }
        if(event)
            postEvent();
    }

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        repaint();
    }

    public void setSelectedIndex(int i)
    {
        m_nSelectIndex = i;
        setSelected(i, false);
    }
}
