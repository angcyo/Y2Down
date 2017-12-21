package com.angcyo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 数据库版本管理类,用于首次创建数据库,获取数据库操作的对象.创建数据库,更新数据库,创建表
 * 
 * @author angcyo
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// CursorFactory设置为null,使用默认值
		super(context, name, factory, version);

	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS [apk_info]"
				+ "([_id] INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "[apk_name] NVARCHAR not null, [apk_ver] NVARCHAR not null, [apk_ico] NVARCHAR not null,"
				+ "[apk_des] NTEXT DEFAULT '没有描述', [apk_url] NTEXT,[apk_size] NVARCHAR,[apk_time] datetime)");

		String sql = "CREATE TABLE IF NOT EXISTS add_url"
				+ "([_id] INTEGER PRIMARY KEY AUTOINCREMENT," + "[url] NVARCHAR)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	}

}
