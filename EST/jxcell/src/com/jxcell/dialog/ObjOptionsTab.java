package com.jxcell.dialog;

import com.jxcell.GRObject;
import com.jxcell.ss.Book;
import com.jxcell.ss.Region;
import com.jxcell.ss.TArea;
import com.jxcell.tools.GridManager;
import com.jxcell.tools.JTextAreaEX;
import com.jxcell.util.CharBuffer;

import javax.swing.*;


class ObjOptionsTab extends TabBase
{

    private JTextField txttext;
    private JTextField txtcell;
    private JTextField txtitems;
    private JTextAreaEX txtURL;
    private JCheckBox chkassign;

    ObjOptionsTab(TabDialog tabDialog)
    {
        super(tabDialog);
        GridManager gridManager = new GridManager();
        getLock();
        try
        {
            GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
            short type = grobject.getType();
            if(type == GRObject.eButton || type == GRObject.eCheckBox)
            {
                txttext = new JTextField("", 20);
                gridManager.insert(this, this, new JLabel("Caption:"), 0, 0, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
                gridManager.insert(this, this, txttext, 0, 1, 1, 1, 1, 1, 0, 0, gridManager.getInsets(3), 2, 18);
            }
            if(type == GRObject.eCheckBox || type == GRObject.eDropDown)
            {
                txtcell = new JTextField("", 20);
                gridManager.insert(this, this, new JLabel("Cell link:"), 0, 3, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
                gridManager.insert(this, this, txtcell, 0, 4, 1, 1, 1, 1, 0, 0, gridManager.getInsets(0), 2, 18);
            }
            if(type == GRObject.eDropDown)
            {
                txtURL = new JTextAreaEX(2, 30);
                chkassign = getJCheckBox("Assign As Text;T", false);
                gridManager.insert(this, this, chkassign, 0, 2, 1, 1, 0, 0, 0, 0, gridManager.getInsets(3), 0, 18);
                gridManager.insert(this, this, new JLabel("Input range:"), 0, 5, 1, 1, 0, 0, 0, 0, gridManager.getInsets(1), 0, 18);
                gridManager.insert(this, this, txtURL, 0, 6, 1, 1, 1, 1, 0, 0, gridManager.getInsets(0), 2, 18);
            }
            if(type == GRObject.ePicture)
            {
                txtitems = new JTextField("", 20);
                gridManager.insert(this, this, new JLabel("URL of picture:"), 0, 7, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
                gridManager.insert(this, this, txtitems, 0, 8, 1, 1, 1, 1, 0, 0, gridManager.getInsets(0), 2, 18);
            }
            if(type == GRObject.eText)
            {
                txtitems = new JTextField("", 20);
                gridManager.insert(this, this, new JLabel("Text content:"), 0, 7, 1, 1, 0, 0, 0, 0, gridManager.getInsets(0), 0, 18);
                gridManager.insert(this, this, txtitems, 0, 8, 1, 1, 1, 1, 0, 0, gridManager.getInsets(0), 2, 18);
            }
        }
        finally
        {
            releaseLock();
        }
    }

    public void updateControls()
    {
        try
        {
            GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
            short type = grobject.getType();
            if(type == GRObject.eButton || type == GRObject.eCheckBox)
                txttext.setText(grobject.getText());
            if((type == GRObject.eCheckBox || type == GRObject.eDropDown) && grobject.getCellType() != 0)
            {
                CharBuffer charBuffer1 = clearText();
                getWorkBook();
                int i = grobject.getCellRow();
                int j = grobject.getCellCol();
                Book.formatColNr(j, charBuffer1);
                charBuffer1.append(i + 1);
                txtcell.setText(charBuffer1.toString());
            }
            if(type == GRObject.eDropDown)
            {
                chkassign.setSelected(grobject.getCellType() == 2);
            }
            if(type == GRObject.ePicture)
            {
                txtitems.setText(grobject.getImageURL());
            }
            if(type == GRObject.eText)
            {
                txtitems.setText(grobject.getText());
            }
        }
        catch(Throwable throwable)
        {
            showMessage(throwable);
        }
    }

    protected void setOptions()
        throws Throwable
    {
        GRObject grobject = new GRObject(getSelection().getDrawingSelection().getSelection(0));
        short type = grobject.getType();
        if(type == GRObject.eButton || type == GRObject.eCheckBox)
            grobject.setText(txttext.getText());
        if(type == GRObject.eDropDown || type == GRObject.eCheckBox)
            if(txtcell.getText().equals(""))
            {
                grobject.setCell((short)0, 0, 0);
            }
            else
            {
                this.m_comp = txtcell;
                Region region1 = getView().getRegion(txtcell.getText(), getWorkBook().getGroup().getBasicLocaleInfo(0x20000000));
                if(region1.getAreaCount() != 1)
                    showError((short)14);
                TArea trange1 = (TArea) region1.getAreaConst(0);
                grobject.setCell((short)(type != GRObject.eDropDown || !chkassign.isSelected() ? 1 : 2), trange1.getRow1(), trange1.getCol1());
            }
        if(type == GRObject.eDropDown)
        {
            String inputRange = txtURL.getText();
            Region region1 = getView().getRegion(inputRange, getWorkBook().getGroup().getBasicLocaleInfo(0x20000000));
            if(region1.getAreaCount() != 1)
                showError((short)14);
        }
        if(type == GRObject.ePicture)
        {
            String fileUrl = grobject.getImageURL();
            if(fileUrl == null || !fileUrl.equals(txtitems.getText()))
                grobject.setImageFileName(txtitems.getText());
        }
        if(type == GRObject.eText)
        {
            grobject.setText(txtitems.getText());
        }
    }
}
