package com.angcyo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * ���ݿ�汾������,�����״δ������ݿ�,��ȡ���ݿ�����Ķ���.�������ݿ�,�������ݿ�,������
 * 
 * @author angcyo
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// CursorFactory����Ϊnull,ʹ��Ĭ��ֵ
		super(context, name, factory, version);

	}

	// ���ݿ��һ�α�����ʱonCreate�ᱻ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS [apk_info]"
				+ "([_id] INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "[apk_name] NVARCHAR not null, [apk_ver] NVARCHAR not null, [apk_ico] NVARCHAR not null,"
				+ "[apk_des] NTEXT DEFAULT 'û������', [apk_url] NTEXT,[apk_size] NVARCHAR,[apk_time] datetime)");

		String sql = "CREATE TABLE IF NOT EXISTS add_url"
				+ "([_id] INTEGER PRIMARY KEY AUTOINCREMENT," + "[url] NVARCHAR)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// ���DATABASE_VERSIONֵ����Ϊ2,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade
	}

}
