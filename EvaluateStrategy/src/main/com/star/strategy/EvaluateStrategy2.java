package com.star.strategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.sql2o.data.Row;
import org.sql2o.data.Table;

import com.star.bean.Summary;
import com.star.db.H2Utils;
import com.star.db.SqlDao;

import est.commons.utils.SpringContext;

public class EvaluateStrategy2 {
    private static Logger LOGGER = Logger.getLogger(EvaluateStrategy2.class);

    public static void run2(String fileName, String outFile) throws Exception {
        // 导入CSV到数据库
        String tblCSV = ("CSV" + UUID.randomUUID()).replace("-", "");
        String tblBuyDayAvgProfit = ("AvgProfit" + UUID.randomUUID()).replace(
                "-", "");
        String tblYearRatio = ("YearRatio" + UUID.randomUUID())
                .replace("-", ""); 
        
        LOGGER.info("导入CSV");
        EsService.importCSV(fileName, tblCSV);

        // 结果存到表buyDayAvgProfit
        String sql = "create table " + tblBuyDayAvgProfit + " as "
                + " select buydate,sum(profit)/count(profit) as avgprofit "
                + " from " + tblCSV + " group by buydate order by buydate";
        SqlDao.executeSql(sql);

        LOGGER.info("开始输出：" + outFile);
        // 写buyDayAvgProfit的csv
        sql = "CALL CSVWRITE('" + outFile + "', 'SELECT * FROM "
                + tblBuyDayAvgProfit + "')";
        SqlDao.executeSql(sql);
        LOGGER.info("输出完毕：" + outFile);

        // 为了方便，插入一条假记录
        sql = "insert into " + tblBuyDayAvgProfit
                + "(buydate,avgprofit) values(20990101,0)";
        SqlDao.executeSql(sql);
        Table dtBuyDayAvgProfit = SqlDao
                .ExecuteTable("select buydate, avgprofit " + "from "
                        + tblBuyDayAvgProfit + " order by buydate");

        // 创建表yearRatio(year,positive,totalcount)

        sql = "create table " + tblYearRatio
                + " as select buydate/10000 year, "
                + " sum(case when profit > 0 then 1 else 0 end) "
                + " positive, count(profit) totalcount from " + tblCSV
                + " group by buydate/10000";
        SqlDao.executeSql(sql);
        Table dtYearRatio = SqlDao
                .ExecuteTable("select * from " + tblYearRatio);
        Map<Long, Row> mapYearRatio = new HashMap<Long, Row>();
        for (Row r1 : dtYearRatio.rows()) {
            mapYearRatio.put(r1.getLong("year"), r1);
        }

        BigDecimal yearReturn = BigDecimal.ONE;
        BigDecimal totalReturn = BigDecimal.ONE;

        long preYear = dtBuyDayAvgProfit.rows().get(0).getInteger("buydate") / 10000;

        List<Summary> sumList = new ArrayList<Summary>();
        int pos = fileName.lastIndexOf("\\");
        String fName = fileName.substring(pos + 1);
        LOGGER.info("开始计算yearReturn");
        for (Row r : dtBuyDayAvgProfit.rows()) {
            long year = r.getInteger("buydate") / 10000;
            if (year != preYear) {

                // 插入表
                Summary s = new Summary();
                s.fileName = fName;
                s.year = preYear;
                s.yearReturn = yearReturn.doubleValue();
                s.totalReturn = totalReturn.doubleValue();

                Row r1 = mapYearRatio.get(s.year);
                s.positive = r1.getLong("positive");
                s.totalCount = r1.getLong("totalcount");
                s.ratio = (double) s.positive / s.totalCount;

                sumList.add(s);

                yearReturn = BigDecimal.ONE;
            }
            double avgP = r.getDouble("avgprofit");
            yearReturn = yearReturn.multiply(new BigDecimal(1 + avgP));
            totalReturn = totalReturn.multiply(new BigDecimal(1 + avgP));
            preYear = year;
        }
        LOGGER.info("计算完毕yearReturn");

        // 插入smmary表
        SqlDao.batchInsertWithId(sumList);

        EsService.dropTable(tblBuyDayAvgProfit);
        EsService.dropTable(tblYearRatio);
        EsService.dropTable(tblCSV);
    }

    // 矩阵翻转
    public static List<List<Object>> overturn(List<List<Object>> matrix) {
        List<List<Object>> overMatrix = new ArrayList<List<Object>>();
        int hsize = matrix.get(0).size();
        for (int i = 0; i < hsize; i++) {
            List<Object> l = new ArrayList<Object>();
            overMatrix.add(l);
        }

        for (List<Object> m : matrix) {
            for (int i = 0; i < hsize; i++) {
                List<Object> l = overMatrix.get(i);
                l.add(m.get(i));
            }
        }
        return overMatrix;
    }

    public static void main(String[] args) throws Exception {
        H2Utils.startServer();
        new SpringContext();

        try {
            // 创建统计表
            EsService.dropTable("Summary");
            String sql = "CREATE TABLE Summary (fileName varchar(255),year INT,"
                    + " positive INT,totalCount INT,ratio Double,"
                    + " yearreturn DOUBLE,totalreturn Double)";
            SqlDao.executeSql(sql);

            File root = new File("single");
            String outdir = root.getAbsolutePath() + "/eval";
            FileUtils.forceMkdir(new File(outdir));

            ExecutorService exe = Executors.newFixedThreadPool(10);

            for (final File f : root.listFiles()) {
                if (f.isDirectory())
                    continue;
                String evalName = f.getName();
                if (!evalName.toLowerCase().endsWith(".csv"))
                    continue;

                int pos = evalName.lastIndexOf(".");
                final String outName = root.getAbsolutePath() + "/eval/"
                        + evalName.substring(0, pos) + "-eval.csv";

                exe.execute(new Runnable() {

                    public void run() {
                        // EvaluateStrategy2 strategy = new EvaluateStrategy2();
                        try {
                            EvaluateStrategy2.run2(f.getAbsolutePath(), outName);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
           
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    System.out.println("结束了！");
                    break;
                }
                Thread.sleep(1000);
            }

            // 生成最终csv
            summaryCsv(outdir);
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
        H2Utils.stopServer();
    }

    // 生成最终csv
    protected static void summaryCsv(String outdir) throws Exception {
        String sql;
        Table dtYear = SqlDao
                .ExecuteTable("select distinct year from summary order by year");
        Table dtFile = SqlDao
                .ExecuteTable("select distinct filename from summary order by filename");
        Table dtSummary = SqlDao.ExecuteTable("select * from summary");
        Map<String, Row> mapSummary = new HashMap<String, Row>();
        for (Row rSummary : dtSummary.rows()) {
            String key = rSummary.getString("fileName") + "-"
                    + rSummary.getLong("year");
            mapSummary.put(key, rSummary);
        }

        PrintStream out = new PrintStream(new FileOutputStream(outdir
                + "/Summary.csv"), false, "GBK");
        // 第一行title
        StringBuilder sb = new StringBuilder(",");
        for (Row r : dtYear.rows()) {
            sb.append(r.getLong("year") + ",,,");
        }
        sb.append("Total,,");
        out.println(sb.toString());

        // 第二行Title
        sb = new StringBuilder(",");
        for (int i = 0; i < dtYear.rows().size(); i++) {
            sb.append("Count,Ratio,YearReturn,");
        }
        sb.append("TotalCount,Ratio,TotalReturn");
        out.println(sb.toString());

        // 计算total
        sql = "select filename, sum(positive) positive , sum(totalcount) totalcount"
                + " from summary group by filename";
        Table dtTotal = SqlDao.ExecuteTable(sql);
        Map<String, Row> mapTotal = new HashMap<String, Row>();
        for (Row r : dtTotal.rows()) {
            mapTotal.put(r.getString("filename"), r);
        }

        for (Row rFile : dtFile.rows()) {
            String fName = rFile.getString("fileName");
            out.print(fName + ",");

            for (Row rYear : dtYear.rows()) {
                String key = fName + "-" + rYear.getLong("year");
                Row rSummary = mapSummary.get(key);
                if (rSummary != null) {
                    out.print(rSummary.getLong("TotalCount") + ","
                            + rSummary.getDouble("ratio") + ","
                            + rSummary.getDouble("yearReturn") + ",");
                } else {
                    out.print(",,,");
                }
            }

            // 输出total
            Row rTotal = mapTotal.get(fName);
            Long positive = rTotal.getLong("positive");
            Long totalCount = rTotal.getLong("totalcount");
            double ratio = 0;
            if (totalCount != null && totalCount > 0) {
                ratio = (double) positive / totalCount;
            }
            // 查出最后一年的totalReturn
            sql = "select totalreturn from summary where filename = '" + fName
                    + "'" + " and year = (select max(year) from summary where "
                    + " filename='" + fName + "')";
            Double totalReturn = (Double) SqlDao.executeScalar(sql);
            out.println(totalCount + "," + ratio + "," + totalReturn);
        }
        out.close();
    }
}
