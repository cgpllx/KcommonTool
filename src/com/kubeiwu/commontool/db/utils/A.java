package com.kubeiwu.commontool.db.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class A {
	/**
	 * 主键的id column必须为"_id"字符串，或者使用BaseColumns._ID
	 * 
	 * @author cgpllx1@qq.com (www.kubeiwu.com)
	 * @date 2014-8-15
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface KProperty {

		public String column() default "";

		public String defaultValue() default "";

		public boolean unique() default false;

	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface KIsNotProperty {
		// 不需要存入数据库的属性用KIsNotProperty注解
	}

	/**
	 * 表名，一般放 在类名上面
	 * 
	 * @author cgpllx1@qq.com (www.kubeiwu.com)
	 * @date 2014-8-18
	 */
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface KTable {
		public String name();
	}

}
