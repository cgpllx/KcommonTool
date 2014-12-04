package com.kubeiwu.commontool.db.table;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
			return objectToByteArray(value);
		}
		return value;
	}

	public byte[] objectToByteArray(Object pojo) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos); 
			oos.writeObject(pojo); 
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(baos);
			close(oos);
		}
		return null;
	}

	private void close(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
