package com.kubeiwu.commontool.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;

import com.kubeiwu.commontool.db.utils.DbUtil;

public class Property {

	private String column;// fieldName字段名称就是
	private String defaultValue;
	private Method get;// get方法，用来取值后存入数据库
	private Method set;// 对对象赋值用
	private Field field;// 如果没有set方法，讲通过field进行赋值
	private Class<?> dataType;// 字段的类型，也就是类的属性的类型

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getGet() {
		return get;
	}

	public void setGet(Method get) {
		this.get = get;
	}

	public Method getSet() {
		return set;
	}

	public void setSet(Method set) {
		this.set = set;
	}

	/**
	 * 获取某个实体执行某个方法的结果
	 * 
	 * @param obj
	 * @param method
	 * @return
	 */
	public Object getValue(Object obj) {
		if (obj != null && get != null) {
			try {
				return get.invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 解析为对象的属性类型
	 * 
	 * @param receiver
	 * @param value
	 */
	public void setValue(Object receiver, String value) {
		if (set != null && value != null) {
			try {
				if (dataType == String.class) {
					set.invoke(receiver, value.toString());
				} else if (dataType == int.class || dataType == Integer.class) {
					set.invoke(receiver, value == null ? null : Integer.parseInt(value.toString()));
				} else if (dataType == float.class || dataType == Float.class) {
					set.invoke(receiver, value == null ? null : Float.parseFloat(value.toString()));
				} else if (dataType == double.class || dataType == Double.class) {
					set.invoke(receiver, value == null ? null : Double.parseDouble(value.toString()));
				} else if (dataType == long.class || dataType == Long.class) {
					set.invoke(receiver, value == null ? null : Long.parseLong(value.toString()));
				} else if (dataType == java.util.Date.class || dataType == java.sql.Date.class) {
					set.invoke(receiver, value == null ? (Date) null : DbUtil.stringToDateTime(value.toString()));
				} else if (dataType == boolean.class || dataType == Boolean.class) {
					set.invoke(receiver, value == null ? null : "1".equals(value.toString()));
				} else if (dataType == ArrayList.class) {
					set.invoke(receiver, DbUtil.stringToArrayList(value));
				} else if (dataType == byte[].class || dataType == Byte[].class) {
					set.invoke(receiver, value.getBytes());
				} else {
					set.invoke(receiver, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				field.setAccessible(true);
				field.set(receiver, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setValue(Object receiver, Cursor value) {
		//待开发中..... 
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
