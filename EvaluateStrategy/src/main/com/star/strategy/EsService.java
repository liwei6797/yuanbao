package com.star.strategy;

import java.util.Date;

import org.apache.log4j.Logger;

import com.star.db.SqlDao;

public class EsService {
    private static Logger LOGGER = Logger.getLogger(EsService.class);

    // 导入CSV文件到H2数据库
    public static void importCSV(String filePath, String tableName) {
        Date t1 = new Date();

        String sql = "CREATE TABLE " + tableName + " (STOCKID INT,BUYDATE INT,"
                + "BUYPRICE INT,SELLDATE INT,SELLPRICE INT,PROFIT DOUBLE)";
        SqlDao.executeSql(sql);

        try {
            SqlDao.executeSql("INSERT INTO " + tableName
                    + " SELECT * FROM CSVREAD('" + filePath + "')");
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
        Date t2 = new Date();
        LOGGER.info("导入" + filePath + "耗时" + (t2.getTime() - t1.getTime())
                + "毫秒");
    }

    public static void dropTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        SqlDao.executeSql(sql);
    }
}
