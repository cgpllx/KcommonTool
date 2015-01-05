package com.kubeiwu.commontool.db.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import android.provider.BaseColumns;
import android.text.TextUtils;
import com.kubeiwu.commontool.db.table.KeyValue;
import com.kubeiwu.commontool.db.table.Property;
import com.kubeiwu.commontool.db.table.TableInfo;
import com.kubeiwu.commontool.db.utils.DbUtil;

/**
 * 拼接sql字符串辅助类
 * 
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-18
 */
public class SqlBuilder {

	public static String getCreatTableSQL(Class<?> clazz) {
	 
		return getCreatTableSQL(null, clazz);
	}
	public static String getCreatTableSQL(String tabName,Class<?> clazz) {
		TableInfo table = TableInfo.get(tabName,clazz);
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(table.getTableName());
		strSQL.append(" ( ");
		strSQL.append(BaseColumns._ID);// 主键
		strSQL.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			if (!BaseColumns._ID.equals(property.getColumn().toLowerCase(Locale.CHINA))) {
				strSQL.append(property.getColumn());
				if (DbUtil.checkColumnUnique(property.getField())) {
					strSQL.append(" UNIQUE");
				}
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
	 * 
	 * @return
	 */
	public static SqlInfo buildInsertSql(Object entity, int conflictAlgorithm,String tabName) {

		List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);
		StringBuffer strSQL = new StringBuffer();
		SqlInfo sqlInfo = null;
		if (keyValueList != null && keyValueList.size() > 0) {

			sqlInfo = new SqlInfo();
			strSQL.append("INSERT");
			strSQL.append(CONFLICT_VALUES[conflictAlgorithm]);
			strSQL.append(" INTO ");
			strSQL.append(TableInfo.get(tabName,entity.getClass()).getTableName());
			strSQL.append(" (");
			boolean primary_key_utomatically = false;
			for (KeyValue kv : keyValueList) {
				String key = kv.getKey();
				Object value = kv.getValue();// sql插入时候不能返回主键id，所以只有重新查询的时候才能知道主键
				if (BaseColumns._ID.equals(key)) {
					if ("-1".equals(value.toString())) {
						primary_key_utomatically = true;
						continue;
					}
				}
				strSQL.append(key).append(",");
				sqlInfo.addValue(value);
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(") VALUES ( ");

			int length = keyValueList.size();
			if (primary_key_utomatically) {
				length--;
			}
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

		// 添加属性
		Collection<Property> propertys = table.propertyMap.values();
		// Log.e("propertys", propertys.size() + "");
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			// Log.e("KeyValue", kv + "");
			if (kv != null)
				keyValueList.add(kv);
		}

		return keyValueList;
	}

	private static KeyValue property2KeyValue(Property property, Object entity) {
		KeyValue kv = null;
		String pcolumn = property.getColumn();
		Object value = property.getValue(entity);
		// Log.e("pcolumn", pcolumn + "");
		// Log.e("value", value + "");
		if (value != null) {
			kv = new KeyValue(pcolumn, value);
		} else {
			if (property.getDefaultValue() != null && property.getDefaultValue().trim().length() != 0)
				kv = new KeyValue(pcolumn, property.getDefaultValue());
		}
		return kv;
	}

	// fields
	public static String getSelectSQLByWhereAndOrderBy(Class<?> clazz, String strWhere, String orderBy, String limit) {
		TableInfo table = TableInfo.get(clazz);

		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));

		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		if (!TextUtils.isEmpty(orderBy)) {
			strSQL.append(" ORDER BY ").append(orderBy);
		}
		if (!TextUtils.isEmpty(limit)) {
			strSQL.append(" LIMIT ").append(limit);
		}
		
		return strSQL.toString();
	}

	// ---------------------------查询sql
	private static String getSelectSqlByTableName(String tableName) {
		return getSelectSqlByTableName(tableName, null);
	}

	private static String getSelectSqlByTableName(String tableName, String[] fields) {
		if (fields == null || fields.length == 0) {
			return new StringBuffer("SELECT * FROM ").append(tableName).toString();
		} else {
			StringBuilder builder = new StringBuilder();
			for (String filed : fields) {
				builder.append(filed);
				builder.append(",");
			}
			return new StringBuffer("SELECT ").append(builder.toString()).append(" FROM ").append(tableName).toString();
		}
	}

	public static String getSelectSQL(Class<?> clazz,String tabName) {
		return getSelectSqlByTableName(TableInfo.get(tabName,clazz).getTableName());
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
	 * 
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
	}// ////////////////////////////update sql start/////////////////////////////////////////////

	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity) {

		TableInfo table = TableInfo.get(entity.getClass());
		Object idvalue = table.propertyMap.get(BaseColumns._ID).getValue(entity);
		if (idvalue == null)// 主键值不能为null，否则不能更新
			throw new IllegalArgumentException("this entity[" + entity.getClass() + "]'s id value is null");

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		// 添加属性
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
		strSQL.append(" WHERE ").append(BaseColumns._ID + "=? ");
		sqlInfo.addValue(idvalue);
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}

	// "set a=cgp where d=1;
	public static SqlInfo getUpdateSqlAsSqlInfo1(Class<?> clazz, String strWhere, String[] attributes, String[] needvalues) {
		TableInfo table = TableInfo.get(clazz);

		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");

		SqlInfo sqlInfo = new SqlInfo();
		for (int i = 0; i < attributes.length; i++) {
			strSQL.append(attributes[i]).append("=?,");
			sqlInfo.addValue(needvalues[i]);
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}

	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere) {

		TableInfo table = TableInfo.get(entity.getClass());

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();

		// 添加属性
		Collection<Property> propertys = table.propertyMap.values();
		for (Property property : propertys) {
			KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		SqlInfo sqlInfo = new SqlInfo();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");
		for (KeyValue kv : keyValueList) {
			strSQL.append(kv.getKey()).append("=?,");
			sqlInfo.addValue(kv.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}
}
