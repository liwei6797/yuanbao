package est.szefile.calc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.sql2o.data.Row;
import org.sql2o.data.Table;

import com.google.common.base.Joiner;

import est.commons.utils.SpringContext;
import est.e6k.mergedoc.util.CimDao;

//计算重过载
public class CalcOverload {
    private static final Logger LOGGER = Logger.getLogger(CalcOverload.class);

    public static void main(String[] args) {
        // spring注入
        SpringContext CONTEXT = new SpringContext();
        Set<String> heavyLoadSet = new HashSet<String>();
        Set<String> overLoadSet = new HashSet<String>();

        Table mps = CimDao.fetchTable("Select * from mid_point3");
        int i = 0;
        List<String> jldhs = new LinkedList<String>();
        for (Row r : mps.rows()) {
            jldhs.add(r.getString("JLDH"));
            i++;
            if (i == 100) {
                calcOverLoad(heavyLoadSet, overLoadSet, jldhs);
                jldhs.clear();// 每100个一批次
                i = 0;
            }
        }
        if (jldhs.size() > 0) {
            // 剩下的再算一批次
            calcOverLoad(heavyLoadSet, overLoadSet, jldhs);
        }

        LOGGER.info("重载数量：" + heavyLoadSet.size());
        LOGGER.info("('" + Joiner.on("','").join(heavyLoadSet) + "')");
        LOGGER.info("过载数量：" + overLoadSet.size());
        LOGGER.info("('" + Joiner.on("','").join(overLoadSet) + "')");

    }

    private static void calcOverLoad(Set<String> heavyLoadSet,
            Set<String> overLoadSet, List<String> jldhs) {
        LOGGER.info("计算:" + jldhs.size());
        String jldhss = "('" + Joiner.on("','").join(jldhs) + "')";
        Table datas = CimDao
                .fetchTable("select mp.jldh, "
                        + " TO_DATE(TO_CHAR(data.datatime, 'YYYY-MM-DD HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') datatime, "
                        + " data.p, "
                        + " data.q, "
                        + " mp.m_capacity, "
                        + " mp.pt, "
                        + " mp.ct, "
                        + " sqrt(power(data.p, 2) + power(data.q, 2)) * nvl(mp.pt, 1) * nvl(mp.ct, 1) / 1000 / mp.m_capacity loadrate "
                        + " from mid_point3 mp "
                        + " inner join est_ddy_szhisdata_p data "
                        + " on mp.jldh = data.jldh "
                        + " where mp.m_capacity > 0 and mp.jldh in " + jldhss
                        + " and data.p is not null and data.q is not null "
                        + " and data.datatime between to_date('20160517','yyyymmdd') and to_date('20160601','yyyymmdd') "
                        + " order by jldh, datatime");
        LOGGER.info("获取数据完毕");
        long currentTime = 0L;
        int heavyLoadCount = 0;
        int overLoadCount = 0;
        String currentJldh = "";
        for (Row dataRow : datas.rows()) {
            long time = dataRow.getDate("datatime").getTime();
            String jldh = dataRow.getString("jldh");
            Double loadRate = dataRow.getDouble("loadrate");
            if (currentJldh.equals(jldh) == false
                    || time - currentTime != 15 * 60 * 1000) {// 计量点号变了，或者时间不是15分钟间隔，重新计数
                heavyLoadCount = 0;
                overLoadCount = 0;
                currentJldh = jldh;
                currentTime = time;
            }

            if (loadRate > 0.8) {
                heavyLoadCount++;
                if (heavyLoadCount >= 8) {
                    heavyLoadSet.add(jldh);
                    LOGGER.info(jldh + "重载");
                }
            } else {
                heavyLoadCount = 0;// 重新计数
            }

            if (loadRate > 1) {
                overLoadCount++;
                if (overLoadCount >= 8) {
                    overLoadSet.add(jldh);
                    LOGGER.info(jldh + "过载");
                }
            } else {
                overLoadCount = 0;// 重新计数
            }
        }
        LOGGER.info("计算完毕");
    }
}
