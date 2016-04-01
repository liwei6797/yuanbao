package com.star.bean;

@javax.persistence.Table(name = "Summary")
public class Summary {
    public String fileName;
    public Long year;
    public Long positive;
    public Long totalCount;
    public Double ratio;
    public Double yearReturn;
    public Double totalReturn;

    public String getFileName() {
        return fileName;
    }

    public Long getPositive() {
        return positive;
    }

    public Double getRatio() {
        return ratio;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Double getTotalReturn() {
        return totalReturn;
    }

    public Long getYear() {
        return year;
    }

    public Double getYearReturn() {
        return yearReturn;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPositive(Long positive) {
        this.positive = positive;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalReturn(Double totalReturn) {
        this.totalReturn = totalReturn;
    }

    public void setYear(Long year) {
        this.year = year;
    }
    public void setYearReturn(Double yearReturn) {
        this.yearReturn = yearReturn;
    }
}
