package com.kubeiwu.commontool.db.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.provider.BaseColumns;
import android.text.TextUtils;

import com.kubeiwu.commontool.db.table.KeyValue;
import com.kubeiwu.commontool.db.table.Property;
import com.kubeiwu.commontool.db.table.TableInfo;

/**
 * 拼接sql字符串辅助类
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-18
 */
public class SqlBuilder {

	public static String getCreatTableSQL(Class<?> clazz) {
		TableInfo table = TableInfo.get(clazz);
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(table.getTableName());
		strSQL.append(" ( ");
		strSQL.append(BaseColumns._ID);//主键
		strSQL.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			if (!BaseColumns._ID.toUpperCase().equals(property.getColumn().toUpperCase())) {
				strSQL.append(property.getColumn());
				strSQL.append(",");
			}
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" )");
		return strSQL.toString();
	}

	private static final String[] CONFLICT_VALUES = new String[] { "", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE " };

	/**
	 * 获取插入的sql语句
	 * @return
	 */
	public static SqlInfo buildInsertSql(Object entity, int conflictAlgorithm) {

		List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);
		StringBuffer strSQL = new StringBuffer();
		SqlInfo sqlInfo = null;
		if (keyValueList != null && keyValueList.size() > 0) {

			sqlInfo = new SqlInfo();
			strSQL.append("INSERT");
			strSQL.append(CONFLICT_VALUES[conflictAlgorithm]);
			strSQL.append(" INTO ");
			strSQL.append(TableInfo.get(entity.getClass()).getTableName());
			strSQL.append(" (");
			for (KeyValue kv : keyValueList) {
				strSQL.append(kv.getKey()).append(",");
				sqlInfo.addValue(kv.getValue());
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(") VALUES ( ");

			int length = keyValueList.size();
			for (int i = 0; i < length; i++) {
				strSQL.append("?,");
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(")");

			sqlInfo.setSql(strSQL.toString());
		}

		return sqlInfo;
	}

	public static List<KeyValue> getSaveKeyValueListByEntity(Object entity) {

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();

		TableInfo table = TableInfo.get(entity.getClass());

		//添加属性
		Collection<Property> propertys = table.propertyMap.values();
		//		Log.e("propertys", propertys.size() + "");
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			//			Log.e("KeyValue", kv + "");
			if (kv != null)
				keyValueList.add(kv);
		}

		return keyValueList;
	}

	private static KeyValue property2KeyValue(Property property, Object entity) {
		KeyValue kv = null;
		String pcolumn = property.getColumn();
		Object value = property.getValue(entity);
		//		Log.e("value", value + "");
		if (value != null) {
			kv = new KeyValue(pcolumn, value);
		} else {
			if (property.getDefaultValue() != null && property.getDefaultValue().trim().length() != 0)
				kv = new KeyValue(pcolumn, property.getDefaultValue());
		}
		return kv;
	}

	public static String getSelectSQLByWhereAndOrderBy(Class<?> clazz, String strWhere, String orderBy) {
		TableInfo table = TableInfo.get(clazz);

		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));

		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		if (!TextUtils.isEmpty(orderBy)) {
			strSQL.append(" ORDER BY ").append(orderBy);
		}

		return strSQL.toString();
	}

	//---------------------------查询sql
	private static String getSelectSqlByTableName(String tableName) {
		return new StringBuffer("SELECT * FROM ").append(tableName).toString();
	}

	public static String getSelectSQL(Class<?> clazz) {
		return getSelectSqlByTableName(TableInfo.get(clazz).getTableName());
	}

	private static String getDeleteSqlBytableName(String tableName) {
		return "DELETE FROM " + tableName;
	}

	public static SqlInfo buildDeleteSql(Class<?> clazz, Object idValue) {
		TableInfo table = TableInfo.get(clazz);

		if (null == idValue) {
			throw new IllegalArgumentException("getDeleteSQL:idValue is null");
		}
		StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
		strSQL.append(" WHERE ").append(BaseColumns._ID).append("=?");

		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idValue);

		return sqlInfo;
	}

	/**
	* 根据条件删除数据 ，条件为空的时候将会删除所有的数据
	* @param clazz
	* @param strWhere
	* @return
	*/
	public static String buildDeleteSql(Class<?> clazz, String strWhere) {
		TableInfo table = TableInfo.get(clazz);
		StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));

		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		return strSQL.toString();
	}//////////////////////////////update sql start/////////////////////////////////////////////

	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity) {

		TableInfo table = TableInfo.get(entity.getClass());
		Object idvalue = table.propertyMap.get(BaseColumns._ID);
		if (idvalue == null)//主键值不能为null，否则不能更新
			throw new IllegalArgumentException("this entity[" + entity.getClass() + "]'s id value is null");

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		//添加属性
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}
		if (keyValueList == null || keyValueList.size() == 0)
			return null;
		SqlInfo sqlInfo = new SqlInfo();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");
		for (KeyValue kv : keyValueList) {
			strSQL.append(kv.getKey()).append("=?,");
			sqlInfo.addValue(kv.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" WHERE ").append(BaseColumns._ID + "=?");
		sqlInfo.addValue(idvalue);
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}
}
