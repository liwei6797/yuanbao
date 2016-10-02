package com.jxcell;

import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.Serializable;
import java.net.URL;


public class JBookApplet extends JApplet
    implements Serializable,java.awt.event.ActionListener, ObjectListener
{

    public static final long serialVersionUID = 1L;
    private View m_workbook;
    private static final String m_parameterInfo[][] =
    {
        {
            "GROUP", "String", "Name of workbook group to join"
        }, {
            "WORKBOOKNAME", "String", "Name of workbook"
        }, {
            "WORKBOOK", "URL", "URL from which to load workbook"
        }
    };

    private netscape.javascript.JSObject m_jsobj;

    public JBookApplet()
    {
        getContentPane().add(m_workbook = new View());
    }

    public void destroy()
    {
        m_workbook.destroy();
    }

    public View getView()
    {
        return m_workbook;
    }

    public String[][] getParameterInfo()
    {
        return m_parameterInfo;
    }

   public void actionPerformed(java.awt.event.ActionEvent event)
   {
       try
       {
           m_jsobj.eval(new StringBuffer().append("Events(\"").append(event.getActionCommand()).append("\")").toString());
       }
       catch (Exception e2)
       {
           System.out.println(e2);
       }
  }
    public void init()
    {
        JSObject m_jsdoc;
        JSObject m_jsbody;
        try
        {
            m_jsobj = netscape.javascript.JSObject.getWindow(this);
            m_jsobj = (netscape.javascript.JSObject) m_jsobj.getMember("top");
            m_jsdoc = (netscape.javascript.JSObject) m_jsobj.getMember("document");
            m_jsbody = (netscape.javascript.JSObject) m_jsdoc.getMember("body");
//            if (m_jsbody.getMember("onload").toString().compareTo("undefined") != 0)
//            {
//                System.out.println("Onload: !null");
//            }
        }
      catch (Exception e)
      {
          System.out.println(e);
      }

        m_workbook.getLock();
        try
        {
            String groupName = getParameter("GROUP");
            if(groupName != null)
                try
                {
                    m_workbook.setGroup(groupName);
                }
                catch(Throwable throwable) { }
            boolean linkBook = false;
            String workbookname = getParameter("WORKBOOKNAME");
            if(workbookname != null)
                try
                {
                    m_workbook.attach(workbookname);
                    linkBook = true;
                }
                catch(Throwable _ex)
                {
                    m_workbook.setWorkbookName(workbookname);
                }
            if(!linkBook)
            {
                String workbook1 = getParameter("WORKBOOK");
                if(workbook1 != null)
                    try
                    {
                        m_workbook.readURL(new URL(getDocumentBase(), workbook1));
                    }
                    catch(Throwable throwable1)
                    {
                        System.out.println("com.jxcell.JBookApplet.init() got exception [" + throwable1 + "] WORKBOOK=" + workbook1);
                        try
                        {
                            m_workbook.readURL(new URL(getCodeBase(), workbook1));
                        }
                        catch(Throwable throwable2)
                        {
                            System.out.println("com.jxcell.JBookApplet.init() got exception [" + throwable2 + "] WORKBOOK=" + workbook1);
                            try
                            {
                                m_workbook.initWorkbook();
                            }
                            catch(Throwable _ex) { }
                        }
                    }
            }
            m_workbook.addObjectListener(this);
        }
        finally
        {
            m_workbook.releaseLock();
        }
        super.init();
    }

    public void objectClicked(ObjectEvent objectevent)
    {
        try
        {
            m_jsobj.eval(new StringBuffer().append("Events(\"").append(objectevent.getObject().getName()).append("\")").toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void objectDblClicked(ObjectEvent objectevent)
    {
        try
        {
            m_jsobj.eval(new StringBuffer().append("Events(\"").append(objectevent.getObject().getName()).append("\")").toString());
        }
        catch (Exception e2)
        {
            System.out.println(e2);
        }
    }

    public void objectGotFocus(ObjectEvent objectevent)
    {
    }

    public void objectLostFocus(ObjectEvent objectevent)
    {
    }

    public void objectValueChanged(ObjectEvent objectevent)
    {
        try
        {
            m_jsobj.eval(new StringBuffer().append("Events(\"").append(objectevent.getObject().getName()).append("\")").toString());
        }
        catch (Exception e2)
        {
            System.out.println(e2);
        }
    }
}
