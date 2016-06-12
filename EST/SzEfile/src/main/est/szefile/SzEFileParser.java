package est.szefile;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sql2o.Connection;
import org.sql2o.StatementRunnable;

import est.commons.U;
import est.commons.dataparser.DataParserStarter;
import est.commons.dataparser.parser.ParseResult;
import est.commons.dataparser.parser.Parser;
import est.commons.efile.EFileDataRow;
import est.commons.efile.EFileReader;
import est.commons.efile.EFileTag;
import est.commons.utils.SpringContext;
import est.e6k.mergedoc.util.CimDao;
import est.szefile.bean.SystemTag;
import est.szefile.bean.SzdlData;
import est.szefile.bean.SzhisData;
import est.szefile.bean.SzhisDataI;
import est.szefile.bean.SzhisDataP;
import est.szefile.bean.SzhisDataQ;
import est.szefile.bean.SzhisDataU;
import est.szefile.util.Config;
import est.szefile.util.EmsUtils;
import est.szefile.util.FileFactory;

@Component
public class SzEFileParser extends Parser {

    private static final Logger LOGGER = Logger.getLogger(SzEFileParser.class);
    private static final Long AREA_CODE = 100L;
    /**
     * E文件默认编码
     */
    protected static String encoding = "GBK";
    // 间隔符（8个空格）
    protected static String gap = "\\|\\|";// 正则表达式

    public static SpringContext CONTEXT = null;

    /**
     * 过滤重复数据
     * 
     * @param dataListm
     *            e
     * @param idName
     */
    private static List<Object> filerReplication(Object[] dataArray) {
        // List<String> idList = new ArrayList<String>();
        // List<Object> replicationList = new ArrayList<Object>();
        // for (Object o : dataArray) {
        // try {
        // Long id = (Long) PropertyUtils.getSimpleProperty(o, "cimId");
        // Long subId = null;
        // Long emsMeasureId = null;
        // try {
        // subId = (Long) PropertyUtils.getSimpleProperty(o,
        // "substationId");
        // } catch (Exception ex) {
        // }
        // try {
        // emsMeasureId = (Long) PropertyUtils.getSimpleProperty(o,
        // "emsMeasureId");
        // } catch (Exception ex) {
        // }
        // String key = id + "-" + subId + "-" + emsMeasureId;
        // if (idList.contains(key) == false) {
        // idList.add(key);
        // } else {
        // replicationList.add(o);
        // }
        // } catch (Exception ex) {
        //
        // }
        // }

        List<Object> l = new ArrayList<Object>(Arrays.asList(dataArray));// aslist出来的list不支持remove
        // l.removeAll(replicationList);
        return l;
    }

    /**
     * 主函数
     * 
     * @param args
     */
    public static void main(String[] args) {
        // spring注入
        CONTEXT = new SpringContext();
        SzEFileParser main = (SzEFileParser) CONTEXT
                .getObject(SzEFileParser.class);
        // 配置项初始化
        EmsUtils.initDir();
        main.startParse();
    }

    /**
     * mergeInto
     * 
     * @param dataArray
     * @param dataTime
     */
    protected static void persistData(final Object[] dataArray,
            final Date dataTime) {
        if (dataArray != null && dataArray.length > 0) {
            // 过滤重复数据
            final List<Object> list = filerReplication(dataArray);
            // 先删再插（该类型设备给定时间的所有数据都删掉)
            CimDao.getSql2o().runInTransaction(new StatementRunnable() {

                public void mergeInto(Connection connection) {
                    String[] conds = null;

                    conds = new String[] { "jldbh", "areaCode", "dataTime" };

                    CimDao.batchMergeInto(connection, conds, list.toArray());
                }

                @Override
                public void run(Connection connection, Object argument)
                        throws Throwable {
                    // 此处用mergeInto会有问题，在多线程插入时，有可能2个E文件的时间一样，同时插入会报错
                    try {
                        mergeInto(connection);
                    } catch (Exception ex) {
                        // merge失败，再merge一次
                        LOGGER.info("merge失败，再merge一次：" + ex.getMessage());
                        mergeInto(connection);
                    }
                }
            });
        } else {
            LOGGER.warn("没有数据--时间点：" + dataTime);
        }
    }

    @Autowired
    private FileFactory fileFactory;

    private DataParserStarter dataStarter;

    // key=监控路径，value=文件后缀名
    private Map<String, String> fileNameMap = new HashMap<String, String>();

    protected void addAMSEData(EFileDataRow efileDataRow,
            Map<String, Long> name2IdMap, SimpleDateFormat sdf,
            List<Object> dataList) {
        // @||计量点编号||用户编号||计量点号||电表资产编号||时间类型||时间标识||需量表码||最大需量发生时间
        // ||正向有功总||正向有功峰||正向有功平||正向有功谷||正向有功尖||正向无功总||正向无功峰||正向无功平||正向无功谷||正向无功尖
        // ||正向有功A相||正向有功B相||正向有功C相||反向有功总||反向有功峰||反向有功平||反向有功谷||反向有功尖||反向无功总||反向无功峰
        // ||反向无功平||反向无功谷||反向无功尖

        // Long windingId = name2IdMap.get(efileDataRow.getValue("变压器绕组ID"));
        // if (windingId != null) {

        Long jldbh = EmsUtils.parseLong(efileDataRow.getValue("计量点编号"));
        String yhbh = efileDataRow.getValue("用户编号");
        String jldh = efileDataRow.getValue("计量点号");
        String dbzcbh = efileDataRow.getValue("电表资产编号");
        LOGGER.debug("计量点号：" + efileDataRow.getValue("计量点号"));

        String dataTimeS = efileDataRow.getValue("时间标识");
        Date dt = null;
        try {
            dt = sdf.parse(dataTimeS);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Double pap = EmsUtils.parseDouble2(efileDataRow.getValue("正向有功总"));
        Double prp = EmsUtils.parseDouble2(efileDataRow.getValue("正向无功总"));
        Double rap = EmsUtils.parseDouble2(efileDataRow.getValue("反向有功总"));
        Double rrp = EmsUtils.parseDouble2(efileDataRow.getValue("反向无功总"));

        SzdlData twd = new SzdlData();
        twd.setDbzcbh(dbzcbh);
        twd.setJldbh(jldbh);
        twd.setJldh(jldh);
        twd.setYhbh(yhbh);
        twd.setDataTime(dt);
        twd.setPap_r(pap);
        twd.setRap_r(rap);
        twd.setPrp_r(prp);
        twd.setRrp_r(rrp);
        twd.setAreaCode(AREA_CODE);
        dataList.add(twd);
        // }
    }

    private void addCurrentData(EFileDataRow efileDataRow, Object object,
            SimpleDateFormat sdf, List<SzhisDataI> list)
            throws InstantiationException, IllegalAccessException {
        SzhisData twd = buildHisData(efileDataRow, sdf, list, SzhisDataI.class);

        // A相电流 B相电流 C相电流 零序电流
        Double ia = EmsUtils.parseDouble2(efileDataRow.getValue("A相电流"));
        Double ib = EmsUtils.parseDouble2(efileDataRow.getValue("B相电流"));
        Double ic = EmsUtils.parseDouble2(efileDataRow.getValue("C相电流"));

        twd.setIa(ia);
        twd.setIb(ib);
        twd.setIc(ic);

    }

    private void addPowerData(EFileDataRow efileDataRow, Object object,
            SimpleDateFormat sdf, List<SzhisDataP> list)
            throws InstantiationException, IllegalAccessException {
        SzhisData twd = buildHisData(efileDataRow, sdf, list, SzhisDataP.class);

        // 总有功功率 A相有功功率 B相有功功率 C相有功功率 总无功功率 A相无功功率 B相无功功率 C相无功功率
        // 总视在功率 A相视在功率 B相视在功率 C相视在功率

        Double p = EmsUtils.parseDouble2(efileDataRow.getValue("总有功功率"));
        Double pa = EmsUtils.parseDouble2(efileDataRow.getValue("A相有功功率"));
        Double pb = EmsUtils.parseDouble2(efileDataRow.getValue("B相有功功率"));
        Double pc = EmsUtils.parseDouble2(efileDataRow.getValue("C相有功功率"));

        Double q = EmsUtils.parseDouble2(efileDataRow.getValue("总无功功率"));
        Double qa = EmsUtils.parseDouble2(efileDataRow.getValue("A相无功功率"));
        Double qb = EmsUtils.parseDouble2(efileDataRow.getValue("B相无功功率"));
        Double qc = EmsUtils.parseDouble2(efileDataRow.getValue("C相无功功率"));

        Double s = EmsUtils.parseDouble2(efileDataRow.getValue("总视在功率"));
        Double sa = EmsUtils.parseDouble2(efileDataRow.getValue("A相视在功率"));
        Double sb = EmsUtils.parseDouble2(efileDataRow.getValue("B相视在功率"));
        Double sc = EmsUtils.parseDouble2(efileDataRow.getValue("C相视在功率"));

        twd.setS(s);
        twd.setSa(sa);
        twd.setSb(sb);
        twd.setSc(sc);
        twd.setP(p);
        twd.setPa(pa);
        twd.setPb(pb);
        twd.setPc(pc);
        twd.setQ(q);
        twd.setQa(qa);
        twd.setQb(qb);
        twd.setQc(qc);
    }

    private void addPowerFactorData(EFileDataRow efileDataRow, Object object,
            SimpleDateFormat sdf, List<SzhisDataQ> list)
            throws InstantiationException, IllegalAccessException {

        SzhisData twd = buildHisData(efileDataRow, sdf, list, SzhisDataQ.class);

        // 总功率因数 A相功率因数 B相功率因数 C相功率因数
        Double pf = EmsUtils.parseDouble2(efileDataRow.getValue("总功率因数"));
        Double pfa = EmsUtils.parseDouble2(efileDataRow.getValue("A相功率因数"));
        Double pfb = EmsUtils.parseDouble2(efileDataRow.getValue("B相功率因数"));
        Double pfc = EmsUtils.parseDouble2(efileDataRow.getValue("C相功率因数"));

        twd.setPf(pf);
        twd.setPfa(pfa);
        twd.setPfb(pfb);
        twd.setPfc(pfc);
    }

    private void addVoltageData(EFileDataRow efileDataRow, Object object,
            SimpleDateFormat sdf, List<SzhisDataU> list)
            throws InstantiationException, IllegalAccessException {
        SzhisData twd = buildHisData(efileDataRow, sdf, list, SzhisDataU.class);

        // A相电压 B相电压 C相电压
        Double ua = EmsUtils.parseDouble2(efileDataRow.getValue("A相电压"));
        Double ub = EmsUtils.parseDouble2(efileDataRow.getValue("B相电压"));
        Double uc = EmsUtils.parseDouble2(efileDataRow.getValue("C相电压"));

        twd.setUa(ua);
        twd.setUb(ub);
        twd.setUc(uc);

    }

    private <T extends SzhisData> T buildHisData(EFileDataRow efileDataRow,
            SimpleDateFormat sdf, List<T> list, Class<T> cls)
            throws InstantiationException, IllegalAccessException {
        Long jldbh = EmsUtils.parseLong(efileDataRow.getValue("计量点编号"));
        String yhbh = efileDataRow.getValue("用户编号");
        String jldh = efileDataRow.getValue("计量点号");
        String dbzcbh = efileDataRow.getValue("电表资产编号");
        LOGGER.debug("计量点号：" + efileDataRow.getValue("计量点号"));

        if (yhbh.equals("0943000047001607")) {
            LOGGER.debug("计量点号：" + efileDataRow.getValue("计量点号"));
        }

        String dataTimeS = efileDataRow.getValue("时间标识");
        Date dt = null;
        try {
            dt = sdf.parse(dataTimeS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        T twd = cls.newInstance();
        twd.setDbzcbh(dbzcbh);
        twd.setJldbh(jldbh);
        twd.setJldh(jldh);
        twd.setYhbh(yhbh);
        twd.setDataTime(dt);
        twd.setAreaCode(AREA_CODE);
        list.add(twd);
        return twd;
    }

    private SzhisData buildHisData(EFileDataRow efileDataRow,
            SimpleDateFormat sdf, Map<String, SzhisData> hisDataMap) {
        Long jldbh = EmsUtils.parseLong(efileDataRow.getValue("计量点编号"));
        String yhbh = efileDataRow.getValue("用户编号");
        String jldh = efileDataRow.getValue("计量点号");
        String dbzcbh = efileDataRow.getValue("电表资产编号");
        LOGGER.debug("计量点号：" + efileDataRow.getValue("计量点号"));

        if (yhbh.equals("0943000047001607")) {
            LOGGER.debug("计量点号：" + efileDataRow.getValue("计量点号"));
        }

        String dataTimeS = efileDataRow.getValue("时间标识");
        Date dt = null;
        try {
            dt = sdf.parse(dataTimeS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String key = jldh + "@" + dataTimeS;
        SzhisData twd = hisDataMap.get(key);
        if (twd == null) {
            twd = new SzhisData();
            twd.setDbzcbh(dbzcbh);
            twd.setJldbh(jldbh);
            twd.setJldh(jldh);
            twd.setYhbh(yhbh);
            twd.setDataTime(dt);
            twd.setAreaCode(AREA_CODE);
            hisDataMap.put(key, twd);
        }
        return twd;
    }

    protected boolean checkColumnCount(EFileDataRow row, int lenth) {
        return row.getValues().length != lenth;
    }

    @Override
    public ParseResult doParse(File file) {
        File newFile = null;
        try {
            LOGGER.info("开始解析E文件" + file.getName() + "...");
            // 预处理
            newFile = pretreatEFile(file);
            if (newFile != null) {
                parseEFile(newFile.getPath());
            } else {
                parseEFile(file.getPath());
            }
            LOGGER.info("E文件" + file.getName() + "解析结束。");
            return new ParseResult();
        } catch (Exception e) {
            LOGGER.error(file.getName() + "解析失败。", e);
            return null;
        } finally {
            // 删掉预处理后的文件
            try {
                if (newFile != null) {
                    newFile.delete();
                }
            } catch (Exception ex) {
                LOGGER.info("删除出错:" + newFile.getPath());
            }
        }
    }

    /**
     * 解析绕组
     * 
     * @param tag
     */
    protected void parseAMSEData(EFileTag tag, Date dataTime) {
        List<Object> list = new LinkedList<Object>();
        int lenth = tag.getColumnHeader().size();
        // Map<String, Long> twMap = Cv2Cache
        // .getCimMap4EFile(Cv2TransWindingAll.class);
        // if (twMap.size() == 0) {
        // LOGGER.error("没有绕组档案缓存");
        // return;
        // }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (EFileDataRow row : tag) {
            try {
                // if (row.getValue("计量点号").equals(row.getValue("计量点号"))) {
                // System.out.println("xxx");
                // }

                if (checkColumnCount(row, lenth)) {
                    LOGGER.debug("计量点号:" + row.getValue("计量点号")
                            + " 系统数据个数不正确：一行数据数量应该为:" + lenth + "，实际数据量为："
                            + row.getValues().length);
                    // continue;
                }

                addAMSEData(row, null, sdf, list);

            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        persistData(list.toArray(), dataTime);
    }

    private void parseCurrentData(EFileTag tag,
            Map<String, SzhisData> hisDataMap) {
        int lenth = tag.getColumnHeader().size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SzhisDataI> list = new LinkedList<SzhisDataI>();
        for (EFileDataRow row : tag) {
            try {
                if (checkColumnCount(row, lenth)) {
                    LOGGER.debug("计量点号:" + row.getValue("计量点号")
                            + " 系统数据个数不正确：一行数据数量应该为:" + lenth + "，实际数据量为："
                            + row.getValues().length);
                    // continue;
                }

                addCurrentData(row, null, sdf, list);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        persistData(list.toArray(), null);
    }

    /**
     * 获取E文件数据
     * 
     * @param path
     * @throws FileNotFoundException
     */
    private void parseEFile(String path) throws Exception {
        EFileReader reader = null;
        Date dataTime = null;// 数据时间
        Long taskDetailId = 0L;// EFileTaskDetail的Id
        try {
            Map<String, SzhisData> hisDataMap = new HashMap<String, SzhisData>();
            reader = new EFileReader(path, gap, encoding);
            for (EFileTag tag : reader) {
                if (tag.getClassName() == null) {
                    continue;
                }

                // 系统信息（没用）
                if (tag.getClassName().equalsIgnoreCase("system")) {
                    SystemTag sysTag = parseSystemTag(tag);
                    dataTime = sysTag.getDataTime();
                }

                if (tag.getClassName().equalsIgnoreCase("AMSE")) {
                    parseAMSEData(tag, dataTime);
                } else if (tag.getClassName().equalsIgnoreCase("Power")) {
                    parsePowerData(tag, hisDataMap);
                } else if (tag.getClassName().equalsIgnoreCase("PowerFactor")) {
                    parsePowerFactorData(tag, hisDataMap);
                } else if (tag.getClassName().equalsIgnoreCase("Current")) {
                    parseCurrentData(tag, hisDataMap);
                } else if (tag.getClassName().equalsIgnoreCase("Voltage")) {
                    parseVoltageData(tag, hisDataMap);
                }
            }
            // 最后保存负荷数据
            // persistData(hisDataMap.values().toArray(), dataTime);
        } catch (Exception ex) {
            LOGGER.error("parseEFile：" + path + "出错。", ex);
            LOGGER.info("重新抛出异常...");
            throw ex;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void parsePowerData(EFileTag tag, Map<String, SzhisData> hisDataMap) {
        int lenth = tag.getColumnHeader().size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SzhisDataP> list = new LinkedList<SzhisDataP>();
        for (EFileDataRow row : tag) {
            try {
                if (checkColumnCount(row, lenth)) {
                    LOGGER.debug("计量点号:" + row.getValue("计量点号")
                            + " 系统数据个数不正确：一行数据数量应该为:" + lenth + "，实际数据量为："
                            + row.getValues().length);
                    // continue;
                }

                addPowerData(row, null, sdf, list);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        persistData(list.toArray(), null);
    }

    private void parsePowerFactorData(EFileTag tag,
            Map<String, SzhisData> hisDataMap) {
        int lenth = tag.getColumnHeader().size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SzhisDataQ> list = new LinkedList<SzhisDataQ>();
        for (EFileDataRow row : tag) {
            try {
                if (checkColumnCount(row, lenth)) {
                    LOGGER.debug("计量点号:" + row.getValue("计量点号")
                            + " 系统数据个数不正确：一行数据数量应该为:" + lenth + "，实际数据量为："
                            + row.getValues().length);
                    // continue;
                }

                addPowerFactorData(row, null, sdf, list);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        persistData(list.toArray(), null);
    }

    /**
     * 解析系统参数
     * 
     * @param tag
     */
    protected SystemTag parseSystemTag(EFileTag tag) {
        SystemTag systemTag = new SystemTag();
        for (EFileDataRow row : tag) {
            String areaStr = row.getValue("地区");
            String sysStr = row.getValue("系统");
            String dataTimeStr = row.getValue("数据生成时间");
            try {
                if (dataTimeStr != null && dataTimeStr.length() > 0) {
                    // 判断数据所属与的5分钟点([0,5)为第0分钟数据，[5~10)为第5分钟数据，以此类推)
                    String minutes = dataTimeStr.substring(14, 16);
                    Integer min = 5 * (Integer.valueOf(minutes) / 5);
                    String m = null;
                    if (min < 10) {
                        m = "0" + min;
                    } else {
                        m = min.toString();
                    }
                    dataTimeStr = dataTimeStr.substring(0, 10) + " "
                            + dataTimeStr.substring(11, 14) + m + ":00";
                    Date dataTime = U.parseDate(dataTimeStr,
                            "yyyy-MM-dd HH:mm:ss");
                    systemTag.setArea(areaStr);
                    systemTag.setSystemName(sysStr);
                    systemTag.setDataTime(dataTime);
                }
            } catch (Exception e) {
                LOGGER.error("可读时间格式错误 ：" + dataTimeStr);
            }
            LOGGER.info("文档类型 : Data分钟数据 ");
            LOGGER.info("地区 : " + areaStr);
            LOGGER.info("系统 : " + sysStr);
            LOGGER.info("数据生成时间 : " + dataTimeStr);
        }
        return systemTag;
    }

    private void parseVoltageData(EFileTag tag,
            Map<String, SzhisData> hisDataMap) {
        int lenth = tag.getColumnHeader().size();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SzhisDataU> list = new LinkedList<SzhisDataU>();
        for (EFileDataRow row : tag) {
            try {
                if (checkColumnCount(row, lenth)) {
                    LOGGER.debug("计量点号:" + row.getValue("计量点号")
                            + " 系统数据个数不正确：一行数据数量应该为:" + lenth + "，实际数据量为："
                            + row.getValues().length);
                    // continue;
                }

                addVoltageData(row, null, sdf, list);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        persistData(list.toArray(), null);
    }

    private File pretreatEFile(File file) {
        // TODO Auto-generated method stub
        return null;
    }

    private void startParse() {
        LOGGER.info("文件解析线程启动......");
        //
        // fileNameMap.put(Config.CIM_Root, Config.Cim_SuffixName);
        fileNameMap.put(Config.EFILE_Root, Config.Efile_SuffixName);

        // 监控文件夹-后缀，扫描间隔，文件工厂，备份路径，出错路径，备份天数
        dataStarter = new DataParserStarter(fileNameMap,
                Config.File_Scan_Interval, fileFactory, Config.BACKUP_DIR,
                Config.ERROR_DIR, Config.BackUp_Days, Config.Parser_Count);
        dataStarter.start();

    }
}
