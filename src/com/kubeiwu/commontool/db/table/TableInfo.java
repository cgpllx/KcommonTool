package com.kubeiwu.commontool.db.table;

import java.util.HashMap;
import java.util.List;

import com.kubeiwu.commontool.db.utils.DbUtil;

/**
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-18
 */
public class TableInfo {
	private boolean checkDatabese;// 在对实体进行数据库操作的时候查询是否已经有表了，只需查询一遍，用此标示
	private String className;// 对象名称，

	private String tableName;// 表名，没有注解表名时，就用className当表名用
	public final HashMap<String, Property> propertyMap = new HashMap<String, Property>();// String 健名称

	public boolean isCheckDatabese() {
		return checkDatabese;
	}

	/**
	 * key是数据库表名
	 */
	private static final HashMap<String, TableInfo> tableInfoMap = new HashMap<String, TableInfo>();// 所有表放在这里 key是对象的name

	public void setCheckDatabese(boolean checkDatabese) {
		this.checkDatabese = checkDatabese;
	}

	public static TableInfo get(Class<?> clazz) {
		
		return get(DbUtil.getTableName(clazz), clazz);
	}

	public static TableInfo get(String tabName, Class<?> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("table info get error,because the clazz is null");
		if(tabName==null){
			tabName=DbUtil.getTableName(clazz);
		}
		TableInfo tableInfo = tableInfoMap.get(tabName);
		if (tableInfo == null) {// 没有就创建表
			tableInfo = new TableInfo();
			tableInfo.setTableName(tabName);
			tableInfo.setClassName(clazz.getName());
			List<Property> pList = DbUtil.getPropertyList(clazz);
			if (pList != null) {
				for (Property p : pList) {
					if (p != null)
						tableInfo.propertyMap.put(p.getColumn(), p);
				}
			}
			tableInfoMap.put(tabName, tableInfo);
		}
		return tableInfo;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
