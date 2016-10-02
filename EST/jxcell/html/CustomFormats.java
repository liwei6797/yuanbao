
public class CustomFormats
{
    CustomFormat m_formats[];

    public CustomFormats()
    {
        m_formats = new CustomFormat[0];
    }

    public void addCustomFormat(CustomFormat format)
    {
        int length = m_formats.length;
        CustomFormat[] m_formats1 = new CustomFormat[length];
        System.arraycopy(m_formats, 0, m_formats1, 0, length);
        m_formats = new CustomFormat[length+1];
        System.arraycopy(m_formats1, 0, m_formats, 0, length);
        m_formats[length] = format;
    }

    public CustomFormat getCustomFormat(int i)
    {
        return m_formats[i];
    }

    public String toString()
    {
        String formats = "";
        for(int i = 0; i < m_formats.length; i++)
        {
            CustomFormat format = m_formats[i];
            formats += format.toString();
        }
        return formats;
    }

    public int indexOf(CustomFormat customFormat)
    {
        for(int i = 0; i < m_formats.length; i++)
        {
            CustomFormat format = m_formats[i];
            if(format.Equals(customFormat))
                return i;
        }
        return -1;
    }
}
