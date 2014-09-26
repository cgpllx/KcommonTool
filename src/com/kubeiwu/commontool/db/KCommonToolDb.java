package com.kubeiwu.commontool.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kubeiwu.commontool.db.sqlite.SqlBuilder;
import com.kubeiwu.commontool.db.sqlite.SqlInfo;
import com.kubeiwu.commontool.db.table.TableInfo;
import com.kubeiwu.commontool.db.utils.DbUtil;

/**
 * 类名做表名，属性作为字段名称
 * @author  cgpllx1@qq.com (www.kubeiwu.com)
 * @date    2014-8-15
 */  
public class KCommonToolDb {
	private static final String TAG = "KCommonToolDb";
	//String 库名  MyKToolDb  是对象
	private static HashMap<String, KCommonToolDb> daoMap = new HashMap<String, KCommonToolDb>();
	private SQLiteDatabase db;

	//private DaoConfig config;

	private KCommonToolDb(DaoConfig config) {
		if (config == null)
			throw new IllegalArgumentException("daoConfig is null");
		if (config.getContext() == null)
			throw new IllegalArgumentException("android context is null");
		if (config.getTargetDirectory() != null && config.getTargetDirectory().trim().length() > 0) {
			this.db = createDbFileOnSDCard(config.getTargetDirectory(), config.getDbName());
		} else {
			this.db = new SqliteDbHelper(config.getContext().getApplicationContext(), config.getDbName(), config.getDbVersion())
					.getWritableDatabase();
		}
		//this.config = config;
	}
	
	public static KCommonToolDb create(DaoConfig config) {
		return getInstance(config);
	}

	private synchronized static KCommonToolDb getInstance(DaoConfig daoConfig) {
		KCommonToolDb dao = daoMap.get(daoConfig.getDbName());
		if (dao == null) {
			dao = new KCommonToolDb(daoConfig);
			daoMap.put(daoConfig.getDbName(), dao);
		}
		return dao;
	}

	//插入和替换
	public static final int CONFLICT_REPLACE = 5;

	//none
	public static final int CONFLICT_NONE = 0;

	/**
	 * 存储对象到数据库
	 * @param <T>
	 * @param entity
	 */
	public void insert(Object entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.buildInsertSql(entity, CONFLICT_NONE));
	}

	public void replace(Object entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.buildInsertSql(entity, CONFLICT_REPLACE));
	}

	public <T> void insertAll(List<T> entitys) {
		insertWithOnConflict(entitys, CONFLICT_NONE);
	}

	public <T> void insertOrReplaceAll(List<T> entitys) {
		insertWithOnConflict(entitys, CONFLICT_REPLACE);
	}
	public <T> void insertOrReplace(T entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.buildInsertSql(entity, CONFLICT_REPLACE));
	}

	private <T> void insertWithOnConflict(List<T> entitys, int conflictAlgorithm) {
		if (DbUtil.isEmpty(entitys))
			throw new IllegalArgumentException("To save the object cannot be empty");
		checkTableExist(entitys.get(0).getClass());
		try {
			db.beginTransaction();
			for (Object entity : entitys) {
				exeSqlInfo(SqlBuilder.buildInsertSql(entity, conflictAlgorithm));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	private void exeSqlInfo(SqlInfo sqlInfo) {
		if (sqlInfo != null) {
			db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
		} else {
			Log.e(TAG, "sava error:sqlInfo is null");
		}
	}

	private void checkTableExist(Class<?> clazz) {
		if (!tableIsExist(TableInfo.get(clazz))) {
			String sql = SqlBuilder.getCreatTableSQL(clazz);
			db.execSQL(sql);
		}
	}

	/**
	 * 查找所有的数据
	 * 
	 * @param clazz
	 */
	public <T> List<T> findAll(Class<T> clazz) {
		checkTableExist(clazz);
		return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz));
	}
	/**
	 * 根据条件更新数据
	 * @param entity
	 * @param strWhere
	 *            条件为空的时候，将会更新所有的数据
	 */
	public void update(Object entity, String strWhere) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
	}
	/**
	 * 根据条件查找所有数据
	 * 
	 * @param clazz
	 * @param strSQL
	 */
	private <T> List<T> findAllBySql(Class<T> clazz, String strSQL) {
		checkTableExist(clazz);
		Cursor cursor = db.rawQuery(strSQL, null);
		try {
			List<T> list = new ArrayList<T>();
			while (cursor.moveToNext()) {
				T t = DbUtil.getEntity(cursor, clazz, this);
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}
		return null;
	}

	/**
	 * 根据条件查询
	 * @param clazz
	 * @param strWhere  eg "_id=1"
	 * @param orderBy  eg  "_id"  DESC 表示按倒序排序(即:从大到小排序) 用 ACS 表示按正序排序(即:从小到大排序)
	 * @return
	 */
	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy) {
		checkTableExist(clazz);
		return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhereAndOrderBy(clazz, strWhere, orderBy));
	}
	
	/**
	 * 根据条件查询
	 * @param clazz
	 * @param strWhere  eg "_id=1"
	 * @param orderBy  eg  "_id"  DESC 表示按倒序排序(即:从大到小排序) 用 ACS 表示按正序排序(即:从小到大排序)
	 * @return
	 */
	public <T> List<T> findFieldByWhere(Class<T> clazz, String[] fields,String strWhere, String orderBy) {
		checkTableExist(clazz);
		return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhereAndOrderBy(clazz, strWhere, orderBy));
	}

	public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
		return findAllByWhere(clazz, strWhere, null);
	}

	private boolean tableIsExist(TableInfo table) {
		if (table.isCheckDatabese())
			return true;

		Cursor cursor = null;
		try {//  每一个 SQLite 数据库都有一个叫 SQLITE_MASTER 的表，  里面包含所有表的信息，可以查找某一个表是否存在
			String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + table.getTableName() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					table.setCheckDatabese(true);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			cursor = null;
		}
		return false;
	}

	/**
	 * 根据主键删除数据
	 * 
	 * @param clazz
	 *            要删除的实体类
	 * @param id
	 *            主键值
	 */
	public void deleteById(Class<?> clazz, Object id) {
		checkTableExist(clazz);
		exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
	}

	/**
	 * 在SD卡的指定目录上创建文件
	 * 
	 * @param sdcardPath
	 * @param dbfilename
	 * @return
	 */
	private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename) {
		File dbf = new File(sdcardPath, dbfilename);
		if (!dbf.exists()) {
			try {
				if (dbf.createNewFile()) {
					return SQLiteDatabase.openOrCreateDatabase(dbf, null);
				}
			} catch (IOException ioex) {
				throw new IllegalArgumentException("Failed to create a database file", ioex);
			}
		} else {
			return SQLiteDatabase.openOrCreateDatabase(dbf, null);
		}

		return null;
	}

	/**
	 * 更新数据 （主键ID必须不能为空）
	 * 
	 * @param entity
	 */
	public void update(Object entity) {
		checkTableExist(entity.getClass());
		exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
	}

	/**
	 * 删除表的所有数据
	 * 
	 * @param clazz
	 */
	public void deleteAll(Class<?> clazz) {
		deleteByWhere(clazz, null);
	}

	/**
	 * 根据条件删除数据
	 * 
	 * @param clazz
	 * @param strWhere
	 *            条件为空的时候 将会删除所有的数据
	 */
	public void deleteByWhere(Class<?> clazz, String strWhere) {
		checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
		db.execSQL(sql);
	}

	class SqliteDbHelper extends SQLiteOpenHelper {

		public SqliteDbHelper(Context context, String name, int version) {
			super(context, name, null, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

	/**
	 * 数据库配置
	 * @author  cgpllx1@qq.com (www.kubeiwu.com)
	 * @date    2014-8-15
	 */
	public static class DaoConfig {
		private Context mContext = null; // android上下文
		private String mDbName = "kubeiwu.db"; // 数据库名字
		private int dbVersion = 1; // 数据库版本
		private String targetDirectory;// 数据库文件在sd卡中的目录

		public String getTargetDirectory() {
			return targetDirectory;
		}

		public DaoConfig setTargetDirectory(String targetDirectory) {
			this.targetDirectory = targetDirectory;
			return this;
		}

		public Context getContext() {
			return mContext;
		}

		public DaoConfig setContext(Context context) {
			this.mContext = context;
			return this;
		}

		public String getDbName() {
			return mDbName;
		}

		public DaoConfig setDbName(String dbName) {
			this.mDbName = dbName;
			return this;
		}

		public int getDbVersion() {
			return dbVersion;
		}

		public DaoConfig setDbVersion(int dbVersion) {
			this.dbVersion = dbVersion;
			return this;
		}
	}
}
