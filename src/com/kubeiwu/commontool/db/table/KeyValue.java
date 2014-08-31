package com.kubeiwu.commontool.db.table;

import java.util.ArrayList;

import com.kubeiwu.commontool.db.utils.DbUtil;

public class KeyValue {
	private String key;
	private Object value;

	public KeyValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public KeyValue() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		if (value instanceof java.util.Date || value instanceof java.sql.Date) {
			return DbUtil.SDF.format(value);
		} else if (value instanceof ArrayList) {
			return value.toString();
		}

		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
