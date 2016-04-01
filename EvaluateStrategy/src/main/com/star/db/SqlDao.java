package com.star.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.StatementRunnable;

import com.google.common.base.Joiner;

import est.commons.configuration.EstConfiguration;
import est.commons.dao.Json;
import est.commons.utils.SpringContext;

@Component
public class SqlDao {
    private static Logger LOGGER = Logger.getLogger(SqlDao.class);
    private static Sql2o sql2o;
    private static EstConfiguration estConfig;
    public static int BATCH_SIZE = 5000;// 数据库jdbc批量操作分解，默认一次处理5000条

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EstConfiguration config;

    public static ThreadLocal<Connection> connThreadLocal = new ThreadLocal<Connection>();

    /**
     * 批量插入（非事务）。条件:1.有Table注解name属性。2.
     * id字段或getId方法上有SequenceGenerator注解的sequenceName属性。
     * 
     * @param newObjs
     */
    public static void batchInsert(Connection conn, Object... newObjs) {
        if (newObjs != null && newObjs.length > 0) {
            if (newObjs.length == 1 && newObjs[0] instanceof Collection) {
                newObjs = ((Collection<?>) newObjs[0]).toArray();
            }
            if (newObjs.length == 0) {
                LOGGER.info("batchInsert:无数据需插入");
                return;
            }
            final String sql = buildInsertSql(newObjs[0]);
            executeBatch(conn, sql, newObjs);
            LOGGER.info("batchInsert:已插入" + newObjs.length + "条数据。");
        } else {
            LOGGER.info("batchInsert:无数据需插入");
        }
    }

    /**
     * 批量插入(事务)。条件:1.有Table注解name属性。2.
     * id字段或getId方法上有SequenceGenerator注解的sequenceName属性。
     * 
     * @param newObjs
     */
    public static void batchInsert(Object... newObjs) {
        // 事务
        batchInsert(null, newObjs);
    }

    /**
     * 批量插入,人工生成Id（非事务）。条件:1.有Table注解name属性。
     * 
     * @param newObjs
     */
    public static void batchInsertWithId(Connection conn, Object... newObjs) {
        if (newObjs != null && newObjs.length > 0) {
            if (newObjs.length == 1 && newObjs[0] instanceof Collection) {
                newObjs = ((Collection<?>) newObjs[0]).toArray();
            }
            if (newObjs.length == 0) {
                LOGGER.info("batchInsert:无数据需插入");
                return;
            }
            final String sql = buildInsertWithIdSql(newObjs[0]);
            executeBatch(conn, sql, newObjs);
            LOGGER.info("batchInsertWithId:已插入" + newObjs.length + "条数据。");
        } else {
            LOGGER.info("batchInsert:无数据需插入");
        }
    }

    /**
     * 批量插入,人工生成Id(事务)。条件:1.有Table注解name属性。
     * 
     * @param newObjs
     */
    public static void batchInsertWithId(Object... newObjs) {
        batchInsertWithId(null, newObjs);
    }

    /**
     * condFields 要跟类中的字段名保持一致，首字母小写。
     * 
     * @param conn
     * @param condFields
     * @param newObjs
     */
    public static void batchMergeInto(Connection conn, String[] condFields,
            Object... newObjs) {
        if (newObjs != null && newObjs.length > 0) {
            if (newObjs.length == 1 && newObjs[0] instanceof Collection) {
                newObjs = ((Collection<?>) newObjs[0]).toArray();
            }
            if (newObjs.length == 0) {
                LOGGER.info("batchMergeInto:无数据需MergeInto");
                return;
            }
            final String sql = buildMergeIntoSql(newObjs[0], condFields);
            executeBatch(conn, sql, newObjs);
            LOGGER.info("condFields,:已 MergeInto " + newObjs.length + "条数据。");
        } else {
            LOGGER.info("condFields,:无数据需MergeInto");
        }
    }

    public static void batchMergeInto(String[] condFields, Object... newObjs) {
        // 事务
        batchMergeInto(null, condFields, newObjs);
    }

    /**
     * 批量更新(非事务)。条件:1.有Table注解name属性。2.主键名为“id”
     * 
     * @param newObjs
     */
    public static void batchUpdateById(Connection conn, Object... newObjs) {
        if (newObjs != null && newObjs.length > 0) {
            if (newObjs.length == 1 && newObjs[0] instanceof Collection) {
                newObjs = ((Collection<?>) newObjs[0]).toArray();
            }
            if (newObjs.length == 0) {
                LOGGER.info("batchUpdateById:无数据需更新");
                return;
            }
            final String sql = buildUpdateByIdSql(newObjs[0]);
            executeBatch(conn, sql, newObjs);
            LOGGER.info("batchUpdateById:已更新" + newObjs.length + "条数据。");
        } else {
            LOGGER.info("batchUpdateById:无数据需更新");
        }
    }

    /**
     * 批量更新(事务)。条件:1.有Table注解name属性。2.主键名为“id”
     * 
     * @param newObjs
     */
    public static void batchUpdateById(Object... newObjs) {
        batchUpdateById(null, newObjs);
    }

    /**
     * 构建插入语句
     * 
     * @param obj
     * @return
     */
    public static String buildInsertSql(Object obj) {
        Table t = obj.getClass().getAnnotation(Table.class);
        String tableName = t.name();
        String seqName = getSequenceName(obj);
        Json j = new Json(obj);
        removeTransient(obj, j);
        String insertSql = "INSERT INTO " + tableName;
        insertSql += "(" + Joiner.on(",").join(j.keySet()) + ") ";
        insertSql += "VALUES(:" + Joiner.on(",:").join(j.keySet()) + ")";
        final String sql = insertSql.replace(":id", seqName + ".Nextval");
        System.out.println(sql);
        return sql;
    }

    /**
     * 构建插入语句,tmp表专用
     * 
     * @param obj
     * @return
     */
    public static String buildInsertSql_tmp(Object obj) {
        Table t = obj.getClass().getAnnotation(Table.class);
        String tableName = t.name() + "_tmp";
        String seqName = getSequenceName(obj);
        Json j = new Json(obj);
        removeTransient(obj, j);
        String insertSql = "INSERT INTO " + tableName;
        insertSql += "(" + Joiner.on(",").join(j.keySet()) + ") ";
        insertSql += "VALUES(:" + Joiner.on(",:").join(j.keySet()) + ")";
        final String sql = insertSql.replace(":id", seqName + ".Nextval");
        System.out.println(sql);
        return sql;
    }

    private static void removeTransient(Object obj, Json j) {
        Field[] fs = obj.getClass().getDeclaredFields();
        for (Field f : fs) {
            Transient trt = f.getAnnotation(Transient.class);
            if (trt != null) {
                j.remove(f.getName());
            }
        }

        // 电压系统里面有些get方法是Transient，会被当成属性
        Method[] ms = obj.getClass().getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().startsWith("get")) {
                Transient trt = m.getAnnotation(Transient.class);
                if (trt != null) {
                    // 首字母
                    String c = m.getName().substring(3, 4);
                    // 首字母大小写都删掉
                    j.remove(c.toLowerCase() + m.getName().substring(4));
                    j.remove(c.toUpperCase() + m.getName().substring(4));
                }
            }
        }
    }

    /**
     * 构建插入语句
     * 
     * @param obj
     * @return
     */
    public static String buildInsertWithIdSql(Object obj) {
        Table t = obj.getClass().getAnnotation(Table.class);
        String tableName = t.name();
        Json j = new Json(obj);

        removeTransient(obj, j);

        String insertSql = "INSERT INTO " + tableName;
        insertSql += "(" + Joiner.on(",").join(j.keySet()) + ") ";
        insertSql += "VALUES(:" + Joiner.on(",:").join(j.keySet()) + ")";
        System.out.println(insertSql);
        return insertSql;
    }

    /**
     * 构建temp表专用
     * 
     * @param obj
     * @return
     */
    public static String buildInsertWithIdSql_tmp(Object obj) {
        Table t = obj.getClass().getAnnotation(Table.class);
        String tableName = t.name() + "_tmp";
        Json j = new Json(obj);
        removeTransient(obj, j);
        String insertSql = "INSERT INTO " + tableName;
        insertSql += "(" + Joiner.on(",").join(j.keySet()) + ") ";
        insertSql += "VALUES(:" + Joiner.on(",:").join(j.keySet()) + ")";
        System.out.println(insertSql);
        return insertSql;
    }

    /**
     * 构建MergeInto语句。 condFieldNames 要跟类中的字段名保持一致，首字母小写。
     * 
     * @param obj
     * @param condFieldNames
     * @return
     */
    public static String buildMergeIntoSql(Object obj, String... condFieldNames) {
        Table t = obj.getClass().getAnnotation(Table.class);
        String tableName = t.name();
        final StringBuffer buffer = new StringBuffer();
        buffer.append("MERGE INTO " + tableName + " T1 ");
        buffer.append(" USING (SELECT ");
        boolean first = true;
        for (String field : condFieldNames) {
            if (first) {
                first = false;
                buffer.append(" :" + field + " " + field);
            } else {
                buffer.append(", :" + field + " " + field);
            }
        }
        buffer.append(" FROM DUAL) T2 ");
        buffer.append(" ON ( ");
        buffer.append(" 1 = 1 ");
        for (String field : condFieldNames) {
            buffer.append(" AND T1." + field + " = T2." + field);
        }
        buffer.append(" ) ");

        buffer.append(" WHEN MATCHED THEN ");
        buffer.append(" UPDATE SET ");

        String updateSql = "";
        Json j = new Json(obj);
        removeTransient(obj, j);
        List<String> condFieldList = Arrays.asList(condFieldNames);
        for (String k : j.keySet()) {
            if (condFieldList.contains(k) == false) {// MergeInto语法：不能包含ON中的字段
                updateSql += "T1." + k + "=:" + k + ",";
            }
        }
        updateSql = updateSql.replace("T1.id=:id,", "");
        updateSql = updateSql.substring(0, updateSql.length() - 1);// 取掉最后的逗号
        buffer.append(updateSql);

        buffer.append(" WHEN NOT MATCHED THEN ");

        String seqName = getSequenceName(obj);
        String insertSql = " INSERT ";
        insertSql += "(" + Joiner.on(",").join(j.keySet()) + ") ";
        insertSql += "VALUES(:" + Joiner.on(",:").join(j.keySet()) + ")";
        insertSql = insertSql.replace(":id", seqName + ".NEXTVAL");
        buffer.append(insertSql);
        String sql = buffer.toString();
        LOGGER.info(sql);
        return sql;
    }

    /**
     * 构建更新语句
     * 
     * @param obj
     * @return
     */
    public static String buildUpdateByIdSql(Object obj) {
        Table t = obj.getClass().getAnnotation(Table.class);
        String tableName = t.name();
        String updateSql = "UPDATE " + tableName + " SET ";
        Json j = new Json(obj);
        removeTransient(obj, j);
        for (String k : j.keySet()) {
            // 首字母转成小写(转Json的时候，如果字段第二个字母是大写，第一个字母也会变成大写)
            // k = k.substring(0, 1).toLowerCase() + k.substring(1);
            updateSql += k + "=:" + k + ",";
        }
        updateSql = updateSql.replace("id=:id,", "");
        updateSql = updateSql.substring(0, updateSql.length() - 1);// 取掉最后的逗号
        updateSql += " WHERE id=:id";
        System.out.println(updateSql);
        return updateSql;
    }

    public static void execute4Debug(Connection conn, final String sql,
            final Object[] newObjs) {
        if (newObjs == null || newObjs.length == 0) {
            LOGGER.info("无数据。");
            return;
        }

        if (conn == null) {// 事务
            // 入库
            sql2o.runInTransaction(new StatementRunnable() {

                public void run(Connection connection, Object argument)
                        throws Throwable {
                    executeSingle(connection, sql, newObjs);
                }
            });
        } else {// 非事务
            executeSingle(conn, sql, newObjs);
        }
    }

    /**
     * 批量执行。如果conn为null,则创建一个新事务。conn不为null，为非事务
     * 
     * @param sql
     * @param newObjs
     */
    public static void executeBatch(Connection conn, final String sql,
            final Object[] newObjs) {
        if (newObjs == null || newObjs.length == 0) {
            LOGGER.info("无数据。");
            return;
        }

        // 数据量大时，分批操作
        for (int grpIdx = 0; grpIdx <= (newObjs.length / BATCH_SIZE); grpIdx++) {
            // 数据数量
            int dataCount = BATCH_SIZE;
            // 起始坐标，也是前面的数据数量
            int fromIdx = grpIdx * BATCH_SIZE;
            if (fromIdx + BATCH_SIZE > newObjs.length) {
                dataCount = newObjs.length - fromIdx;
            }
            if (dataCount == 0) {
                break;// 刚好整除，退出
            }
            final Object[] objGroup = new Object[dataCount];
            System.arraycopy(newObjs, fromIdx, objGroup, 0, dataCount);

            if (conn == null) {// 事务
                // 入库
                sql2o.runInTransaction(new StatementRunnable() {

                    public void run(Connection connection, Object argument)
                            throws Throwable {
                        executeBatch2(connection, sql, objGroup);
                    }
                });
            } else {// 非事务
                executeBatch2(conn, sql, objGroup);
            }
        }
    }

    /**
     * 将数据按batchSize分组，便于批量插入
     * 
     * @param objs
     * @param batchSize
     * @return
     */
    public static List<Object[]> splitGroup(Object[] objs, int batchSize) {
        List<Object[]> groups = new LinkedList<Object[]>();
        // 数据量大时，分批操作
        for (int grpIdx = 0; grpIdx <= (objs.length / batchSize); grpIdx++) {
            // 数据数量
            int dataCount = batchSize;
            // 起始坐标，也是前面的数据数量
            int fromIdx = grpIdx * batchSize;
            if (fromIdx + batchSize > objs.length) {
                dataCount = objs.length - fromIdx;
            }
            if (dataCount == 0) {
                break;// 刚好整除，退出
            }
            final Object[] objGroup = new Object[dataCount];
            System.arraycopy(objs, fromIdx, objGroup, 0, dataCount);
            groups.add(objGroup);
        }
        return groups;
    }

    /**
     * 批量执行(大数据会分段，按事务执行)
     * 
     * @param sql
     * @param newObjs
     */
    public static void executeBatch(String sql, Object[] newObjs) {
        executeBatch(null, sql, newObjs);
    }

    /**
     * 批量执行。不分段，非事务
     * 
     * @param conn
     * @param sql
     * @param objGroup
     */
    private static void executeBatch2(Connection conn, String sql,
            Object[] objGroup) {
        Query query = conn.createQuery(sql);
        for (Object o : objGroup) {
            query.bind(o).addToBatch();
        }
        query.executeBatch();
        LOGGER.info("executeBatch:" + objGroup.length);
    }

    /**
     * 执行sql语句，返回单个值
     * 
     * @param sql
     * @return
     */
    public static Object executeScalar(String sql) {
        return sql2o.createQuery(sql).executeScalar();
    }

    private static void executeSingle(Connection conn, String sql,
            Object[] objGroup) {
        Query query = conn.createQuery(sql);
        for (Object o : objGroup) {
            query.bind(o).executeUpdate();
        }
        LOGGER.info("executeSingle:" + objGroup.length);
    }

    public static void executeSql(Connection conn, String sql) {
        conn.createQuery(sql).executeUpdate();
    }

    /**
     * 执行sql语句
     * 
     * @param sql
     */
    public static void executeSql(String sql) {
        sql2o.createQuery(sql).executeUpdate();
    }

    /**
     * 根据sql取得T类型的list，适用于多表join的情况 bean不需要定义任何注解
     * 
     * @param sql
     * @param clazz
     * @return
     */
    public static <T> List<T> fetch(Connection conn, String sql, Class<T> clazz) {
        List<T> list = conn.createQuery(sql).executeAndFetch(clazz);
        return list;
    }

    /**
     * 根据sql取得T类型的list，适用于多表join的情况
     * 
     * @param sql
     * @param clazz
     * @return
     */
    public static <T> List<T> fetch(String sql, Class<T> clazz) {
        List<T> list = sql2o.createQuery(sql).executeAndFetch(clazz);
        return list;
    }

    /**
     * 根据sql获取数据表
     * 
     * @param sql
     * @return
     */
    public static org.sql2o.data.Table fetchTable(String sql) {
        return sql2o.createQuery(sql).executeAndFetchTable();
    }

    /**
     * 取得表中数据(bean需要有Table注解)
     * 
     * @param c
     * @return
     */
    public static <T> List<T> fetchAll(Class<T> c) {
        return fetchAll(c, "");
    }

    /**
     * 取得表中数据(bean需要有Table注解)
     * 
     * @param c
     * @param whereSql
     *            "Where columnName = value and columnName2=value2"
     * @return
     */
    public static <T> List<T> fetchAll(Class<T> c, String whereSql) {
        String tableName = DbUtils.getTableName(c);
        if (whereSql == null) {
            whereSql = "";
        }
        List<T> list = getSql2o().createQuery(
                "SELECT * FROM " + tableName + " " + whereSql).executeAndFetch(
                c);
        return list;
    }

    public static <T> T fetchById(Class<T> clazz, Long id) {
        Table t = clazz.getAnnotation(Table.class);
        String tableName = t.name();
        String sql = "SELECT * FROM " + tableName + " WHERE ID=:ID";
        T obj = getSql2o().createQuery(sql).addParameter("ID", id)
                .executeAndFetchFirst(clazz);
        return obj;
    }

    /**
     * 取得表中第一条数据(bean需要有Table注解)
     * 
     * @param c
     * @param whereSql
     *            "Where columnName = value and columnName2=value2"
     * @return
     */
    public static <T> T fetchFirst(Class<T> c, String whereSql) {
        String tableName = DbUtils.getTableName(c);
        if (whereSql == null) {
            whereSql = "";
        }
        T o = getSql2o().createQuery(
                "SELECT * FROM " + tableName + " " + whereSql)
                .executeAndFetchFirst(c);
        return o;
    }

    /**
     * 取得scalar集合
     * 
     * @param sql
     * @return
     */
    public static <T> List<T> fetchScalarList(String sql) {
        List<T> l = getSql2o().createQuery(sql).executeScalarList();
        return l;
    }

    public static EstConfiguration getEstConfig() {
        return estConfig;
    }

    /**
     * 取得下一个序列值
     * 
     * @param obj
     * @return
     */
    public static Long getNextSeq(Object obj) {
        String seqName = getSequenceName(obj);// 先取一个序列值
        String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL";
        BigDecimal newId = (BigDecimal) sql2o.createQuery(sql).executeScalar();// 取得id，后面要用
        return newId.longValue();
    }

    /**
     * 取得序列名称
     * 
     * @param obj
     * @return
     */
    public static String getSequenceName(Object obj) {
        SequenceGenerator sequence = null;
        try {
            Method m = DbUtils.getDeclaredMethod(obj, "getId");
            sequence = m.getAnnotation(SequenceGenerator.class);
        } catch (Exception e) {
            LOGGER.info("getId方法上没有SequenceGenerator");
        }
        if (sequence == null) {
            try {
                Field f = DbUtils.getDeclaredField(obj, "id");
                sequence = f.getAnnotation(SequenceGenerator.class);
            } catch (Exception e) {
                LOGGER.info("id字段上没有SequenceGenerator");
            }
        }
        if (sequence != null) {
            return sequence.sequenceName();
        } else {
            LOGGER.info("没有找到sequence信息");
            return null;
        }
    }

    public static Sql2o getSql2o() {
        return sql2o;
    }

    /**
     * 非批量插入，性能差，用来调试语句问题，需放在事务中执行
     * 
     * @param conn
     * @param newObjs
     */
    public static void insert(Connection conn, Object... newObjs) {
        if (newObjs != null && newObjs.length > 0) {
            if (newObjs.length == 1 && newObjs[0] instanceof Collection) {
                newObjs = ((Collection<?>) newObjs[0]).toArray();
            }
            if (newObjs.length == 0) {
                LOGGER.info("insert:无数据需插入");
                return;
            }
            String sql = buildInsertSql(newObjs[0]);
            Query query = conn.createQuery(sql);
            for (Object o : newObjs) {
                query.bind(o).executeUpdate();
            }
            LOGGER.info("insert:已插入" + newObjs.length + "条数据。");
        } else {
            LOGGER.info("insert:无数据需插入");
        }
    }

    /**
     * 非批量插入，性能差，用来调试语句问题，需放在事务中执行
     * 
     * @param conn
     * @param newObjs
     */
    public static void mergeInto(Connection conn, String[] condFields,
            Object... newObjs) {
        if (newObjs != null && newObjs.length > 0) {
            if (newObjs.length == 1 && newObjs[0] instanceof Collection) {
                newObjs = ((Collection<?>) newObjs[0]).toArray();
            }
            if (newObjs.length == 0) {
                LOGGER.info("batchMergeInto:无数据需MergeInto");
                return;
            }
            final String sql = buildMergeIntoSql(newObjs[0], condFields);
            Query query = conn.createQuery(sql);
            for (Object o : newObjs) {
                query.bind(o).executeUpdate();
            }
            LOGGER.info("mergeInto:已插入" + newObjs.length + "条数据。");
        } else {
            LOGGER.info("mergeInto:无数据需插入");
        }
    }

    public static org.sql2o.data.Table ExecuteTable(String sql) {
        org.sql2o.data.Table t = SqlDao.getSql2o().createQuery(sql)
                .executeAndFetchTable();
        return t;

    }

    public static void main(String[] args) {
        SpringContext CONTEXT = new SpringContext();
        CONTEXT.getObject(SqlDao.class);
        // String sql = buildMergeIntoSql(new Cv2DTMpHisData(), "areaCode",
        // "dataTime");
        // System.out.println(sql);
    }

    public static void renameTable(String oldName, String newName) {
        getSql2o().createQuery(
                " alter table  " + oldName + " rename to " + newName)
                .executeUpdate();
    }

    @PostConstruct
    public void init() {
        sql2o = new Sql2o(dataSource);
        estConfig = config;
    }

  
}
