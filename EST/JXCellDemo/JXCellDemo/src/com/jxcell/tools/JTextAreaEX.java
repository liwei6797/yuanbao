package com.jxcell.tools;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.EventListener;

public class JTextAreaEX extends JScrollPane
{

    private JTextArea textArea;

    public JTextAreaEX(int row, int col)
    {
        textArea = new JTextArea(row, col)
        {
            public void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        textArea.setLineWrap(true);
        textArea.setFont(new Font("SansSerif", 0, 11));
        setViewportView(textArea);
    }

    public void addListener(EventListener eventlistener)
    {
        textArea.getDocument().addDocumentListener((DocumentListener)eventlistener);
    }

    public String getText()
    {
        return textArea.getText();
    }

    public JTextArea getJTextArea()
    {
        return textArea;
    }

    public void addDocumentListener(EventListener eventlistener)
    {
        textArea.getDocument().removeDocumentListener((DocumentListener)eventlistener);
    }

    public void requestFocus()
    {
        textArea.requestFocus();
    }

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        textArea.setEnabled(enabled);
    }

    public void setText(String text)
    {
        textArea.setText(text);
    }
}
