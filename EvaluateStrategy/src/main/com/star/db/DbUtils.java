package com.star.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public class DbUtils {
    private static Logger LOGGER = Logger.getLogger(DbUtils.class);
    
    

    // 全角转半角的经典函数，只能转字母和数字+-
    public static String convertQjToBj(String qjStr) // java下实现
    {
        char[] chr = qjStr.toCharArray();
        String str = "";
        for (int i = 0; i < chr.length; i++) {
            if (chr[i] < 65248) {
                chr[i] = chr[i];
            } else
                chr[i] = (char) (chr[i] - 65248);

            str += chr[i];
        }
        return str;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     * 
     * @param object
     *            : 对象
     * @param fieldName
     *            : 属性名
     * @return 属性对象
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     * 
     * @param object
     *            : 对象
     * @param methodName
     *            : 方法名
     * @param parameterTypes
     *            : 方法参数类型
     * @return 方法对象
     */
    public static Method getDeclaredMethod(Object object, String methodName,
            Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
            }
        }
        return null;
    }


    // id字符串分解为id列表
    public static List<Long> getIdList(String idString) {
        List<Long> idList = new ArrayList<Long>();
        if (Strings.isNullOrEmpty(idString) == false) {
            String[] ids = idString.split(",");
            for (String id : ids) {
                if (Strings.isNullOrEmpty(id) == false) {
                    idList.add(Long.parseLong(id));
                }
            }
        }
        return idList;
    }


    

    // 名称提纯
    public static String refineName(String name) {
        if (Strings.isNullOrEmpty(name) == false) {
            return convertQjToBj(name).replace("K", "k").replace("v", "V");
        }
        return "";
    }

   

    public static String getEntityName(Class<?> c) {
        String entityName = c.getSimpleName().replace("Cv2", "")
                .replace("All", "").toLowerCase();
        return entityName;
    }

    /**
     * 根据对象取得数据表名
     * 
     * @param obj
     * @return
     */
    public static String getTableName(Object obj) {
        Table t = obj.getClass().getAnnotation(Table.class);
        if (t != null) {
            return t.name();
        }
        return null;
    }

    /**
     * 根据类对象取得表名
     * 
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table t = clazz.getAnnotation(Table.class);
        if (t != null) {
            return t.name();
        }
        LOGGER.error(clazz.getSimpleName() + "没有Table注解。");
        return null;
    }
}
