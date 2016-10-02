package com.jxcell.tools;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;


public class FileChooser extends JFileChooser
    implements KeyListener
{

    private boolean save;
    private boolean supportfile;

    public FileChooser(File file, boolean bsave)
    {
        super(file);
        save = bsave;
        addListener(this);
    }

    public void addFilter(MyFileFilter fileFilter)
    {
        addChoosableFileFilter(fileFilter);
    }

    public void approveSelection()
    {
        if(save)
        {
            File file = getFilterSelectedFile();
            if(file != null && file.exists())
            {
                Toolkit.getDefaultToolkit().beep();
                int i = JOptionPane.showConfirmDialog(this, "The file already exists.  Do you want to replace it?", "Save", 0, 3);
                if(i != 0)
                    return;
            }
        }
        super.approveSelection();
    }

    public FileFilter getAcceptAllFileFilter()
    {
        return null;
    }

    public File getFilterSelectedFile()
    {
        File file = getSelectedFile();
        if(file != null)
        {
            String name = file.getName();
            if(name.endsWith("."))
            {
                String path = file.getPath();
                file = new File(path.substring(0, path.length() - 1));
            } else
            {
                boolean bFile = false;
                MyFileFilter fileFilter = getMyFileFilter();
                int len = fileFilter.getLength();
                for(int i = 0; i < len; i++)
                {
                    if(!name.endsWith(fileFilter.getExt(i)))
                        continue;
                    bFile = true;
                    break;
                }

                if(!bFile)
                {
                    String path2 = file.getPath();
                    if(name.lastIndexOf(".") != -1)
                    {
                        if(supportfile)
                        {
                            path2 = path2.substring(0, path2.lastIndexOf("."));
                            path2 = path2 + getExt(0);
                        }
                    } else
                    {
                        path2 = path2 + getExt(0);
                    }
                    file = new File(path2);
                }
            }
        }
        return file;
    }

    public MyFileFilter getMyFileFilter()
    {
        return (MyFileFilter)getFileFilter();
    }

    public String getExt(int index)
    {
        return getMyFileFilter().getExt(index);
    }

    public short getFileType()
    {
        return getMyFileFilter().getFileType();
    }

    public void keyPressed(KeyEvent keyevent)
    {
        int keycode = keyevent.getKeyCode();
        if(keycode == 27)
        {
            cancelSelection();
            return;
        }
        if(keycode == 8 && !(keyevent.getSource() instanceof JTextComponent))
            changeToParentDirectory();
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    private void addListener(Component component)
    {
        component.addKeyListener(this);
        if(component instanceof Container)
        {
            Container container = (Container)component;
            int count = container.getComponentCount();
            for(int i = 0; i < count; i++)
                addListener(container.getComponent(i));

        }
    }

    public void setSelectedFile1(File file)
    {
        if(file != null)
        {
            String name = file.getName();
            supportfile = name.endsWith(".jxl") || name.endsWith(".xls")  || name.endsWith(".xlsx") || name.endsWith(".txt") || name.endsWith(".htm") || name.endsWith(".html");
        }
        setSelectedFile(file);
    }

    public void setFileFilters(short type)
    {
        FileFilter fileFilters[] = getChoosableFileFilters();
        for(int i = 0; i < fileFilters.length; i++)
        {
            if(!(fileFilters[i] instanceof MyFileFilter))
                continue;
            MyFileFilter filter1 = (MyFileFilter)fileFilters[i];
            if(filter1.getFileType() != type)
                continue;
            setFileFilter(filter1);
            break;
        }

        if(getFileFilter() == null && fileFilters.length > 0)
            setFileFilter(fileFilters[0]);
    }
}
