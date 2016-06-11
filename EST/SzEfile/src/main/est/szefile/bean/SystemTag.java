package est.szefile.bean;

import java.util.Date;

/**
 * 临时用的bean
 * 
 * @author Li
 * 
 */
public class SystemTag {
    private String area;// 地区
    private String systemName;// 系统
    private Date dataTime;// 数据生成时间

    public String getArea() {
        return area;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

}
