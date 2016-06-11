package est.szefile.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用电信息电量表码
 * 
 * @author Li
 * 
 */
@Entity
@Table(name = "EST_DDY_SzdlData")
public class SzdlData {

    @Id
    @Column
    private Long id;

    @Column
    private Long jldbh;

    @Column
    private String yhbh;

    @Column
    private String jldh;

    @Column
    private String dbzcbh;

    @Column
    private Long areaCode;

    @Column
    private Date dataTime;

    @Column
    private Long ct;

    @Column
    private Long pt;

    @Column
    private Date updateTime;

    @Column
    private Double pap_r;

    @Column
    private Double prp_r;

    @Column
    private Double rap_r;

    @Column
    private Double rrp_r;

    public Long getAreaCode() {
        return areaCode;
    }

    public Long getCt() {
        return ct;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public String getDbzcbh() {
        return dbzcbh;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EST_DDY_SzdlData_seq")
    @SequenceGenerator(name = "EST_DDY_SzdlData_seq", sequenceName = "EST_DDY_SzdlData_seq", allocationSize = 1, initialValue = 100)   
    public Long getId() {
        return id;
    }

    public Long getJldbh() {
        return jldbh;
    }

    public String getJldh() {
        return jldh;
    }

    public Double getPap_r() {
        return pap_r;
    }

    public Double getPrp_r() {
        return prp_r;
    }

    public Long getPt() {
        return pt;
    }

    public Double getRap_r() {
        return rap_r;
    }

    public Double getRrp_r() {
        return rrp_r;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getYhbh() {
        return yhbh;
    }

    public void setAreaCode(Long areaCode) {
        this.areaCode = areaCode;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public void setDbzcbh(String dbzcbh) {
        this.dbzcbh = dbzcbh;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJldbh(Long jldbh) {
        this.jldbh = jldbh;
    }

    public void setJldh(String jldh) {
        this.jldh = jldh;
    }

    public void setPap_r(Double pap_r) {
        this.pap_r = pap_r;
    }

    public void setPrp_r(Double prp_r) {
        this.prp_r = prp_r;
    }

    public void setPt(Long pt) {
        this.pt = pt;
    }

    public void setRap_r(Double rap_r) {
        this.rap_r = rap_r;
    }

    public void setRrp_r(Double rrp_r) {
        this.rrp_r = rrp_r;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setYhbh(String yhbh) {
        this.yhbh = yhbh;
    }
}
