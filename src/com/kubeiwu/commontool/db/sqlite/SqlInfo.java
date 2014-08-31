package com.kubeiwu.commontool.db.sqlite;

import java.util.LinkedList;

/**包含sql字符串和参数
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-15
 */
public class SqlInfo {
	private String sql;
	private LinkedList<Object> bindArgs;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public LinkedList<Object> getBindArgs() {
		return bindArgs;
	}

	public void setBindArgs(LinkedList<Object> bindArgs) {
		this.bindArgs = bindArgs;
	}

	public Object[] getBindArgsAsArray() {
		if (bindArgs != null)
			return bindArgs.toArray();
		return null;
	}

	public String[] getBindArgsAsStringArray() {
		if (bindArgs != null) {
			String[] strings = new String[bindArgs.size()];
			for (int i = 0; i < bindArgs.size(); i++) {
				strings[i] = bindArgs.get(i).toString();
			}
			return strings;
		}
		return null;
	}

	public void addValue(Object obj) {
		if (bindArgs == null)
			bindArgs = new LinkedList<Object>();

		bindArgs.add(obj);
	}

}
