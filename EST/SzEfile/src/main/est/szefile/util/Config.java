package est.szefile.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 配置项
 * 
 * @author 殷闯
 * 
 */
@Component
public class Config {
    private static Logger logger = LoggerFactory.getLogger(Config.class);

    /**
     * CIM模型文件路径
     */
    public static String CIM_Root;
    public static String BACKUP_DIR;
    public static String ERROR_DIR;
    /**
     * E文件扫描根目录,配置只对EMS系统接口有效
     */
    public static String EFILE_Root;
    /**
     * E文件后缀名
     */
    public static String Efile_SuffixName;
    
    public static String Cim_SuffixName;
    
    /**
     * 文件扫描时间间隔，单位 秒
     */
    public static int File_Scan_Interval;
    public static int BackUp_Days;
    // 文件解析线程数
    public static int Parser_Count;
    // cim过滤
    public static String Cim_Filter;

    // EMS库的Schema
    public static String Ems_Schema;

    static {
        initConfig();
    }

    public static void initConfig() {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration();
            config.setEncoding("UTF-8");
            config.setFileName("EmsInterface.properties");
            config.load();

            logger.info("读取配置文件...");
            Class<Config> clazz = Config.class;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // 属性必须是公有静态
                if (field.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC)) {
                    continue;
                }
                String fieldName = field.getName();
                String value = config.getString(fieldName);
                logger.info(fieldName + " = " + value);

                if (value == null || value.trim().equals("")) {
                    logger.warn(fieldName + "没有配置。");
                }
                Class<?> clazz0 = field.getType();
                try {
                    if (clazz0 == String.class) {
                        field.set(null, value);
                    } else if (clazz0 == int.class || clazz0 == Integer.class) {
                        field.setInt(null, Integer.parseInt(value));
                    } else if (clazz0 == long.class || clazz0 == Long.class) {
                        field.setLong(null, Long.parseLong(value));
                    } else if (clazz0 == float.class || clazz0 == Float.class) {
                        field.setFloat(null, Float.parseFloat(value));
                    } else if (clazz0 == double.class || clazz0 == Double.class) {
                        field.setDouble(null, Double.parseDouble(value));
                    } else if (clazz0 == boolean.class
                            || clazz0 == Boolean.class) {
                        field.setBoolean(null, Boolean.parseBoolean(value));
                    }
                } catch (Exception e) {
                    logger.error(fieldName + "配置出错", e);
                }
            }
        } catch (Exception ex) {
            logger.error("读取配置出错", ex);
        }
    }
}
