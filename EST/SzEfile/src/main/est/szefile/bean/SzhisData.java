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
 * 配变15粒度的相关数据，包括三相电压、三相电流、总有功、总无功容量等数据 按日分区
 * 
 * @author 殷闯
 * @version 1.0
 * @created 17-12月-2013 15:01:22
 */
@Entity
@Table(name = "EST_DDY_SZHISDATA")
public class SzhisData {

    private Long areaCode;

    /**
     * 流水号
     */
    @Column(nullable = false)
    private Long id;

    // 计量点编号
    @Column
    private Long jldbh;

    // 用户编号
    @Column
    private String yhbh;

    // 计量点号
    @Column
    private String jldh;

    // 电表资产编号
    @Column
    private String dbzcbh;

    /**
     * 数据时间，yyyy-mm-dd hh24:mi, mi%15=0
     */
    @Column(nullable = false)
    private Date dataTime;
    /**
     * A相电流，A
     */
    @Column
    private Double ia;
    /**
     * B相电流，A
     */
    @Column
    private Double ib;
    /**
     * C相电流，A
     */
    @Column
    private Double ic;
    /**
     * A相电压，V
     */
    @Column
    private Double ua;
    /**
     * B相电压，V
     */
    @Column
    private Double ub;
    /**
     * C相电压，V
     */
    @Column
    private Double uc;
    private Double q;
    /**
     * 无功功率，单位var
     */
    @Column
    private Double qa;
    @Column
    private Double qb;
    @Column
    private Double qc;
    @Column
    private Double p;
    /**
     * 有功功率，W
     */
    @Column
    private Double pa;
    @Column
    private Double pc;
    @Column
    private Double pb;
    @Column
    private Double pf;
    /**
     * 功率因数
     */
    @Column
    private Double pfa;
    @Column
    private Double pfb;
    @Column
    private Double pfc;
    /**
     * pt
     */
    @Column
    private Double pt;
    /**
     * CT
     */
    @Column
    private Double ct;

    @Column
    private Double s;

    @Column
    private Double sa;

    @Column
    private Double sb;

    @Column
    private Double sc;

    public Long getAreaCode() {
        return areaCode;
    }

    public Double getCt() {
        return ct;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public String getDbzcbh() {
        return dbzcbh;
    }

    public Double getIa() {
        return ia;
    }

    public Double getIb() {
        return ib;
    }

    public Double getIc() {
        return ic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EST_DDY_SZHISDATA_SEQ")
    @SequenceGenerator(name = "EST_DDY_SZHISDATA_SEQ", sequenceName = "EST_DDY_SZHISDATA_SEQ", allocationSize = 1, initialValue = 100)
    public Long getId() {
        return id;
    }

    public Long getJldbh() {
        return jldbh;
    }

    public String getJldh() {
        return jldh;
    }

    public Double getP() {
        return p;
    }

    public Double getPa() {
        return pa;
    }

    public Double getPb() {
        return pb;
    }

    public Double getPc() {
        return pc;
    }

    public Double getPf() {
        return pf;
    }

    public Double getPfa() {
        return pfa;
    }

    public Double getPfb() {
        return pfb;
    }

    public Double getPfc() {
        return pfc;
    }

    public Double getPt() {
        return pt;
    }

    public Double getQ() {
        return q;
    }

    public Double getQa() {
        return qa;
    }

    public Double getQb() {
        return qb;
    }

    public Double getQc() {
        return qc;
    }

    public Double getS() {
        return s;
    }

    public Double getSa() {
        return sa;
    }

    public Double getSb() {
        return sb;
    }

    public Double getSc() {
        return sc;
    }

    public Double getUa() {
        return ua;
    }

    public Double getUb() {
        return ub;
    }

    public Double getUc() {
        return uc;
    }

    public String getYhbh() {
        return yhbh;
    }

    public void setAreaCode(Long areaCode) {
        this.areaCode = areaCode;
    }

    public void setCt(Double ct) {
        this.ct = ct;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public void setDbzcbh(String dbzcbh) {
        this.dbzcbh = dbzcbh;
    }

    public void setIa(Double ia) {
        this.ia = ia;
    }

    public void setIb(Double ib) {
        this.ib = ib;
    }

    public void setIc(Double ic) {
        this.ic = ic;
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

    public void setP(Double p) {
        this.p = p;
    }

    public void setPa(Double pa) {
        this.pa = pa;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    public void setPc(Double pc) {
        this.pc = pc;
    }

    public void setPf(Double pf) {
        this.pf = pf;
    }

    public void setPfa(Double pfa) {
        this.pfa = pfa;
    }

    public void setPfb(Double pfb) {
        this.pfb = pfb;
    }

    public void setPfc(Double pfc) {
        this.pfc = pfc;
    }

    public void setPt(Double pt) {
        this.pt = pt;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public void setQa(Double qa) {
        this.qa = qa;
    }

    public void setQb(Double qb) {
        this.qb = qb;
    }

    public void setQc(Double qc) {
        this.qc = qc;
    }

    public void setS(Double s) {
        this.s = s;
    }

    public void setSa(Double sa) {
        this.sa = sa;
    }

    public void setSb(Double sb) {
        this.sb = sb;
    }

    public void setSc(Double sc) {
        this.sc = sc;
    }

    public void setUa(Double ua) {
        this.ua = ua;
    }

    public void setUb(Double ub) {
        this.ub = ub;
    }

    public void setUc(Double uc) {
        this.uc = uc;
    }

    public void setYhbh(String yhbh) {
        this.yhbh = yhbh;
    }

}