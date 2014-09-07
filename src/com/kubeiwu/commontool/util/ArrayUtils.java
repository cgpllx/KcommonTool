package com.kubeiwu.commontool.util;

import java.util.List;

public class ArrayUtils {

	public static boolean isEmpty(List<Object> array) {
		if (array == null || array.size() == 0)
			return true;
		else
			return false;
	}
}
