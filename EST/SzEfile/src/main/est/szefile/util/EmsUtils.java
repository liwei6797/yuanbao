package est.szefile.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import est.commons.utils.EstFileUtils;

public class EmsUtils {
    static final Logger LOGGER = Logger.getLogger(EmsUtils.class);

    // 非标准电压值，映射到标准电压值
    public static Map<Double, Long> UnnormalVoltMap = new HashMap<Double, Long>();

    static {
        initVoltMap();
    }

    public static void initVoltMap() {
        UnnormalVoltMap.put(525.0, 500000L);
        UnnormalVoltMap.put(230.0, 220000L);
        UnnormalVoltMap.put(115.0, 110000L);
        UnnormalVoltMap.put(35.0, 35000L);
        UnnormalVoltMap.put(37.0, 35000L);
        UnnormalVoltMap.put(37.5, 35000L);
        UnnormalVoltMap.put(38.0, 35000L);
        UnnormalVoltMap.put(38.5, 35000L);
        UnnormalVoltMap.put(10.5, 10000L);
        UnnormalVoltMap.put(6.3, 6000L);
        UnnormalVoltMap.put(0.4, 380L);
    }

    public static void initDir() {
        EstFileUtils.mkdirQuietly(new File(Config.CIM_Root));
        EstFileUtils.mkdirQuietly(new File(Config.EFILE_Root));
        EstFileUtils.mkdirQuietly(new File(Config.BACKUP_DIR));
        EstFileUtils.mkdirQuietly(new File(Config.ERROR_DIR));
    }

    /**
     * 解析出错返回null
     * 
     * @param o
     * @return
     */
    public static Double parseDouble2(Object o) {
        if (o == null) {
            return null;
        } else {
            try {
                return Double.parseDouble(o.toString());
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static double parseDouble(Object o) {
        if (o == null) {
            return 0d;
        } else {
            try {
                return Double.parseDouble(o.toString());
            } catch (Exception ex) {
                return 0d;
            }
        }
    }

    /**
     * 解析出错返回0
     * 
     * @param o
     * @return
     */
    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        } else {
            try {
                return Integer.parseInt(o.toString());
            } catch (Exception ex) {
                return 0;
            }
        }
    }

    public static long parseLong(Object o) {
        if (o == null) {
            return 0;
        } else {
            try {
                return Long.parseLong(o.toString());
            } catch (Exception ex) {
                return 0;
            }
        }
    }

    public static Long parseLong2(Object o) {
        if (o == null) {
            return null;
        } else {
            try {
                return Long.parseLong(o.toString());
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static Double getS(Double p, Double q) {
        if (p == null || q == null) {
            return null;
        }
        double s = Math.sqrt(p * p + q * q);
        return s;
    }

    public static Double getPF(Double p, Double q) {
        if (p == null || q == null) {
            return null;
        }
        double s = getS(p, q);
        if (s == 0) {
            return 0d;
        } else {
            return p / s;
        }
    }
}
