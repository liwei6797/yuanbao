package com.jxcell.tools;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MyFileFilter extends FileFilter
{

    private String files[];
    private String discription;
    private short filetype;

    public MyFileFilter(String exts, String discript, short type)
    {
        files = new String[] { exts };
        int len = files.length;
        if(len > 0)
        {
            discript = discript + " (";
            for(int j = 0; j < len; j++)
            {
                discript = discript + "*." + files[j];
                if(j < len - 1)
                    discript = discript + "; ";
            }

            discript = discript + ")";
        }
        discription = discript;
        filetype = type;
    }

    public MyFileFilter(String[] exts, String discript, short type)
    {
        files = exts;
        int len = files.length;
        if(len > 0)
        {
            discript = discript + " (";
            for(int j = 0; j < len; j++)
            {
                discript = discript + "*." + files[j];
                if(j < len - 1)
                    discript = discript + "; ";
            }

            discript = discript + ")";
        }
        discription = discript;
        filetype = type;
    }

    public boolean accept(File file)
    {
        return file != null && (file.isDirectory() || isAcceptFile(getFileExt(file)));
    }

    public String getDescription()
    {
        return discription;
    }

    public String getExt(int index)
    {
        return "." + files[index];
    }

    private String getFileExt(File file)
    {
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if(i > 0 && i < name.length() - 1)
            return name.substring(i + 1).toLowerCase();
        else
            return null;
    }

    public int getLength()
    {
        return files.length;
    }

    public short getFileType()
    {
        return filetype;
    }

    private boolean isAcceptFile(String fileExt)
    {
        boolean hasExt = fileExt != null;
        for(int i = 0; i < files.length; i++)
        {
            String ext = files[i];
            if(ext.equals("*") || hasExt && ext.equalsIgnoreCase(fileExt))
                return true;
        }
        return false;
    }
}
