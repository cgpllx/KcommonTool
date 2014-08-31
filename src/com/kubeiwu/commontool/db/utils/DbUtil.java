package com.kubeiwu.commontool.db.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import com.kubeiwu.commontool.db.KCommonToolDb;
import com.kubeiwu.commontool.db.table.Property;
import com.kubeiwu.commontool.db.table.TableInfo;
import com.kubeiwu.commontool.db.utils.A.Table;

/**
 * DB工具类
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-18
 */
public class DbUtil {
	public static <T> T getEntity(Cursor cursor, Class<T> clazz, KCommonToolDb db) {
		try {
			if (cursor != null) {
				TableInfo table = TableInfo.get(clazz);
				int columnCount = cursor.getColumnCount();
				if (columnCount > 0) {
					T entity = (T) clazz.newInstance();
					for (int i = 0; i < columnCount; i++) {
						String column = cursor.getColumnName(i);
						Property property = table.propertyMap.get(column);
						if (property != null) {
							property.setValue(entity, cursor.getString(i));
						}
					}
					return entity;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final <T> boolean isEmpty(List<T> lists) {
		if (lists == null || lists.size() == 0) {
			return true;
		}
		return false;
	}

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Method getFieldGetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		Method m = null;
		if (f.getType() == boolean.class) {
			m = getBooleanFieldGetMethod(clazz, fn);
		}
		if (m == null) {
			m = getFieldGetMethod(clazz, fn);
		}
		return m;
	}

	public static Method getFieldSetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		try {
			return clazz.getDeclaredMethod(mn, f.getType());
		} catch (NoSuchMethodException e) {
			if (f.getType() == boolean.class) {
				return getBooleanFieldSetMethod(clazz, f);
			}
		}
		return null;
	}

	public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		if (isISStart(f.getName())) {
			mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
		}
		try {
			return clazz.getDeclaredMethod(mn, f.getType());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 布尔值 对外提供的set方法是isXxx
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Method getBooleanFieldGetMethod(Class<?> clazz, String fieldName) {
		String mn = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		if (isISStart(fieldName)) {
			mn = fieldName;
		}
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isISStart(String fieldName) {
		if (fieldName == null || fieldName.trim().length() == 0)
			return false;
		//is开头，并且is之后第一个字母是大写 比如 isOk
		return fieldName.startsWith("is") && !Character.isLowerCase(fieldName.charAt(2));
	}

	public static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
		String mn = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date stringToDateTime(String strDate) {
		if (strDate != null) {
			try {
				return SDF.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 根据实体类 获得 实体类对应的表名
	 * @param entity
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table == null || table.name().trim().length() == 0) {
			//当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(_)
			return clazz.getName().replace('.', '_');
		}
		return table.name();
	}

	/**
	 * 将对象转换为Property集合，也就是ContentValues
	 * @param entity
	 * @param selective 是否忽略 值为null的字段
	 * @return
	 */
	public static List<Property> getPropertyList(Class<?> clazz) {

		ArrayList<Property> plist = new ArrayList<Property>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				Property property = new Property();
				property.setColumn(getPropertyName(f));
				property.setDefaultValue(getPropertyDefaultValue(f));
				property.setGet(DbUtil.getFieldGetMethod(clazz, f));
				property.setSet(DbUtil.getFieldSetMethod(clazz, f));
				property.setDataType(f.getType());
				property.setField(f);
				plist.add(property);
			}
			return plist;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String getPropertyDefaultValue(Field field) {
		A.Property property = field.getAnnotation(A.Property.class);
		if (property != null && property.defaultValue().trim().length() != 0) {
			return property.defaultValue();
		}
		return null;
	}

	public static String getPropertyName(Field field) {
		A.Property property = field.getAnnotation(A.Property.class);
		if (property != null && property.column().trim().length() != 0) {
			return property.column().trim();
		}
		return field.getName();
	}
	public static ArrayList<String> stringToArrayList(String value){
		String[] array= value.replace("[", "").replace("]", "").split(",");
		ArrayList<String> list = new ArrayList<String>();
		for (String item : array) {
			list.add(item);
		}
		return list;
	}
}
